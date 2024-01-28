package org.team.b6.catchtable.domain.member.service

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team.b6.catchtable.domain.blacklist.entity.BlackList
import org.team.b6.catchtable.domain.blacklist.entity.BlackListReason
import org.team.b6.catchtable.domain.blacklist.repository.BlackListRepository
import org.team.b6.catchtable.domain.member.dto.request.SignupMemberRequest
import org.team.b6.catchtable.domain.member.dto.response.BannedMemberResponse
import org.team.b6.catchtable.domain.member.repository.MemberRepository
import org.team.b6.catchtable.domain.review.dto.response.ReviewResponse
import org.team.b6.catchtable.domain.review.model.Review
import org.team.b6.catchtable.domain.review.model.ReviewStatus
import org.team.b6.catchtable.domain.review.repository.ReviewRepository
import org.team.b6.catchtable.domain.store.dto.response.StoreResponse
import org.team.b6.catchtable.domain.store.model.StoreStatus
import org.team.b6.catchtable.global.service.FindingEntityService
import org.team.b6.catchtable.global.service.UtilService
import org.team.b6.catchtable.global.variable.Variables
import java.time.LocalDateTime

@Service
@Transactional
class AdminService(
    private val findingEntityService: FindingEntityService,
    private val utilService: UtilService,
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder, // TODO : 추후 삭제 (테스트용)
    private val reviewRepository: ReviewRepository,
    private val blackListRepository: BlackListRepository
) {
    // 계정 정지가 만료된 회원을 10초에 한 번씩 확인
    @Scheduled(fixedDelay = 1000 * 10)
    fun liftSuspension(){
        findingEntityService.getAllMembers()
            .filter { it.bannedExpiration != null && it.bannedExpiration!! < LocalDateTime.now()}
            .map { it.liftSuspension() }
    }

    // ADMIN이 처리해야 하는 식당 관련 요구사항들을 조회
    fun findAllStoreRequirements() =
        findingEntityService.getAllStores()
            .filter { unavailableToReservation(it.status) }
            .map {
                StoreResponse.from(
                    store = it,
                    member = findingEntityService.getMember(it.belongTo),
                    reviews = findingEntityService.getAllReviews()
                        .filter { review -> review.store.id == it.id }
                        .map { review -> ReviewResponse.from(review) }
                )
            }

    // ADMIN이 처리해야 하는 리뷰 삭제 요구사항들을 조회
    fun findAllReviewDeleteRequirements() =
        findingEntityService.getAllReviews()
            .filter { it.status == ReviewStatus.REQUIRED_FOR_DELETE }
            .map { ReviewResponse.from(it) }

    // 현재 계정이 정지된 USER 목록을 조회
    fun findAllBannedMembers() =
        findingEntityService.getAllMembers()
            .filter { it.bannedExpiration != null && it.bannedExpiration!! > LocalDateTime.now() }
            .map {
                BannedMemberResponse.from(
                    member = it,
                    reasons = findingEntityService.getAllBlackLists()
                        .filter { blacklist -> blacklist.subject == it.id }
                        .map { blacklist -> blacklist.reason.name }
                        .toSet().joinToString(" ")
                )
            }

    // 식당 관련 요구사항들을 승인 혹은 거절 (식당 등록 및 삭제 요청)
    fun handleStoreRequirement(storeId: Long, isAccepted: Boolean) =
        findingEntityService.getStore(storeId).let {
            when (it.status) {
                StoreStatus.WAITING_FOR_CREATE -> {
                    if (isAccepted) {
                        utilService.sendMail(
                            email = findingEntityService.getMember(it.belongTo).email,
                            subject = Variables.MAIL_SUBJECT_ACCEPTED,
                            text = Variables.MAIL_CONTENT_STORE_CREATE_ACCEPTED
                        )
                        it.updateStatus(StoreStatus.OK)
                    } else utilService.sendMail(
                        email = findingEntityService.getMember(it.belongTo).email,
                        subject = Variables.MAIL_SUBJECT_REFUSED,
                        text = Variables.MAIL_CONTENT_STORE_CREATE_REFUSED
                    )
                }

                StoreStatus.WAITING_FOR_DELETE -> {
                    if (isAccepted) {
                        utilService.sendMail(
                            email = findingEntityService.getMember(it.belongTo).email,
                            subject = Variables.MAIL_SUBJECT_ACCEPTED,
                            text = Variables.MAIL_CONTENT_STORE_DELETE_ACCEPTED
                        )
                        it.updateForDelete()
                        deleteAllComments(it.id!!)
                    } else utilService.sendMail(
                        email = findingEntityService.getMember(it.belongTo).email,
                        subject = Variables.MAIL_SUBJECT_REFUSED,
                        text = Variables.MAIL_CONTENT_STORE_DELETE_REFUSED
                    )

                }

                else -> {}
            }
        }

    // 리뷰 관련 요구사항들을 승인 혹은 거절 (리뷰 삭제 요청)
    fun handleReviewRequirement(reviewId: Long, isAccepted: Boolean) {
        val review = findingEntityService.getReview(reviewId)
        if (isAccepted) {
            utilService.sendMail( // OWNER
                email = findingEntityService.getMember(review.store.belongTo).email,
                subject = Variables.MAIL_SUBJECT_ACCEPTED,
                text = Variables.MAIL_CONTENT_REVIEW_DELETE_ACCEPTED
            )
            utilService.sendMail( // USER
                email = review.member.email,
                subject = Variables.MAIL_SUBJECT_WARN,
                text = Variables.MAIL_CONTENT_REVIEW_WARN + additionalMailContent(review)
            )
            val blacklist = BlackList(
                subject = review.member.id!!,
                reason = BlackListReason.TROLLING_REVIEW
            )
            blackListRepository.save(blacklist)
            blacklist.addWarnings(review.member, utilService)
            reviewRepository.delete(review)
        } else {
            utilService.sendMail(
                email = findingEntityService.getMember(review.store.belongTo).email,
                subject = Variables.MAIL_SUBJECT_REFUSED,
                text = Variables.MAIL_CONTENT_REVIEW_DELETE_REFUSED
            )
        }
    }

    // TODO: 추후 삭제 (테스트용)
    fun registerAdmin(request: SignupMemberRequest) =
        memberRepository.save(request.to(passwordEncoder))

    // [내부 메서드] Store가 예약 가능하지 않은 상태인 경우를 판단
    private fun unavailableToReservation(status: StoreStatus) =
        (status == StoreStatus.WAITING_FOR_CREATE) || (status == StoreStatus.WAITING_FOR_DELETE) || (status == StoreStatus.DELETED)

    // [내부 메서드] Store 하위의 모든 리뷰들을 삭제
    private fun deleteAllComments(storeId: Long) =
        reviewRepository.findAll()
            .filter { review -> review.store.id == storeId }
            .map { review -> reviewRepository.delete(review) }

    // [내부 메서드] 악의성 리뷰를 작성한 USER에게 보내줄 ADMIN 메일의 추가적인 내용
    private fun additionalMailContent(review: Review) = """
        
        
        "작성 일시 : ${review.createdAt},
        "식당 이름 : ${review.store.name},
        "리뷰 내용 : ${review.content},
        "누적 경고 횟수 : ${review.member.numberOfWarnings}
    """.trimIndent()
}