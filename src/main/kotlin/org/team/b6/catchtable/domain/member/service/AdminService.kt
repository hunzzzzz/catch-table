package org.team.b6.catchtable.domain.member.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team.b6.catchtable.domain.store.model.StoreStatus
import org.team.b6.catchtable.domain.store.repository.StoreRepository
import org.team.b6.catchtable.global.exception.ModelNotFoundException

@Service
@Transactional
class AdminService(
    private val storeRepository: StoreRepository,
    private val javaMailSender: JavaMailSender
) {
    fun findAllStoreRequirements() =
        storeRepository.findAll()
            .filter { unavailableToOrder(it.status) }

    fun accept(storeId: Long) {
        getStore(storeId).let {
            when (it.status) {
                StoreStatus.WAITING_FOR_CREATE -> it.updateStatus(StoreStatus.OK)
                StoreStatus.WAITING_FOR_DELETE -> it.updateForDelete()
                else -> ""
            }
        }
    }

    private fun getStore(storeId: Long) =
        (storeRepository.findByIdOrNull(storeId) ?: throw ModelNotFoundException("식당"))

//    private fun sendMail(email: String, requirement: StoreRequirementCategory, isAccepted: Boolean) {
//        SimpleMailMessage().let {
//            it.replyTo = email
//            it.subject = Variables.MAIL_SUBJECT
//
//            if (isAccepted) {
//                when (requirement) {
//                    StoreRequirementCategory.CREATE -> it.text = Variables.MAIL_CONTENT_STORE_CREATE_ACCEPTED
//                    StoreRequirementCategory.UPDATE -> it.text = Variables.MAIL_CONTENT_STORE_UPDATE_ACCEPTED
//                    StoreRequirementCategory.DELETE -> it.text = Variables.MAIL_CONTENT_STORE_DELETE_ACCEPTED
//                }
//            }
//
//            javaMailSender.send(it)
//        }
//    }

    private fun unavailableToOrder(status: StoreStatus) =
        (status == StoreStatus.WAITING_FOR_CREATE) || (status == StoreStatus.WAITING_FOR_DELETE)
}