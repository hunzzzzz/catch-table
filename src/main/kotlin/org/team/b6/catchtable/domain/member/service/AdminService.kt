package org.team.b6.catchtable.domain.member.service

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team.b6.catchtable.domain.store.model.StoreStatus
import org.team.b6.catchtable.global.service.GlobalService
import org.team.b6.catchtable.global.variable.Variables

@Service
@Transactional
class AdminService(
    private val globalService: GlobalService,
    private val javaMailSender: JavaMailSender
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
                            text = Variables.MAIL_CONTENT_STORE_CREATE_ACCEPTED
                        )
                        it.updateStatus(StoreStatus.OK)
                    } else sendMail(
                        email = globalService.getMember(it.belongTo).email,
                        text = Variables.MAIL_CONTENT_STORE_CREATE_REFUSED
                    )
                }

                StoreStatus.WAITING_FOR_DELETE -> {
                    if (isAccepted) {
                        sendMail(
                            email = globalService.getMember(it.belongTo).email,
                            text = Variables.MAIL_CONTENT_STORE_DELETE_ACCEPTED
                        )
                        it.updateForDelete()
                    } else sendMail(
                        email = globalService.getMember(it.belongTo).email,
                        text = Variables.MAIL_CONTENT_STORE_DELETE_REFUSED
                    )
                }

                else -> {}
            }
        }

    private fun sendMail(email: String, text: String) =
        javaMailSender.createMimeMessage().let {
            MimeMessageHelper(it, false).let { helper ->
                helper.setTo(email)
                helper.setSubject(Variables.MAIL_SUBJECT)
                helper.setText(text)
            }
            javaMailSender.send(it)
        }

    private fun unavailableToReservation(status: StoreStatus) =
        (status == StoreStatus.WAITING_FOR_CREATE) || (status == StoreStatus.WAITING_FOR_DELETE) || (status == StoreStatus.DELETED)
}