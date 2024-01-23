package org.team.b6.catchtable.domain.review.dto.request

data class UpdateReviewRequest(
    val memberId: Long,
    val storeId: Long,
    val content: String,
    val ratings: Int
)
