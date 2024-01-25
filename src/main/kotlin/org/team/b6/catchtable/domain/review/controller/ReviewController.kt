package org.team.b6.catchtable.domain.review.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.team.b6.catchtable.domain.review.dto.request.ReviewRequest
import org.team.b6.catchtable.domain.review.service.ReviewService
import java.net.URI

@RequestMapping("/stores/{storeId}/reviews")
@RestController
class ReviewController(
    private val reviewService: ReviewService
) {
    @GetMapping
    fun findReviews(@PathVariable storeId: Long) =
        ResponseEntity.ok().body(reviewService.findReviews(storeId))

    @GetMapping("/{reviewId}")
    fun findReview(@PathVariable storeId: Long, @PathVariable reviewId: Long) =
        ResponseEntity.ok().body(reviewService.findReview(storeId, reviewId))

    @PostMapping
    fun addReview(
        @PathVariable storeId: Long,
        @RequestBody request: ReviewRequest
    ): ResponseEntity<Unit> =
        ResponseEntity.created(
            URI.create(
                "/reviews/${reviewService.addReview(storeId, request)}"
            )
        ).build()

    @PutMapping("/{reviewId}")
    fun updateReview(
        @PathVariable storeId: Long,
        @PathVariable reviewId: Long,
        @RequestBody request: ReviewRequest
    ): ResponseEntity<Unit> {
        reviewService.updateReview(storeId, reviewId, request)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{reviewId}")
    fun deleteReview(
        @PathVariable storeId: Long,
        @PathVariable reviewId: Long
    ): ResponseEntity<Unit> {
        reviewService.deleteReview(storeId, reviewId)
        return ResponseEntity.noContent().build()
    }
}