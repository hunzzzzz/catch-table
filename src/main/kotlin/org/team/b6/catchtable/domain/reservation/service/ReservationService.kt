package org.team.b6.catchtable.domain.reservation.service

import org.team.b6.catchtable.domain.reservation.dto.ReservationRequest
import org.team.b6.catchtable.domain.reservation.dto.ReservationResponse
import org.team.b6.catchtable.global.security.MemberPrincipal

interface ReservationService {

    fun letReservation(
        storeId: Long,
        request: ReservationRequest,
        memberPrincipal: MemberPrincipal
    ): ReservationResponse

    fun memberReservationList(memberPrincipal: MemberPrincipal): List<ReservationResponse>

    fun storeReservationList(memberPrincipal: MemberPrincipal): List<ReservationResponse>

    fun storeReservationListByTime(memberPrincipal: MemberPrincipal, request: ReservationRequest): String

    fun cancelReservation(reservationId: Long, memberPrincipal: MemberPrincipal): String

    fun confirmReservation(reservationId: Long, memberPrincipal: MemberPrincipal): String

    fun rejectReservation(reservationId: Long, memberPrincipal: MemberPrincipal): String


}