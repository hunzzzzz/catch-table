package org.team.b6.catchtable.global.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.team.b6.catchtable.domain.member.repository.MemberRepository
import org.team.b6.catchtable.domain.review.repository.ReviewRepository
import org.team.b6.catchtable.domain.store.repository.StoreRepository
import org.team.b6.catchtable.global.exception.ModelNotFoundException

@Service
class GlobalService(
    private val memberRepository: MemberRepository,
    private val storeRepository: StoreRepository,
    private val reviewRepository: ReviewRepository
) {
    fun getAllMembers() = memberRepository.findAll()

    fun getMember(memberId: Long) =
        memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("멤버")

    fun getAllStores() = storeRepository.findAll()

    fun getStore(storeId: Long) =
        storeRepository.findByIdOrNull(storeId) ?: throw ModelNotFoundException("식당")

    fun getAllReviews() = memberRepository.findAll()

    fun getReview(reviewId: Long) =
        reviewRepository.findByIdOrNull(reviewId) ?: throw ModelNotFoundException("리뷰")
}