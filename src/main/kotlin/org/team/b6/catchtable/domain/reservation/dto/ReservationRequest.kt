package org.team.b6.catchtable.domain.reservation.dto

import java.time.LocalDateTime

data class ReservationRequest(
    val time: LocalDateTime,
    val party: String,
)
