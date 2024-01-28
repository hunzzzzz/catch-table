package org.team.b6.catchtable.domain.review.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import org.team.b6.catchtable.domain.review.model.Review
import java.time.LocalDateTime

data class ReviewResponse(
    val writerName: String,
    val content: String,
    val ratings: Int,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime,
) {
    companion object{
        fun from(review: Review): ReviewResponse {
            return ReviewResponse(
                writerName = review.member.name,
                content = review.content,
                ratings = review.ratings,
                createdAt = review.createdAt
            )
        }
    }
}