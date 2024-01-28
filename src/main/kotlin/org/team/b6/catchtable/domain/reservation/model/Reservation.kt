package org.team.b6.catchtable.domain.reservation.model

import jakarta.persistence.*
import org.team.b6.catchtable.domain.member.model.Member
import org.team.b6.catchtable.domain.reservation.dto.ReservationResponse
import org.team.b6.catchtable.domain.store.model.Store
import java.time.LocalDateTime

@Entity
@Table(name = "Reservations")
class Reservation(
    @Column(name = "time", nullable = false)
    val time: Int,

    @Column(name = "created_at", columnDefinition = "TIMESTAMP(6)", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "party", nullable = false)
    val party: String,

    @ManyToOne
    @JoinColumn(name = "member_id")
    val member: Member,

    @ManyToOne
    @JoinColumn(name = "store_id")
    val store: Store,

    @Column(name = "status", nullable = false)
    var status: ReservationStatus,

//    @Column(name = "deleted", nullable = false)
//    var deleted: String,
) {
    @Id
    @Column(name = "reservation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null


    fun isConfirmed() {
        status = ReservationStatus.Confirmed
    }

    fun isRejected() {
        status = ReservationStatus.Rejected
    }

    fun isCancelled() {
        status = ReservationStatus.Cancelled
    }
}

fun Reservation.toResponse(): ReservationResponse {
    return ReservationResponse(
        id = id!!,
        memberName = member.name,
        store = store.name,
        time = time,
        createdAt = createdAt,
        party = party,
        status = status.name,

        )
}

fun checkTime(time: Int, open: Int, close: Int){
    if (time !in open until close) throw IllegalArgumentException("영업시간이 아닙니다.")
}