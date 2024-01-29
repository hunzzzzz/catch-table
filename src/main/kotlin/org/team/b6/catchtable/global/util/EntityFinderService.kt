package org.team.b6.catchtable.global.util

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.team.b6.catchtable.domain.blacklist.entity.BlackList
import org.team.b6.catchtable.domain.blacklist.repository.BlackListRepository
import org.team.b6.catchtable.domain.member.model.Member
import org.team.b6.catchtable.domain.member.repository.MemberRepository
import org.team.b6.catchtable.domain.reservation.model.Reservation
import org.team.b6.catchtable.domain.reservation.repository.ReservationRepository
import org.team.b6.catchtable.domain.review.model.Review
import org.team.b6.catchtable.domain.review.repository.ReviewRepository
import org.team.b6.catchtable.domain.store.model.Store
import org.team.b6.catchtable.domain.store.repository.StoreRepository
import org.team.b6.catchtable.global.exception.ModelNotFoundException

@Service
class EntityFinderService(
    private val memberRepository: MemberRepository,
    private val storeRepository: StoreRepository,
    private val reviewRepository: ReviewRepository,
    private val reservationRepository: ReservationRepository,
    private val blackListRepository: BlackListRepository
) {
    fun getAllMembers(): MutableList<Member> = memberRepository.findAll()

    fun getMember(memberId: Long) =
        memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("멤버")

    fun getAllStores(): MutableList<Store> = storeRepository.findAll()

    fun getStore(storeId: Long) =
        storeRepository.findByIdOrNull(storeId) ?: throw ModelNotFoundException("식당")

    fun getAllReviews(): MutableList<Review> = reviewRepository.findAll()

    fun getReview(reviewId: Long) =
        reviewRepository.findByIdOrNull(reviewId) ?: throw ModelNotFoundException("리뷰")

    fun getAllReservations(): MutableList<Reservation> = reservationRepository.findAll()

    fun getReservation(reservationId: Long) =
        reservationRepository.findByIdOrNull(reservationId) ?: throw ModelNotFoundException("예약")

    fun getAllBlackLists(): MutableList<BlackList> = blackListRepository.findAll()
}