package org.team.b6.catchtable.domain.store.service

import org.team.b6.catchtable.domain.store.dto.request.CreateStoreRequest
import org.team.b6.catchtable.domain.store.dto.request.DeleteStoreRequest
import org.team.b6.catchtable.domain.store.dto.request.UpdateStoreRequest
import org.team.b6.catchtable.domain.store.dto.response.StoreResponse

interface StoreService {

    fun getStoreById(storeId: Long): StoreResponse
    fun createStore(request: CreateStoreRequest): StoreResponse
    fun updateStore(storeId: Long, request: UpdateStoreRequest): StoreResponse
    fun deleteStore(storeId: Long)
}