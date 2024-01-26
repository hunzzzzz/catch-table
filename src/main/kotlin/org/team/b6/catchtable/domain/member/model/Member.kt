package org.team.b6.catchtable.domain.member.model

import jakarta.persistence.*
import org.team.b6.catchtable.global.entity.BaseEntity

@Entity
@Table(name = "Members")
class Member(
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    val role: MemberRole,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "nickname", nullable = false)
    var nickname: String,

    @Column(name = "email", nullable = false)
    val email: String,

    @Column(name = "password", nullable = false)
    var password: String
) : BaseEntity() {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}