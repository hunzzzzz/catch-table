package org.team.b6.catchtable.domain.member.dto.response

import org.team.b6.catchtable.domain.member.model.Member
import org.team.b6.catchtable.domain.member.model.MemberRole
import java.time.LocalDateTime

data class MemberResponse(
    //회원가입시 response
    val role: MemberRole,
    val nickname: String,
    val name: String,
    val email: String,
    val createAt: LocalDateTime
)
{
    companion object
    fun from(member: Member) = MemberResponse (
        role = member.role,
        nickname = member.nickname,
        name = member.name,
        email = member.email,
        createAt = member.createdAt,
    )
}