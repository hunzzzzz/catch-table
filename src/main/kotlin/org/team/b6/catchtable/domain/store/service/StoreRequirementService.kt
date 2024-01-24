package org.team.b6.catchtable.domain.store.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.team.b6.catchtable.domain.store.dto.request.StoreRequest
import org.team.b6.catchtable.domain.store.model.StoreRequirement
import org.team.b6.catchtable.domain.store.model.StoreRequirementCategory
import org.team.b6.catchtable.domain.store.repository.StoreRequirementRepository
import org.team.b6.catchtable.domain.store.repository.StoreRepository
import java.time.LocalDateTime

@Service
@Transactional
class StoreRequirementService(
    private val storeRepository: StoreRepository,
    private val storeRequirementRepository: StoreRequirementRepository
) {
    fun applyForRegister(storeRequest: StoreRequest) {
        storeRequirementRepository.save(
            storeRequest.to(
                requirement = StoreRequirementCategory.CREATE,
                store = storeRequest.to()
            )
        )
    }

    fun applyForUpdate(storeId: Long, storeRequest: StoreRequest) {
        storeRequirementRepository.save(
            storeRequest.to(
                requirement = StoreRequirementCategory.UPDATE,
                store = getStore(storeId)
            )
        )
    }

    fun applyForDelete(storeId: Long) {
        storeRequirementRepository.save(
            StoreRequirement(
                requirement = StoreRequirementCategory.DELETE,
                store = getStore(storeId),
                createdAt = LocalDateTime.now()
            )
        )
    }

    private fun getStore(storeId: Long) =
        storeRepository.findByIdOrNull(storeId) ?: throw Exception("")
}