package org.team.b6.catchtable.domain.store.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team.b6.catchtable.domain.reservation.repository.ReservationRepository
import org.team.b6.catchtable.domain.review.dto.response.ReviewResponse
import org.team.b6.catchtable.domain.store.dto.request.StoreRequest
import org.team.b6.catchtable.domain.store.dto.response.StoreResponse
import org.team.b6.catchtable.domain.store.model.Store
import org.team.b6.catchtable.domain.store.model.StoreCategory
import org.team.b6.catchtable.domain.store.model.StoreStatus
import org.team.b6.catchtable.domain.store.repository.StoreRepository
import org.team.b6.catchtable.global.exception.*
import org.team.b6.catchtable.global.security.MemberPrincipal
import org.team.b6.catchtable.global.util.EntityFinder

@Service
@Transactional
class StoreService(
    private val storeRepository: StoreRepository,
    private val reservationRepository: ReservationRepository,
    private val entityFinder: EntityFinder
) {
    // 카테고리 별 식당 전체 조회
    fun findAllStoresByCategory(category: String) =
        storeRepository.findAllByCategory(getCategory(category))
            .filter { availableToReservation(it.status) }
            .map {
                StoreResponse.from(
                    store = it,
                    member = entityFinder.getMember(it.belongTo),
                    reviews = entityFinder.getAllReviews()
                        .filter { review -> review.store.id == it.id }
                        .map { review -> ReviewResponse.from(review) }
                )
            }

    // 카테고리와 정렬 조건을 사용하여 식당 전체 조회
    fun findAllStoresByCategoryWithSortCriteria(category: String, criteria: String) =
        when (criteria) {
            // TODO: 추후 정렬 조건 추가
            "reservation" -> sortByNumberOfReservations(category)
            else -> throw InvalidStoreSearchingValuesException("criteria")
        }

    // 식당 단일 조회
    fun findStore(storeId: Long) =
        entityFinder.getStore(storeId).let {
            StoreResponse.from(
                store = it,
                member = entityFinder.getMember(it.belongTo),
                reviews = entityFinder.getAllReviews()
                    .filter { review -> review.store.id == it.id }
                    .map { review -> ReviewResponse.from(review) }
            )
        }

    // 식당 등록 신청
    fun registerStore(memberPrincipal: MemberPrincipal, request: StoreRequest): Long {
        validateDuplication(request.name, request.address)
        return storeRepository.save(request.to(memberPrincipal.id)).id!!
    }

    // 식당 정보 수정
    fun updateStore(storeId: Long, request: StoreRequest) {
        validateDuplicationInUpdate(request.name, request.address, storeId)
        entityFinder.getStore(storeId)
            .let {
                validateStoreStatus(it)
                it.update(request)
            }
    }

    // 식당 삭제 신청
    fun deleteStore(storeId: Long) =
        entityFinder.getStore(storeId).updateStatus(StoreStatus.WAITING_FOR_DELETE)

    // 카테고리에 대한 유효성 검사
    private fun getCategory(category: String) =
        StoreCategory.entries.firstOrNull { it.name == category.uppercase() }
            ?: throw InvalidStoreSearchingValuesException("category")

    // '예약 많은 수'로 정렬
    private fun sortByNumberOfReservations(category: String) =
        storeRepository.findAllByCategory(getCategory(category))
            .filter { availableToReservation(it.status) }
            .sortedByDescending {
                reservationRepository.findAll().count { reservation -> reservation.store.id == it.id }
            }
            .map {
                StoreResponse.from(
                    store = it,
                    member = entityFinder.getMember(it.belongTo),
                    reviews = entityFinder.getAllReviews()
                        .filter { review -> review.store.id == it.id }
                        .map { review -> ReviewResponse.from(review) }
                )
            }

    // 식당 등록 시, 상호명과 식당 주소 중복 여부 검사
    private fun validateDuplication(name: String, address: String) {
        if (storeRepository.existsByName(name))
            throw DuplicatedValueException("상호명")
        else if (storeRepository.existsByAddress(address))
            throw DuplicatedValueException("식당 주소")
    }

    // 식당 등록 시, 상호명과 식당 주소 중복 여부 검사 + 본인이 기존에 사용하던 상호명과 식당 주소는 검증 대상에서 제외
    private fun validateDuplicationInUpdate(name: String, address: String, storeId: Long) {
        if (storeRepository.existsByName(name) && (storeRepository.findByName(name)!!.id != storeId))
            throw DuplicatedValueException("상호명")
        else if (storeRepository.existsByAddress(address) && (storeRepository.findByAddress(address)!!.id != storeId))
            throw DuplicatedValueException("식당 주소")
    }

    // 식당이 예약 가능한 상태인지 확인 (단순 Exception Throw)
    private fun validateStoreStatus(store: Store) {
        if (!availableToReservation(store.status)) throw StoreRequirementDeniedException("update")
    }

    // 식당이 예약 가능한 상태인지 확인 (boolean 값 리턴)
    private fun availableToReservation(status: StoreStatus) = (status == StoreStatus.OK)
}