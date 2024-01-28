package org.team.b6.catchtable.domain.member.model

import jakarta.persistence.*
import org.team.b6.catchtable.global.entity.BaseEntity
import java.time.LocalDateTime

@Entity
@Table(name = "Members")
class Member(
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    var role: MemberRole,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "nickname", nullable = false)
    var nickname: String,

    @Column(name = "email", nullable = false)
    val email: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Column(name = "is_deleted", nullable = false)
    var isDeleted: Boolean = false,
) : BaseEntity() {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(name = "number_of_warnings", nullable = false)
    var numberOfWarnings: Int = 0// 경고 누적 횟수

    @Column(name = "banned_expiration")
    var bannedExpiration: LocalDateTime? = null // 계정 정지 만료일
}