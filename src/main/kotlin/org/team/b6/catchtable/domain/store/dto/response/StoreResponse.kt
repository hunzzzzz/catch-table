package org.team.b6.catchtable.domain.store.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import org.team.b6.catchtable.domain.member.model.Member
import org.team.b6.catchtable.domain.review.dto.response.ReviewResponse
import org.team.b6.catchtable.domain.review.model.Review
import org.team.b6.catchtable.domain.store.model.Store
import org.team.b6.catchtable.domain.store.model.StoreCategory
import java.time.LocalDateTime

data class StoreResponse(
    val name: String,
    val owner: String,
    val averageRatings: Double,
    val category: StoreCategory,
    val description: String,
    val phone: String,
    val address: String,
    val openTime: String,
    val closeTime: String,
    val reviews: List<ReviewResponse>,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(store: Store, member: Member, reviews: List<ReviewResponse>) = StoreResponse(
            name = store.name,
            owner = member.name,
            averageRatings = getAverageOfRatings(reviews),
            category = store.category,
            description = store.description,
            phone = store.phone,
            address = store.address,
            openTime = "${store.openTime}시",
            closeTime = "${store.closeTime}시",
            reviews = reviews,
            createdAt = store.createdAt
        )

        private fun getAverageOfRatings(reviewList: List<ReviewResponse>) =
            reviewList.let {
                Math.round(
                    it.sumOf { review -> review.ratings } / it.size.toDouble() * 10
                ) / 10.0
            }
    }
}