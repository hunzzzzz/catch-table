package org.team.b6.catchtable.domain.review.dto.response

import org.team.b6.catchtable.domain.review.model.Review

data class ReviewResponse(
    val content: String,
    val ratings: Int
) {
    companion object{
        fun from(review: Review): ReviewResponse {
            return ReviewResponse(
                content = review.content,
                ratings = review.ratings
            )
        }
    }
}