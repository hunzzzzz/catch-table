package org.team.b6.catchtable.domain.review.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team.b6.catchtable.domain.member.model.Member
import org.team.b6.catchtable.domain.member.repository.MemberRepository
import org.team.b6.catchtable.domain.review.dto.request.CreateReviewRequest
import org.team.b6.catchtable.domain.review.dto.request.DeleteReviewRequest
import org.team.b6.catchtable.domain.review.dto.request.UpdateReviewRequest
import org.team.b6.catchtable.domain.review.dto.response.ReviewResponse
import org.team.b6.catchtable.domain.review.model.Review
import org.team.b6.catchtable.domain.review.repository.ReviewRepository
import org.team.b6.catchtable.domain.store.dto.request.UpdateStoreRequest
import org.team.b6.catchtable.domain.store.dto.response.StoreResponse
import org.team.b6.catchtable.domain.store.model.Store
import org.team.b6.catchtable.domain.store.model.StoreCategory
import org.team.b6.catchtable.domain.store.repository.StoreRepository

@Service
class ReviewServiceImpl(
    private val memberRepository: MemberRepository,
    private val storeRepository: StoreRepository,
    private val reviewRepository: ReviewRepository
) : ReviewService {
    override fun getReviewById(reviewId: Long): ReviewResponse {
        val review = reviewRepository.findByIdOrNull(reviewId)
            ?: throw Exception("...")
        return ReviewResponse.from(review)
    }
    override fun createReview(request: CreateReviewRequest): ReviewResponse {
        val review = reviewRepository.save(
            Review(
                content = request.content,
                ratings = request.ratings,
                member = getMember(request.memberId),
                store = getStore(request.storeId)
            )
        )
        return ReviewResponse.from(review)
    }

    private fun getMember(memberId: Long): Member {
        return memberRepository.findByIdOrNull(memberId) ?: throw Exception("")
    }

    private fun getStore(storeId: Long): Store {
        return storeRepository.findByIdOrNull(storeId) ?: throw Exception("")
    }
    override fun updateReview(reviewId: Long, request: UpdateReviewRequest): ReviewResponse {
        val review = reviewRepository.findByIdOrNull(reviewId) ?: throw Exception("")
        review.update(
            content = request.content,
            ratings = request.ratings
            )

        return ReviewResponse.from(review)
    }
    @Transactional
    override fun deleteReview(reviewId: Long) {
        reviewRepository.deleteById(reviewId)
    }
}