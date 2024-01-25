package org.team.b6.catchtable.domain.member.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team.b6.catchtable.domain.store.model.StoreStatus
import org.team.b6.catchtable.domain.store.repository.StoreRepository
import org.team.b6.catchtable.global.exception.ModelNotFoundException
import org.team.b6.catchtable.global.variable.Variables

@Service
@Transactional
class AdminService(
    private val storeRepository: StoreRepository,
    private val javaMailSender: JavaMailSender
) {
    fun findAllStoreRequirements() =
        storeRepository.findAll()
            .filter { unavailableToReservation(it.status) }

    // TODO : 추후 코드 리팩토링 필요
    fun accept(storeId: Long) =
        getStore(storeId).let {
            when (it.status) {
                StoreStatus.WAITING_FOR_CREATE -> {
                    sendMail(
                        email = "", // TODO : 추후 대상 이메일 설정
                        text = Variables.MAIL_CONTENT_STORE_CREATE_ACCEPTED,
                        isAccepted = true
                    )
                    it.updateStatus(StoreStatus.OK)
                }

                StoreStatus.WAITING_FOR_DELETE -> {
                    sendMail(
                        email = "",
                        text = Variables.MAIL_CONTENT_STORE_DELETE_ACCEPTED,
                        isAccepted = true
                    )
                    it.updateForDelete()
                }

                else -> {}
            }
        }

    private fun getStore(storeId: Long) =
        (storeRepository.findByIdOrNull(storeId) ?: throw ModelNotFoundException("식당"))

    private fun sendMail(email: String, text: String, isAccepted: Boolean) {
        val mimeMessage = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, false)
        helper.setTo(email)
        helper.setSubject(Variables.MAIL_SUBJECT)
        helper.setText(text)
        javaMailSender.send(mimeMessage)
    }


    private fun unavailableToReservation(status: StoreStatus) =
        (status == StoreStatus.WAITING_FOR_CREATE) || (status == StoreStatus.WAITING_FOR_DELETE) || (status == StoreStatus.DELETED)
}