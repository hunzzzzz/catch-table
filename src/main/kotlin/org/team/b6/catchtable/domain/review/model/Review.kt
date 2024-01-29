package org.team.b6.catchtable.domain.review.model

import jakarta.persistence.*
import org.team.b6.catchtable.domain.member.model.Member
import org.team.b6.catchtable.domain.review.dto.request.ReviewRequest
import org.team.b6.catchtable.domain.store.model.Store
import org.team.b6.catchtable.global.entity.BaseEntity

@Entity
@Table(name = "Reviews")
class Review(
    @Column(name = "content", nullable = false)
    var content: String,

    @Column(name = "ratings", nullable = false)
    var ratings: Int,

    @ManyToOne
    @JoinColumn(name = "member_id")
    val member: Member,

    @ManyToOne
    @JoinColumn(name = "store_id")
    val store: Store
) : BaseEntity() {
    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    var status = ReviewStatus.OK

    fun update(request: ReviewRequest) {
        this.content = request.content
        this.ratings = request.ratings
    }

    fun updateStatus(status: ReviewStatus) {
        this.status = status
    }
}