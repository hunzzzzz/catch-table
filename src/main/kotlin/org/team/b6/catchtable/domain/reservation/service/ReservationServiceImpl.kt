package org.team.b6.catchtable.domain.reservation.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team.b6.catchtable.domain.member.model.Member
import org.team.b6.catchtable.domain.member.model.MemberRole
import org.team.b6.catchtable.domain.member.repository.MemberRepository
import org.team.b6.catchtable.domain.reservation.dto.request.ReservationRequest
import org.team.b6.catchtable.domain.reservation.dto.response.ReservationResponse
import org.team.b6.catchtable.domain.reservation.model.Reservation
import org.team.b6.catchtable.domain.reservation.model.ReservationStatus
import org.team.b6.catchtable.domain.reservation.model.checkTime
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

        val reservation = Reservation(
            member = member,
            time = request.time,
            party = request.party,
            store = store,
            status = ReservationStatus.Pending,
            date = request.date,
        )
        if (reservation.checkDate(request.date)) throw IllegalArgumentException("당일 예약은 불가능해요.")
        if (!checkTime(
                request.time,
                store.openTime,
                store.closeTime
            )
        ) throw IllegalArgumentException("해당 매장의 영업 시간 내에서 선택해주세요.")
        validateBannedExpiration(member)

        return reservationRepository.save(reservation).toResponse()
//        return reservationRepository.save(
//            Reservation(
//                member = member,
//                time = request.time,
//                party = request.party,
//                store = store,
//                status = ReservationStatus.Pending,
////                deleted = "0",
//            )
//        ).toResponse()
    }

    override fun memberReservationList(memberPrincipal: MemberPrincipal): List<ReservationResponse> {
//        val member= memberRepository.findByIdOrNull(memberPrincipal.id) ?: throw  ModelNotFoundException("modelName")
        val reservations = reservationRepository.findAllReservationById(memberPrincipal.id)

        return reservations.map { it.toResponse() }
    }

    override fun storeReservationList(memberPrincipal: MemberPrincipal): List<ReservationResponse> {
        val member = memberRepository.findByIdOrNull(memberPrincipal.id) ?: throw ModelNotFoundException("modelName")
        if (member.role != MemberRole.OWNER) throw IllegalStateException("매장 주인만 이용할 수 있는 기능이에요")
        val store = storeRepository.findStoreByBelongTo(memberPrincipal.id) ?: throw ModelNotFoundException("modelName")
        val reservations = reservationRepository.findAllReservationById(store.id!!)
//            .filter { it.status == ReservationStatus.Pending } 예약 승인용으로 쓰려 했으나 취소
//        val num= 비관적 잠금?

        return reservations.map { it.toResponse() }
    }

    //예약이 승인이 난 것들만 모아서 request에 입력한 날짜에 따라 시간별로 모아서 출력
    override fun storeReservationListByTime(memberPrincipal: MemberPrincipal, request: ReservationRequest): String {
        val store = storeRepository.findStoreByBelongTo(memberPrincipal.id) ?: throw ModelNotFoundException("modelName")
        val reservations = reservationRepository.findAllReservationByStoreAndDate(store, request.date)
            .filter { it.status == ReservationStatus.Confirmed }

        // 시간대별로 그룹화하고 예약 수 확인
        val timeMap = reservations.groupBy { it.time }
            .mapValues { (_, reservations) -> reservations.size }

        val timeList = StringBuilder()

        // 영업 시간대의 예약 갯수 출력
        for (i in store.openTime until store.closeTime) {
            val count = timeMap[i] ?: 0
            timeList.append("${i}시: $count 팀\n")
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

    private fun validateBannedExpiration(member: Member) {
        if (member.bannedExpiration != null)
            throw BannedUserException(member.bannedExpiration!!)
    }
}