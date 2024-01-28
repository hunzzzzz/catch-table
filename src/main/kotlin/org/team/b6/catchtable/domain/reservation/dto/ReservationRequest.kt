package org.team.b6.catchtable.domain.reservation.dto

import java.time.LocalDate
import java.time.LocalDateTime

data class ReservationRequest(
    val time: Int,
    val party: String,
    val date: LocalDate,
)
