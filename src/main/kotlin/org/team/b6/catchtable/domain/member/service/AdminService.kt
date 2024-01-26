package org.team.b6.catchtable.domain.member.service

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team.b6.catchtable.domain.member.dto.request.SignupMemberRequest
import org.team.b6.catchtable.domain.member.repository.MemberRepository
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
    @PreAuthorize("hasRole('ADMIN')")
    fun findAllStoreRequirements() =
        globalService.getAllStores().filter { unavailableToReservation(it.status) }

    // OWNER의 요구사항들을 승인 혹은 거절
    @PreAuthorize("hasRole('ADMIN')")
    fun handleRequirement(storeId: Long, isAccepted: Boolean) =
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

    // TODO: 추후 삭제 (테스트용)
    @PreAuthorize("hasRole('ADMIN')")
    fun registerAdmin(request: SignupMemberRequest) =
        memberRepository.save(request.to(passwordEncoder))

    // [내부 메서드] ADMIN이 OWNER의 요청을 승인/거절 시 OWNER에게 결과를 메일로 전송
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
}