package org.team.b6.catchtable.domain.reservation.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.team.b6.catchtable.domain.reservation.model.Reservation
import org.team.b6.catchtable.domain.store.model.Store
import java.time.LocalDate

@Repository
interface ReservationRepository : JpaRepository<Reservation, Long> {

    fun findAllReservationById(memberId: Long): List<Reservation>

    fun findAllReservationByStoreAndDate(store: Store, date: LocalDate): List<Reservation>


}