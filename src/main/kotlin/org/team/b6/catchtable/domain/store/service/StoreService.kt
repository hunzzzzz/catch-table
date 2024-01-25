package org.team.b6.catchtable.domain.store.service

import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team.b6.catchtable.domain.store.dto.request.StoreRequest
import org.team.b6.catchtable.domain.store.dto.response.StoreResponse
import org.team.b6.catchtable.domain.store.model.StoreCategory
import org.team.b6.catchtable.domain.store.model.StoreStatus
import org.team.b6.catchtable.domain.store.repository.StoreRepository
import org.team.b6.catchtable.global.exception.InvalidStoreSearchingValuesException
import org.team.b6.catchtable.global.variable.Variables

@Service
@Transactional
class StoreService(
    private val storeRepository: StoreRepository
) {
    // 카테고리 별 식당 전체 조회
    fun findAllStoresByCategory(category: String) =
        storeRepository.findAllByCategory(
            category = getCategory(category)
        ).filter { availableToReservation(it.status) }
            .map { StoreResponse.from(it) }

    // 카테고리와 정렬 조건을 사용하여 식당 전체 조회
    fun findAllStoresByCategoryWithSortCriteria(category: String, direction: Sort.Direction, criteria: String) =
        storeRepository.findAllByCategory(
            category = getCategory(category),
            sort = Sort.by(direction, getCriteria(criteria))
        ).filter { availableToReservation(it.status) }
            .map { StoreResponse.from(it) }

    // 식당 단일 조회
    fun findStore(storeId: Long) = StoreResponse.from(getStore(storeId))

    // 식당 등록
    fun registerStore(request: StoreRequest) =
        validateName(request.name)
        .run { validateAddress(request.address)
               storeRepository.save(request.to()).id!!
        }

    // 식당 수정
    fun updateStore(storeId: Long, request: StoreRequest) {
        validateName(request.name)
        validateAddress(request.address)
        return getStore(storeId).update(request)
    }

    fun registerStore(requiredStore: Store) = storeRepository.save(requiredStore)

    // 식당 제거
    fun deleteStore(storeId: Long) = getStore(storeId).updateStatus(StoreStatus.WAITING_FOR_DELETE)

    // 내부 메서드들
    private fun getCategory(category: String) =
        StoreCategory.entries.firstOrNull { it.name == category.uppercase() }
            ?: throw InvalidStoreSearchingValuesException("category")

    private fun getCriteria(criteria: String) =
        Variables.CRITERIA_LIST.firstOrNull { it == criteria }
            ?: throw InvalidStoreSearchingValuesException("criteria")

    private fun getStore(storeId: Long) =
        (storeRepository.findByIdOrNull(storeId) ?: throw Exception("")) // TODO : ModelNotFoundException
            .let {
                if (!availableToReservation(it.status))
                    throw Exception("") // TODO : Exception 이름 미정 (
                else it
            }

    private fun validateName(name: String) {
        if (storeRepository.existByName(name)) throw Exception(message = "이미 존재 하는 이름 입니다.")
    }

    private fun validateAddress(address: String) {
        if (storeRepository.existByAddress(address)) throw Exception(message = "이미 존재 하는 주소 입니다.")
    }

    private fun availableToReservation(status: StoreStatus) = (status == StoreStatus.OK)
}