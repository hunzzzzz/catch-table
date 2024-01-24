package org.team.b6.catchtable.domain.store.service

import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team.b6.catchtable.domain.store.dto.request.StoreRequest
import org.team.b6.catchtable.domain.store.dto.response.StoreResponse
import org.team.b6.catchtable.domain.store.model.Store
import org.team.b6.catchtable.domain.store.model.StoreCategory
import org.team.b6.catchtable.domain.store.repository.StoreRepository
import org.team.b6.catchtable.global.exception.InvalidStoreSearchingValuesException

@Service
@Transactional
class StoreService(
    private val storeRepository: StoreRepository
) {
    // 카테고리 별 식당 전체 조회
    fun findAllStoresByCategory(category: String) =
        storeRepository.findAllByCategory(
            category = getCategory(category)
        ).map { StoreResponse.from(it) }

    // 카테고리와 정렬 조건을 사용하여 식당 전체 조회
    fun findAllStoresByCategoryWithSortCriteria(category: String, direction: Sort.Direction, criteria: String) =
        storeRepository.findAllByCategory(
            category = getCategory(category),
            sort = Sort.by(direction, getCriteria(criteria))
        ).map { StoreResponse.from(it) }

    // 식당 단일 조회
    fun findStore(storeId: Long) =
        StoreResponse.from(getStore(storeId))

    // 식당 등록
    fun registerStore(requiredStore: Store) = storeRepository.save(requiredStore)

    // 식당 수정
    fun updateStore(storeId: Long, request: StoreRequest) = getStore(storeId).update(request)

    // 식당 제거
    fun deleteStore(storeId: Long) = storeRepository.deleteById(storeId)

    // 내부 메서드들
    private fun getCategory(category: String) =
        StoreCategory.entries.firstOrNull { it.name == category.uppercase() }
            ?: throw InvalidStoreSearchingValuesException("category")

    private fun getCriteria(criteria: String) =
        arrayListOf("name").firstOrNull { it == criteria } // TODO: 정렬 조건 추후 추가
            ?: throw InvalidStoreSearchingValuesException("criteria")

    private fun getStore(storeId: Long) =
        storeRepository.findByIdOrNull(storeId) ?: throw Exception("") // TODO: 추후 반영
}