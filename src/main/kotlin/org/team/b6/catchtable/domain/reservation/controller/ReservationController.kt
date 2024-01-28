package org.team.b6.catchtable.domain.reservation.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.team.b6.catchtable.domain.reservation.dto.ReservationRequest
import org.team.b6.catchtable.domain.reservation.dto.ReservationResponse
import org.team.b6.catchtable.domain.reservation.service.ReservationService
import org.team.b6.catchtable.global.security.MemberPrincipal

@RestController
//@RequestMapping("/reservations")
class ReservationController(
    private val reservationService: ReservationService,
) {
    @PostMapping("/stores/{storeId}/reservations")
    @PreAuthorize("hasRole('USER')")
    fun letReservation(
        @PathVariable storeId: Long,
        @RequestBody reservationRequest: ReservationRequest,
        @AuthenticationPrincipal memberPrincipal: MemberPrincipal,
    ): ResponseEntity<ReservationResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(reservationService.letReservation(storeId, reservationRequest, memberPrincipal))
    }

    @GetMapping("/members/myReservations")
    @PreAuthorize("hasRole('USER')")
    fun memberReservationList(
        @AuthenticationPrincipal memberPrincipal: MemberPrincipal
    ): ResponseEntity<List<ReservationResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(reservationService.memberReservationList(memberPrincipal))
    }

    @GetMapping("/members/storeReservations")
    @PreAuthorize("hasRole('OWNER')")
    fun storeReservationList(
        @AuthenticationPrincipal memberPrincipal: MemberPrincipal
    ): ResponseEntity<List<ReservationResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(reservationService.storeReservationList(memberPrincipal))
    }

    @GetMapping("/members/storeReservations/timeList")
    @PreAuthorize("hasRole('OWNER')")
    fun storeReservationListByTime(
        @AuthenticationPrincipal memberPrincipal: MemberPrincipal,
        @RequestBody reservationRequest: ReservationRequest
    ): ResponseEntity<String>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(reservationService.storeReservationListByTime(memberPrincipal, reservationRequest))
    }


    @PutMapping("/members/myReservations/{reservationId}/cancel")
    @PreAuthorize("hasRole('USER')")
    fun cancelReservation(
        @PathVariable reservationId: Long,
        @AuthenticationPrincipal memberPrincipal: MemberPrincipal
    ): ResponseEntity<String> {
        return ResponseEntity
            .status((HttpStatus.OK))
            .body(reservationService.cancelReservation(reservationId, memberPrincipal))
    }

    @PutMapping("/members/storeReservations/{reservationId}/confirm")
    @PreAuthorize("hasRole('OWNER')")
    fun confirmReservation(
        @PathVariable reservationId: Long,
        @AuthenticationPrincipal memberPrincipal: MemberPrincipal
    ): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(reservationService.confirmReservation(reservationId, memberPrincipal))
    }

    @PutMapping("/members/storeReservations/{reservationId}/reject")
    @PreAuthorize("hasRole('OWNER')")
    fun rejectReservation(
        @PathVariable reservationId: Long,
        @AuthenticationPrincipal memberPrincipal: MemberPrincipal
    ): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(reservationService.rejectReservation(reservationId, memberPrincipal))
    }

}