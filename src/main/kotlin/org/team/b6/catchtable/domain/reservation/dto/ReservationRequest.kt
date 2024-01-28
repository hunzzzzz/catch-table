package org.team.b6.catchtable.domain.reservation.dto

import java.time.LocalDate

data class ReservationRequest(
    val time: Int,
    val party: String,
    val date: LocalDate,
)
