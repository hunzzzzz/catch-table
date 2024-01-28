package org.team.b6.catchtable.domain.reservation.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team.b6.catchtable.domain.member.repository.MemberRepository
import org.team.b6.catchtable.domain.reservation.dto.ReservationRequest
import org.team.b6.catchtable.domain.reservation.dto.ReservationResponse
import org.team.b6.catchtable.domain.reservation.model.Reservation
import org.team.b6.catchtable.domain.reservation.model.ReservationStatus
import org.team.b6.catchtable.domain.reservation.model.checkTime
import org.team.b6.catchtable.domain.reservation.model.toResponse
import org.team.b6.catchtable.domain.reservation.repository.ReservationRepository
import org.team.b6.catchtable.domain.store.repository.StoreRepository
import org.team.b6.catchtable.global.exception.ModelNotFoundException
import org.team.b6.catchtable.global.security.MemberPrincipal

@Service
class ReservationServiceImpl(
    private val reservationRepository: ReservationRepository,
    private val memberRepository: MemberRepository,
    private val storeRepository: StoreRepository,
) : ReservationService {

    @Transactional
    override fun letReservation(
        storeId: Long,
        request: ReservationRequest,
        memberPrincipal: MemberPrincipal
    ): ReservationResponse {
        val member = memberRepository.findByIdOrNull(memberPrincipal.id) ?: throw ModelNotFoundException("modelName")
        val store = storeRepository.findByIdOrNull(storeId) ?: throw ModelNotFoundException("modelName")

//        checkTime(request.time, store.openTime, store.closeTime)
        
        return reservationRepository.save(
            Reservation(
                member = member,
                time = request.time,
                party = request.party,
                store = store,
                status = ReservationStatus.Pending,
//                deleted = "0",
            )
        ).toResponse()
    }

    override fun memberReservationList(memberPrincipal: MemberPrincipal): List<ReservationResponse> {
//        val member= memberRepository.findByIdOrNull(memberPrincipal.id) ?: throw  ModelNotFoundException("modelName")
        val reservations = reservationRepository.findAllReservationById(memberPrincipal.id)

        return reservations.map { it.toResponse() }
    }

    override fun storeReservationList(memberPrincipal: MemberPrincipal): List<ReservationResponse> {
        val store = storeRepository.findStoreByBelongTo(memberPrincipal.id)
        val reservations = reservationRepository.findAllReservationById(store.id!!)
//        val num= 비관적 잠금?

        return reservations.map { it.toResponse() }
    }

    override fun storeReservationListByTime(memberPrincipal: MemberPrincipal): String {
        val store = storeRepository.findStoreByBelongTo(memberPrincipal.id)
        val reservations= reservationRepository.findAllReservationById(store.id!!)

        // 시간대별로 그룹화하고 예약된 횟수 확인
        val timeCountMap = reservations.groupBy { it.time }
            .mapValues { (_, reservations) -> reservations.size }

        val timeList= StringBuilder()

// 영업 시간대의 예약 갯수 출력
        for (i in store.openTime until store.closeTime) {
            val count = timeCountMap[i] ?: 0
            timeList.append("$i 시: $count 팀")
        }
        return timeList.toString()
    }

    @Transactional
    override fun cancelReservation(reservationId: Long, memberPrincipal: MemberPrincipal): String {
        val member = memberRepository.findByIdOrNull(memberPrincipal.id) ?: throw ModelNotFoundException("modelName")
        val reservation =
            reservationRepository.findByIdOrNull(reservationId) ?: throw ModelNotFoundException("modelName")

        if (reservation.member == member && member.role.name == "USER") {
            reservation.isCancelled()
            reservationRepository.save(reservation)
            return ("예약을 취소했습니다.")
        } else {
            throw IllegalStateException("현재 로그인 정보와 예약자의 정보가 달라요.")
        }
    }

    @Transactional
    override fun confirmReservation(reservationId: Long, memberPrincipal: MemberPrincipal): String {
        val member = memberRepository.findByIdOrNull(memberPrincipal.id) ?: throw ModelNotFoundException("modelName")
        val reservation =
            reservationRepository.findByIdOrNull(reservationId) ?: throw ModelNotFoundException("modelName")
        val store = storeRepository.findStoreByBelongTo(memberPrincipal.id)

        if (reservation.store == store && member.role.name == "OWNER") {
            reservation.isConfirmed()
            reservationRepository.save(reservation)
            return ("매장이 예약을 접수했습니다.")
        } else {
            throw IllegalStateException("현재 로그인 정보와 매장 관리자의 정보가 일치하지 않아요.")
        }
    }

    @Transactional
    override fun rejectReservation(reservationId: Long, memberPrincipal: MemberPrincipal): String {
        val member = memberRepository.findByIdOrNull(memberPrincipal.id) ?: throw ModelNotFoundException("modelName")
        val reservation =
            reservationRepository.findByIdOrNull(reservationId) ?: throw ModelNotFoundException("modelName")
        val store = storeRepository.findStoreByBelongTo(memberPrincipal.id)

        if (reservation.store == store && member.role.name == "OWNER") {
            reservation.isRejected()
            reservationRepository.save(reservation)
            return ("매장이 예약을 받을 수 없는 상황으로 거절했습니다.")
        } else {
            throw IllegalStateException("현재 로그인 정보와 매장 관리자의 정보가 일치하지 않아요.")
        }
    }

}