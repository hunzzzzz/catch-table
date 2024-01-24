package org.team.b6.catchtable.domain.store.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team.b6.catchtable.domain.store.dto.request.StoreRequest
import org.team.b6.catchtable.domain.store.model.StoreRequirement
import org.team.b6.catchtable.domain.store.model.StoreRequirementCategory
import org.team.b6.catchtable.domain.store.repository.StoreRepository
import org.team.b6.catchtable.domain.store.repository.StoreRequirementRepository
import java.time.LocalDateTime

@Service
@Transactional
class StoreRequirementService(
    private val storeRequirementRepository: StoreRequirementRepository,
    private val storeRepository: StoreRepository,
) {
    fun applyForRegister(storeRequest: StoreRequest) =
        storeRequirementRepository.save(
            storeRequest.to(
                requirement = StoreRequirementCategory.CREATE,
                store = storeRequest.to()
            )
        )

    fun applyForUpdate(storeId: Long, storeRequest: StoreRequest) =
        validateStoreId(storeId).run {
            storeRequirementRepository.save(
                storeRequest.to(
                    requirement = StoreRequirementCategory.UPDATE,
                    store = storeRequest.to(),
                    requireOf = storeId
                )
            )
        }

    fun applyForDelete(storeId: Long) {
        validateStoreId(storeId).run {
            storeRequirementRepository.save(
                StoreRequirement(
                    requirement = StoreRequirementCategory.DELETE,
                    requireOf = storeId,
                    createdAt = LocalDateTime.now()
                )
            )
        }
    }

    private fun validateStoreId(storeId: Long) {
        if (!storeRepository.existsById(storeId)) throw Exception("") // TODO
    }
}