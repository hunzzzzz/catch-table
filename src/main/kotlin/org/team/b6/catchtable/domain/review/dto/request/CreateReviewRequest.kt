package org.team.b6.catchtable.domain.review.dto.request

data class CreateReviewRequest(
    val memberId: Long,
    val storeId: Long,
    val content: String,
    val ratings: Int
)
