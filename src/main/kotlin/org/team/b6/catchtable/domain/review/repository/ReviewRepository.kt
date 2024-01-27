package org.team.b6.catchtable.domain.review.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.team.b6.catchtable.domain.review.model.Review

interface ReviewRepository : JpaRepository<Review, Long> {
    fun getAllByStoreId(storeId: Long): List<Review>
}