package org.team.b6.catchtable.domain.reservation.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.team.b6.catchtable.domain.reservation.model.Reservation

@Repository
interface ReservationRepository : JpaRepository<Reservation, Long> {

    fun findAllReservationById(memberId: Long): List<Reservation>


}