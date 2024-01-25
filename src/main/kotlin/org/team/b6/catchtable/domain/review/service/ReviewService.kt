package org.team.b6.catchtable.domain.review.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team.b6.catchtable.domain.member.repository.MemberRepository
import org.team.b6.catchtable.domain.review.dto.request.ReviewRequest
import org.team.b6.catchtable.domain.review.dto.response.ReviewResponse
import org.team.b6.catchtable.domain.review.repository.ReviewRepository
import org.team.b6.catchtable.domain.store.model.Store
import org.team.b6.catchtable.domain.store.repository.StoreRepository
import org.team.b6.catchtable.global.exception.ModelNotFoundException

@Service
@Transactional
class ReviewService(
    private val memberRepository: MemberRepository,
    private val storeRepository: StoreRepository,
    private val reviewRepository: ReviewRepository
) {
    fun findReviews(storeId: Long) =
        reviewRepository.findAll().filter { it.store.id == storeId }

    fun findReview(storeId: Long, reviewId: Long) =
        ReviewResponse.from(getReview(reviewId))

    fun addReview(storeId: Long, request: ReviewRequest) =
        reviewRepository.save(
            request.to(
                member = getMember(request.memberId),
                store = getStore(storeId)
            )
        ).id

    fun updateReview(storeId: Long, reviewId: Long, request: ReviewRequest) =
        getReview(reviewId).update(request)

    fun deleteReview(storeId: Long, reviewId: Long) =
        reviewRepository.deleteById(reviewId)

    private fun validate(memberId: Long, storeId: Long, reviewId: Long) =
        getReview(reviewId).let {
            if (it.member.id != memberId) throw Exception("")
            else if (it.store.id != storeId) throw Exception("")
        } // TODO: 추후 memberId를 받는 로직이 추가되면 validate 구현

    private fun validateAndGetReview(memberId: Long, storeId: Long, reviewId: Long) =
        getReview(reviewId).let {
            if (it.member.id != memberId) throw Exception("")
            else if (it.store.id != storeId) throw Exception("")
            it
        } // TODO: 추후 memberId를 받는 로직이 추가되면 validate 구현

    private fun getReview(reviewId: Long) =
        reviewRepository.findByIdOrNull(reviewId) ?: throw ModelNotFoundException("리뷰")

    private fun getMember(memberId: Long) =
        memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("멤버")

    private fun getStore(storeId: Long): Store =
        storeRepository.findByIdOrNull(storeId) ?: throw ModelNotFoundException("식당")
}