package org.team.b6.catchtable.domain.reservation.model

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.team.b6.catchtable.domain.member.model.Member
import org.team.b6.catchtable.domain.store.model.Store
import org.team.b6.catchtable.global.entity.BaseEntity
import java.time.LocalDateTime

@Entity
@Table(name = "Reservations")
class Reservation(
    @Column(name = "time", nullable = false)
    val time: LocalDateTime,

    @Column(name = "created_at", columnDefinition = "TIMESTAMP(6)", nullable = false, updatable = false)
    val createdAt: LocalDateTime,

    @ManyToOne
    @JoinColumn(name = "member_id")
    val member: Member,

    @ManyToOne
    @JoinColumn(name = "store_id")
    val store: Store
) {
    @Id
    @Column(name = "reservation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}