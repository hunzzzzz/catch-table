package org.team.b6.catchtable.domain.member.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import org.team.b6.catchtable.domain.blacklist.entity.BlackList
import org.team.b6.catchtable.domain.blacklist.entity.BlackListReason
import org.team.b6.catchtable.domain.member.model.Member
import org.team.b6.catchtable.domain.member.model.MemberRole
import java.time.LocalDateTime

data class BannedMemberResponse(
    val role: MemberRole,
    val nickname: String,
    val name: String,
    val email: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val until: LocalDateTime,
    val reasons: String
) {
    companion object {
        fun from(member: Member, reasons: String) = BannedMemberResponse(
            role = member.role,
            nickname = member.nickname,
            name = member.name,
            email = member.email,
            until = member.bannedExpiration!!,
            reasons = reasons
        )
    }
}