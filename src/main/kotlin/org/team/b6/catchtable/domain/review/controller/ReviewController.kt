package org.team.b6.catchtable.domain.review.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.team.b6.catchtable.domain.review.dto.request.CreateReviewRequest
import org.team.b6.catchtable.domain.review.dto.request.DeleteReviewRequest
import org.team.b6.catchtable.domain.review.dto.request.UpdateReviewRequest
import org.team.b6.catchtable.domain.review.dto.response.ReviewResponse
import org.team.b6.catchtable.domain.review.service.ReviewServiceImpl
import org.team.b6.catchtable.domain.store.dto.request.UpdateStoreRequest
import org.team.b6.catchtable.domain.store.dto.response.StoreResponse

@RequestMapping("/api/reviews")
@RestController
class ReviewController(
    private val reviewServiceImpl: ReviewServiceImpl
) {

    @GetMapping("/{reviewId}")
    fun getReview(@PathVariable reviewId: Long): ResponseEntity<ReviewResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(reviewServiceImpl.getReviewById(reviewId))
    }

    @PostMapping
    fun createReview(@RequestBody createReviewRequest: CreateReviewRequest): ResponseEntity<ReviewResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(reviewServiceImpl.createReview(createReviewRequest))
    }

    @PutMapping("/{reviewId}")
    fun updateReview(
        @PathVariable reviewId: Long,
        @RequestBody updateReviewRequest: UpdateReviewRequest
    ): ResponseEntity<ReviewResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(reviewServiceImpl.updateReview(reviewId, updateReviewRequest))
    }

    @DeleteMapping("/{reviewId}")
    fun deleteReview(
        @PathVariable reviewId: Long
    ): ResponseEntity<Unit> {
        reviewServiceImpl.deleteReview(reviewId)

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(null)
    }


}