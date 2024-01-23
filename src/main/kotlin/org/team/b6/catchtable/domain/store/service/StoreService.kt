package org.team.b6.catchtable.domain.store.service

import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team.b6.catchtable.domain.store.dto.request.StoreRequest
import org.team.b6.catchtable.domain.store.dto.response.StoreResponse
import org.team.b6.catchtable.domain.store.model.StoreCategory
import org.team.b6.catchtable.domain.store.repository.StoreRepository
import org.team.b6.catchtable.global.exception.InvalidStoreSearchingValuesException

@Service
@Transactional
class StoreService(
    private val storeRepository: StoreRepository
) {
    fun findAllStoresByCategory(category: String) =
        storeRepository.findAllByCategory(
            category = getCategory(category)
        ).map { StoreResponse.from(it) }

    fun findAllStoresByCategoryWithSortCriteria(category: String, direction: Sort.Direction, criteria: String) =
        storeRepository.findAllByCategory(
            category = getCategory(category),
            sort = Sort.by(direction, getCriteria(criteria))
        ).map { StoreResponse.from(it) }

    private fun getCategory(category: String) =
        StoreCategory.entries.firstOrNull { it.name == category.uppercase() }
            ?: throw InvalidStoreSearchingValuesException("category")

    private fun getCriteria(criteria: String) =
        arrayListOf("name").firstOrNull { it == criteria } // TODO: 정렬 조건 추후 추가
            ?: throw InvalidStoreSearchingValuesException("criteria")
}