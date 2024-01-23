package org.team.b6.catchtable.domain.member.dto.request

import org.team.b6.catchtable.domain.member.model.Member
import org.team.b6.catchtable.domain.member.model.MemberRole

data class SignupMemberRequest(
    val role: MemberRole,
    val nickname: String,
    val name: String,
    val email: String,
    val password: String,
)
{
    fun to()= Member(role,nickname,name,email,password)
}
