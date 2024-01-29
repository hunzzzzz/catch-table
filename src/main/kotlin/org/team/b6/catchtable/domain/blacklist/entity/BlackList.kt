package org.team.b6.catchtable.domain.blacklist.entity

import jakarta.persistence.*
import org.team.b6.catchtable.domain.member.model.Member
import org.team.b6.catchtable.global.util.MailSenderService
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
    @Column(name = "black_list_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun addWarnings(member: Member, mailSenderService: MailSenderService) {
        member.numberOfWarnings++
        if (member.numberOfWarnings % 3 == 0){
            member.bannedExpiration = LocalDateTime.now().plusDays(7)
            mailSenderService.sendMail(
                email = member.email,
                subject = Variables.MAIL_SUBJECT_BANNED,
                text = Variables.MAIL_CONTENT_BANNED
            )
        }
    }
}