package org.team.b6.catchtable.domain.member.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team.b6.catchtable.domain.blacklist.entity.BlackList
import org.team.b6.catchtable.domain.blacklist.entity.BlackListReason
import org.team.b6.catchtable.domain.blacklist.repository.BlackListRepository
import org.team.b6.catchtable.domain.member.dto.request.SignupMemberRequest
import org.team.b6.catchtable.domain.member.repository.MemberRepository
import org.team.b6.catchtable.domain.review.dto.response.ReviewResponse
import org.team.b6.catchtable.domain.review.model.Review
import org.team.b6.catchtable.domain.review.model.ReviewStatus
import org.team.b6.catchtable.domain.review.repository.ReviewRepository
import org.team.b6.catchtable.domain.store.dto.response.StoreResponse
import org.team.b6.catchtable.domain.store.model.StoreStatus
import org.team.b6.catchtable.global.service.GlobalService
import org.team.b6.catchtable.global.service.UtilService
import org.team.b6.catchtable.global.variable.Variables

@Service
@Transactional
class AdminService(
    private val globalService: GlobalService,
    private val utilService: UtilService,
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder, // TODO : 추후 삭제 (테스트용)
    private val reviewRepository: ReviewRepository,
    private val blackListRepository: BlackListRepository
) {
    // ADMIN이 처리해야 하는 요구사항들을 조회
    fun findAllStoreRequirements() =
        globalService.getAllStores()
            .filter { unavailableToReservation(it.status) }
            .map {
                StoreResponse.from(
                    store = it,
                    member = globalService.getMember(it.belongTo),
                    reviews = globalService.getAllReviewsByStoreId(it.id!!)
                        .map { review -> ReviewResponse.from(review) })
            }

    // ADMIN이 처리해야 하는 리뷰 삭제 요구사항들을 조회
    fun findAllReviewDeleteRequirements() =
        globalService.getAllReviews()
            .filter { it.status == ReviewStatus.REQUIRED_FOR_DELETE }
            .map { ReviewResponse.from(it) }

    // 식당 관련 요구사항들을 승인 혹은 거절 (식당 등록 및 삭제 요청)
    fun handleStoreRequirement(storeId: Long, isAccepted: Boolean) =
        globalService.getStore(storeId).let {
            when (it.status) {
                StoreStatus.WAITING_FOR_CREATE -> {
                    if (isAccepted) {
                        utilService.sendMail(
                            email = globalService.getMember(it.belongTo).email,
                            subject = Variables.MAIL_SUBJECT_ACCEPTED,
                            text = Variables.MAIL_CONTENT_STORE_CREATE_ACCEPTED
                        )
                        it.updateStatus(StoreStatus.OK)
                    } else utilService.sendMail(
                        email = globalService.getMember(it.belongTo).email,
                        subject = Variables.MAIL_SUBJECT_REFUSED,
                        text = Variables.MAIL_CONTENT_STORE_CREATE_REFUSED
                    )
                }

                StoreStatus.WAITING_FOR_DELETE -> {
                    if (isAccepted) {
                        utilService.sendMail(
                            email = globalService.getMember(it.belongTo).email,
                            subject = Variables.MAIL_SUBJECT_ACCEPTED,
                            text = Variables.MAIL_CONTENT_STORE_DELETE_ACCEPTED
                        )
                        it.updateForDelete()
                        deleteAllComments(it.id!!)
                    } else utilService.sendMail(
                        email = globalService.getMember(it.belongTo).email,
                        subject = Variables.MAIL_SUBJECT_REFUSED,
                        text = Variables.MAIL_CONTENT_STORE_DELETE_REFUSED
                    )

                }

                else -> {}
            }
        }

    // 리뷰 관련 요구사항들을 승인 혹은 거절 (리뷰 삭제 요청)
    fun handleReviewRequirement(reviewId: Long, isAccepted: Boolean) {
        val review = globalService.getReview(reviewId)
        if (isAccepted) {
            utilService.sendMail( // OWNER
                email = globalService.getMember(review.store.belongTo).email,
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
                email = globalService.getMember(review.store.belongTo).email,
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

    private fun additionalMailContent(review: Review) = """
        
        
        "작성 일시 : ${review.createdAt},
        "식당 이름 : ${review.store.name},
        "리뷰 내용 : ${review.content} 
    """.trimIndent() // TODO : 누적된 경고 횟수도 보내줘야 할듯
}