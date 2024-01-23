package org.team.b6.catchtable.domain.review.model

import jakarta.persistence.*
import org.team.b6.catchtable.domain.member.model.Member
import org.team.b6.catchtable.domain.store.model.Store
import org.team.b6.catchtable.global.entity.BaseEntity

@Entity
@Table(name = "Reviews")
class Review(
    @Column(name = "content", nullable = false)
    val content: String,

    @Column(name = "ratings", nullable = false)
    val ratings: Int,

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
}