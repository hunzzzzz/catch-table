package org.team.b6.catchtable.domain.member.service

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team.b6.catchtable.domain.member.dto.request.SignupMemberRequest
import org.team.b6.catchtable.domain.member.repository.MemberRepository
import org.team.b6.catchtable.domain.review.model.Review
import org.team.b6.catchtable.domain.review.repository.ReviewRepository
import org.team.b6.catchtable.domain.store.model.StoreStatus
import org.team.b6.catchtable.global.service.GlobalService
import org.team.b6.catchtable.global.variable.Variables

@Service
@Transactional
class AdminService(
    private val globalService: GlobalService,
    private val javaMailSender: JavaMailSender,
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder, // TODO : 추후 삭제 (테스트용)
    private val reviewRepository: ReviewRepository
) {
    // ADMIN이 처리해야 하는 요구사항들을 조회
    fun findAllStoreRequirements() =
        globalService.getAllStores().filter { unavailableToReservation(it.status) }

    // 식당 관련 요구사항들을 승인 혹은 거절 (식당 등록 및 삭제 요청)
    fun handleStoreRequirement(storeId: Long, isAccepted: Boolean) =
        globalService.getStore(storeId).let {
            when (it.status) {
                StoreStatus.WAITING_FOR_CREATE -> {
                    if (isAccepted) {
                        sendMail(
                            email = globalService.getMember(it.belongTo).email,
                            subject = Variables.MAIL_SUBJECT_ACCEPTED,
                            text = Variables.MAIL_CONTENT_STORE_CREATE_ACCEPTED
                        )
                        it.updateStatus(StoreStatus.OK)
                    } else sendMail(
                        email = globalService.getMember(it.belongTo).email,
                        subject = Variables.MAIL_SUBJECT_REFUSED,
                        text = Variables.MAIL_CONTENT_STORE_CREATE_REFUSED
                    )
                }

                StoreStatus.WAITING_FOR_DELETE -> {
                    if (isAccepted) {
                        sendMail(
                            email = globalService.getMember(it.belongTo).email,
                            subject = Variables.MAIL_SUBJECT_ACCEPTED,
                            text = Variables.MAIL_CONTENT_STORE_DELETE_ACCEPTED
                        )
                        it.updateForDelete()
                        deleteAllComments(it.id!!)
                    } else sendMail(
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
            sendMail( // OWNER
                email = globalService.getMember(review.store.belongTo).email,
                subject = Variables.MAIL_SUBJECT_ACCEPTED,
                text = Variables.MAIL_CONTENT_REVIEW_DELETE_ACCEPTED
            )
            sendMail( // USER
                email = review.member.email,
                subject = Variables.MAIL_SUBJECT_WARN,
                text = Variables.MAIL_CONTENT_REVIEW_WARN + additionalMailContent(review)
            )
            // TODO : 해당 멤버를 Blacklist에 추가하는 로직도 추가해야함
            reviewRepository.delete(review)
        } else {
            sendMail(
                email = globalService.getMember(review.store.belongTo).email,
                subject = Variables.MAIL_SUBJECT_REFUSED,
                text = Variables.MAIL_CONTENT_REVIEW_DELETE_REFUSED
            )
        }
    }

    // TODO: 추후 삭제 (테스트용)
    fun registerAdmin(request: SignupMemberRequest) =
        memberRepository.save(request.to(passwordEncoder))

    // [내부 메서드] 메일 전송 기능
    private fun sendMail(email: String, subject: String, text: String) =
        javaMailSender.createMimeMessage().let {
            MimeMessageHelper(it, false).let { helper ->
                helper.setTo(email)
                helper.setSubject(subject)
                helper.setText(text)
            }
            javaMailSender.send(it)
        }

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