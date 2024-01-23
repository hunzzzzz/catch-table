package org.team.b6.catchtable.domain.review.service

import org.team.b6.catchtable.domain.review.dto.request.CreateReviewRequest
import org.team.b6.catchtable.domain.review.dto.request.DeleteReviewRequest
import org.team.b6.catchtable.domain.review.dto.request.UpdateReviewRequest
import org.team.b6.catchtable.domain.review.dto.response.ReviewResponse

interface ReviewService {
    fun getReviewById(reviewId: Long): ReviewResponse
    fun createReview(request: CreateReviewRequest): ReviewResponse
    fun updateReview(reviewId: Long,request: UpdateReviewRequest): ReviewResponse
    fun deleteReview(reviewId: Long)
}