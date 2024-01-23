package org.team.b6.catchtable.domain.member.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import org.team.b6.catchtable.domain.member.model.Member
import org.team.b6.catchtable.domain.member.model.MemberRole
import java.time.LocalDateTime

data class MemberResponse(
    val role: MemberRole,
    val nickname: String,
    val name: String,
    val email: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.mm.dd", timezone = "Asia/Seoul")
    val createAt: LocalDateTime
)
{
    companion object {
        fun from(member: Member) = MemberResponse(
            role = member.role,
            nickname = member.nickname,
            name = member.name,
            email = member.email,
            createAt = member.createdAt,
        )
    }
}