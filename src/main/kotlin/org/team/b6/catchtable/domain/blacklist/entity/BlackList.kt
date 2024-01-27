package org.team.b6.catchtable.domain.blacklist.entity

import jakarta.persistence.*
import org.team.b6.catchtable.domain.member.model.Member
import org.team.b6.catchtable.domain.member.model.MemberRole
import org.team.b6.catchtable.global.service.UtilService
import org.team.b6.catchtable.global.variable.Variables
import java.time.LocalDateTime

@Entity
@Table(name = "BlackList")
class BlackList(
    @Column(name = "subject", nullable = false)
    val subject: Long, // 대상

    @Column(name = "reason", nullable = false)
    @Enumerated(EnumType.STRING)
    val reason: BlackListReason, // 사유
) {
    @Id
    @Column(name = "store_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun addWarnings(member: Member, utilService: UtilService) {
        member.numberOfWarnings++
        if (member.numberOfWarnings % 3 == 0){
            member.bannedExpiration = LocalDateTime.now().plusDays(7)
//            member.role = MemberRole.BANNED
            utilService.sendMail(
                email = member.email,
                subject = Variables.MAIL_SUBJECT_BANNED,
                text = Variables.MAIL_CONTENT_BANNED
            )
        }
    }
}