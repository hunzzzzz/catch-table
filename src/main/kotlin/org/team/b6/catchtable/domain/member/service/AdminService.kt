package org.team.b6.catchtable.domain.member.service

import org.springframework.data.repository.findByIdOrNull
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
import org.team.b6.catchtable.domain.store.repository.StoreRepository
import org.team.b6.catchtable.global.exception.ModelNotFoundException
import org.team.b6.catchtable.global.service.GlobalService
import org.team.b6.catchtable.global.variable.Variables

@Service
@Transactional
class AdminService(
    private val globalService: GlobalService,
    private val javaMailSender: JavaMailSender,
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val reviewRepository: ReviewRepository,
    private val storeRepository: StoreRepository
) {
    @PreAuthorize("hasRole('ADMIN')")
    fun findAllStoreRequirements() =
        globalService.getAllStores().filter { unavailableToReservation(it.status) }

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
                        // 리뷰 삭제 코드
                    reviewRepository.findAll() // 모든 리뷰들을 꺼내온다
                        .filter { it.store.id == storeId } // 리뷰 중에서 storeId가 일치하는 리뷰들만 꺼내온다
                        .map { reviewRepository.delete(it) } // 지운다
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
    fun registerAdmin(request: SignupMemberRequest) {
        memberRepository.save(request.to(passwordEncoder))
    }

    private fun getStore(storeId: Long) =
        (storeRepository.findByIdOrNull(storeId) ?: throw ModelNotFoundException("식당"))

    private fun getMember(memberId: Long) =
        memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("멤버")

    private fun sendMail(email: String, subject: String, text: String) =
        javaMailSender.createMimeMessage().let {
            MimeMessageHelper(it, false).let { helper ->
                helper.setTo(email)
                helper.setSubject(subject)
                helper.setText(text)
            }
            javaMailSender.send(it)
        }

    private fun unavailableToReservation(status: StoreStatus) =
        (status == StoreStatus.WAITING_FOR_CREATE) || (status == StoreStatus.WAITING_FOR_DELETE) || (status == StoreStatus.DELETED)
}