package org.team.b6.catchtable.domain.store.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team.b6.catchtable.domain.store.dto.request.CreateStoreRequest
import org.team.b6.catchtable.domain.store.dto.request.UpdateStoreRequest
import org.team.b6.catchtable.domain.store.dto.response.StoreResponse
import org.team.b6.catchtable.domain.store.repository.StoreRepository
import org.team.b6.catchtable.domain.store.model.Store
import org.team.b6.catchtable.domain.store.model.StoreCategory

@Service
@Transactional
class StoreServiceImpl(
    private val storeRepository: StoreRepository
) : StoreService {
    override fun getStoreById(storeId: Long): StoreResponse {
        val store = storeRepository.findByIdOrNull(storeId)
            ?: throw Exception("...")
        return StoreResponse.from(store)
    }
     override fun createStore(request: CreateStoreRequest): StoreResponse {
            val store = storeRepository.save(
                Store(
                    name = request.name,
                    category = StoreCategory.valueOf(request.category),
                    description = request.description,
                    phone = request.phone,
                    address = request.address
                )
            )
           return StoreResponse.from(store)
     }

    override fun updateStore(storeId: Long, request: UpdateStoreRequest): StoreResponse {

        val store = storeRepository.findByIdOrNull(storeId) ?: throw Exception("")
        store.update(
            name = request.name,
            category = StoreCategory.valueOf(request.category),
            description = request.description,
            phone = request.phone,
            address = request.address)

        return StoreResponse.from(store)
    }

    @Transactional
    override fun deleteStore(storeId: Long) {
        storeRepository.deleteById(storeId)
    }
}