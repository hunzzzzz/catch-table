package org.team.b6.catchtable.domain.reservation.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.team.b6.catchtable.domain.member.model.Member
import org.team.b6.catchtable.domain.reservation.model.Reservation
import org.team.b6.catchtable.domain.reservation.model.ReservationStatus
import org.team.b6.catchtable.domain.store.model.Store
import java.time.LocalDate

@Repository
interface ReservationRepository : JpaRepository<Reservation, Long> {

    fun findAllByStore(store: Store): List<Reservation>

    fun findAllByMember(member: Member): List<Reservation>

    fun findAllReservationByStoreAndDate(store: Store, date: LocalDate): List<Reservation>

    fun countByMemberAndDateBetweenAndStatus(
        member: Member,
        nowTime: LocalDate,
        monthAgo: LocalDate,
        reservationStatus: ReservationStatus
    ): Int

}