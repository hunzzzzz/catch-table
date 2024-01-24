package org.team.b6.catchtable.domain.member.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.team.b6.catchtable.domain.member.dto.response.AdminResponse
import org.team.b6.catchtable.domain.store.dto.request.StoreRequest
import org.team.b6.catchtable.domain.store.model.StoreRequirementCategory
import org.team.b6.catchtable.domain.store.repository.StoreRequirementRepository
import org.team.b6.catchtable.domain.store.service.StoreService

@Service
@Transactional
class AdminService(
    private val storeService: StoreService,
    private val storeRequirementRepository: StoreRequirementRepository
) {
    fun findAllStoreRequirements() =
        storeRequirementRepository.findAll().filter { !it.isAccepted }.map { AdminResponse.from(it) }

    fun acceptStoreRequirement(storeRequirementId: Long) {
        getStoreRequirement(storeRequirementId)
            .let {
                when (it.requirement) {
                    StoreRequirementCategory.CREATE -> storeService.registerStore(it.store!!)

                    StoreRequirementCategory.UPDATE -> storeService.updateStore(
                        storeId = it.requireTo!!,
                        request = StoreRequest(
                            it.store!!.name,
                            it.store.category.name,
                            it.store.description,
                            it.store.phone,
                            it.store.address
                        )
                    )

                    StoreRequirementCategory.DELETE -> storeService.deleteStore(it.requireTo!!)
                }
            }.run { deleteStoreRequirement(storeRequirementId) }
    }

    private fun deleteStoreRequirement(storeChangeId: Long) =
        storeRequirementRepository.deleteById(storeChangeId)

    private fun getStoreRequirement(storeChangeId: Long) =
        storeRequirementRepository.findByIdOrNull(storeChangeId) ?: throw Exception("") // TODO : 추후 구현
}