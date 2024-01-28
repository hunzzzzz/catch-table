package org.team.b6.catchtable.domain.reservation.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.team.b6.catchtable.domain.reservation.model.Reservation
import org.team.b6.catchtable.domain.store.model.Store

@Repository
interface ReservationRepository : JpaRepository<Reservation, Long> {

    fun findAllReservationById(memberId: Long): List<Reservation>

    fun findAllReservationByStoreAndTime(store: Store, time: Int): List<Reservation>


}