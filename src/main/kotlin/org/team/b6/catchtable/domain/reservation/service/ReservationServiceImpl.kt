package org.team.b6.catchtable.domain.reservation.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team.b6.catchtable.domain.member.model.Member
import org.team.b6.catchtable.domain.member.repository.MemberRepository
import org.team.b6.catchtable.domain.reservation.dto.ReservationRequest
import org.team.b6.catchtable.domain.reservation.dto.ReservationResponse
import org.team.b6.catchtable.domain.reservation.model.Reservation
import org.team.b6.catchtable.domain.reservation.model.ReservationStatus
import org.team.b6.catchtable.domain.reservation.model.toResponse
import org.team.b6.catchtable.domain.reservation.repository.ReservationRepository
import org.team.b6.catchtable.domain.store.repository.StoreRepository
import org.team.b6.catchtable.global.exception.BannedUserException
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
        validateBannedExpiration(member)
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

        return reservations.map { it.toResponse() }
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

    private fun validateBannedExpiration(member: Member) {
        if (member.bannedExpiration != null)
            throw BannedUserException(member.bannedExpiration!!)
    }
}