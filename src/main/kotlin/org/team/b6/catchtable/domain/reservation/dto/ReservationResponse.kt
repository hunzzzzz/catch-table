package org.team.b6.catchtable.domain.reservation.dto

import java.time.LocalDateTime

data class ReservationResponse(
    val id: Long,
    val memberName: String,
    val store: String,
    val time: LocalDateTime,
    val party: String,
    val createdAt: LocalDateTime,
    val status: String,
)