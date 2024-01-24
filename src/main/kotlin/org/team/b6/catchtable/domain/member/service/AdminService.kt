package org.team.b6.catchtable.domain.member.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import org.team.b6.catchtable.domain.member.dto.response.AdminResponse
import org.team.b6.catchtable.domain.store.dto.request.StoreRequest
import org.team.b6.catchtable.domain.store.model.StoreRequirementCategory
import org.team.b6.catchtable.domain.store.repository.StoreRequirementRepository
import org.team.b6.catchtable.domain.store.service.StoreService
import org.team.b6.catchtable.global.variable.Strings

@Service
@Transactional
class AdminService(
    private val storeService: StoreService,
    private val storeRequirementRepository: StoreRequirementRepository,
    private val javaMailSender: JavaMailSender
) {
    fun findAllStoreRequirements() =
        storeRequirementRepository.findAll().filter { !it.isAccepted }.map { AdminResponse.from(it) }

    fun acceptStoreRequirement(storeRequirementId: Long) {
        getStoreRequirement(storeRequirementId)
            .let {
                when (it.requirement) {
                    StoreRequirementCategory.CREATE -> storeService.registerStore(it.store!!)

                    StoreRequirementCategory.UPDATE -> storeService.updateStore(
                        storeId = it.requireOf!!,
                        request = StoreRequest(
                            it.store!!.name,
                            it.store.category.name,
                            it.store.description,
                            it.store.phone,
                            it.store.address
                        )
                    )

                    StoreRequirementCategory.DELETE -> storeService.deleteStore(it.requireOf!!)
                }
            }.run {
                // TODO : 메일 전송 로직 추가
                deleteStoreRequirement(storeRequirementId)
            }
    }

    private fun deleteStoreRequirement(storeChangeId: Long) =
        storeRequirementRepository.deleteById(storeChangeId)

    private fun getStoreRequirement(storeChangeId: Long) =
        storeRequirementRepository.findByIdOrNull(storeChangeId) ?: throw Exception("") // TODO : 추후 구현

    private fun sendMail(email: String, requirement: StoreRequirementCategory, isAccepted: Boolean) {
        SimpleMailMessage().let {
            it.replyTo = email
            it.subject = Strings.MAIL_SUBJECT

            if (isAccepted) {
                when (requirement) {
                    StoreRequirementCategory.CREATE -> it.text = Strings.MAIL_CONTENT_STORE_CREATE_ACCEPTED
                    StoreRequirementCategory.UPDATE -> it.text = Strings.MAIL_CONTENT_STORE_UPDATE_ACCEPTED
                    StoreRequirementCategory.DELETE -> it.text = Strings.MAIL_CONTENT_STORE_DELETE_ACCEPTED
                }
            }

            javaMailSender.send(it)
        }
    }
}