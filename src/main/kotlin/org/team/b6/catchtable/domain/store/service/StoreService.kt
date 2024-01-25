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
import org.team.b6.catchtable.global.exception.DuplicatedValueException
import org.team.b6.catchtable.global.exception.InvalidStoreSearchingValuesException
import org.team.b6.catchtable.global.exception.ModelNotFoundException
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
        validateNameAndAddress(request.name, request.address)
            .run { storeRepository.save(request.to()).id!! }

    // 식당 수정
    fun updateStore(storeId: Long, request: StoreRequest) =
        validateNameAndAddress(request.name, request.address, storeId)
            .run { getStore(storeId).update(request) }

    // 식당 제거
    fun deleteStore(storeId: Long) =
        getStore(storeId).updateStatus(StoreStatus.WAITING_FOR_DELETE)

    // 내부 메서드들
    private fun getCategory(category: String) =
        StoreCategory.entries.firstOrNull { it.name == category.uppercase() }
            ?: throw InvalidStoreSearchingValuesException("category")

    private fun getCriteria(criteria: String) =
        Variables.CRITERIA_LIST.firstOrNull { it == criteria }
            ?: throw InvalidStoreSearchingValuesException("criteria")

    private fun getStore(storeId: Long) =
        (storeRepository.findByIdOrNull(storeId) ?: throw ModelNotFoundException("식당"))
            .let {
                if (!availableToReservation(it.status))
                    throw Exception("") // TODO : Exception 이름 미정
                else it
            }

    private fun validateNameAndAddress(name: String, address: String) {
        if (storeRepository.existsByName(name))
            throw DuplicatedValueException("상호명")
        else if (storeRepository.existsByAddress(address))
            throw DuplicatedValueException("식당 주소")
    }

    private fun validateNameAndAddress(name: String, address: String, storeId: Long) {
        if (storeRepository.existsByName(name) && (storeRepository.findByName(name)!!.id != storeId))
            throw DuplicatedValueException("상호명")
        else if (storeRepository.existsByAddress(address) && (storeRepository.findByAddress(address)!!.id != storeId))
            throw DuplicatedValueException("식당 주소")
    }

    private fun availableToReservation(status: StoreStatus) = (status == StoreStatus.OK)
}