package org.team.b6.catchtable.domain.review.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.team.b6.catchtable.domain.review.dto.request.ReviewRequest
import org.team.b6.catchtable.domain.review.service.ReviewService
import org.team.b6.catchtable.global.security.MemberPrincipal
import java.net.URI

@RequestMapping("/stores/{storeId}/reviews")
@RestController
class ReviewController(
    private val reviewService: ReviewService
) {
    @PreAuthorize("hasRole('USER') || hasRole('OWNER') || hasRole('ADMIN')")
    @GetMapping
    fun findReviews(@PathVariable storeId: Long) =
        ResponseEntity.ok().body(reviewService.findReviews(storeId))

    @PreAuthorize("hasRole('USER') || hasRole('OWNER') || hasRole('ADMIN')")
    @GetMapping("/{reviewId}")
    fun findReview(@PathVariable storeId: Long, @PathVariable reviewId: Long) =
        ResponseEntity.ok().body(reviewService.findReview(storeId, reviewId))

    @PreAuthorize("hasRole('USER') || hasRole('OWNER')")
    @PostMapping
    fun addReview(
        @AuthenticationPrincipal memberPrincipal: MemberPrincipal,
        @PathVariable storeId: Long,
        @RequestBody request: ReviewRequest
    ): ResponseEntity<Unit> =
        ResponseEntity.created(
            URI.create(
                "/reviews/${reviewService.addReview(memberPrincipal, storeId, request)}"
            )
        ).build()

    @PreAuthorize("hasRole('USER') || hasRole('OWNER')")
    @PutMapping("/{reviewId}")
    fun updateReview(
        @AuthenticationPrincipal memberPrincipal: MemberPrincipal,
        @PathVariable storeId: Long,
        @PathVariable reviewId: Long,
        @RequestBody request: ReviewRequest
    ) = ResponseEntity.ok().body(reviewService.updateReview(memberPrincipal, storeId, reviewId, request))

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{reviewId}")
    fun deleteReview(
        @AuthenticationPrincipal memberPrincipal: MemberPrincipal,
        @PathVariable storeId: Long,
        @PathVariable reviewId: Long
    ): ResponseEntity<Unit> {
        reviewService.deleteReview(memberPrincipal, storeId, reviewId)
        return ResponseEntity.noContent().build()
    }

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/{reviewId}")
    fun requireForDeleteReview(
        @AuthenticationPrincipal memberPrincipal: MemberPrincipal,
        @PathVariable storeId: Long,
        @PathVariable reviewId: Long
    ): ResponseEntity<Unit> {
        reviewService.requireForDeleteReview(memberPrincipal, storeId, reviewId)
        return ResponseEntity.ok().build()
    }
}