package org.team.b6.catchtable.domain.review.dto.request

import org.team.b6.catchtable.domain.member.model.Member
import org.team.b6.catchtable.domain.review.model.Review
import org.team.b6.catchtable.domain.store.model.Store

data class ReviewRequest(
    val memberId: Long,
    val content: String,
    val ratings: Int
) {
    fun to(member: Member, store: Store) = Review(content, ratings, member, store)
}