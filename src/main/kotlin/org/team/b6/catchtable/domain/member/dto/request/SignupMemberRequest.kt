package org.team.b6.catchtable.domain.member.dto.request

import org.team.b6.catchtable.domain.member.model.Member
import org.team.b6.catchtable.domain.member.model.MemberRole

data class SignupMemberRequest(
    val role: String?,
    val nickname: String,
    val name: String,
    val email: String,
    val password: String,
) {
    //fun to() = Member(MemberRole.valueOf(role), nickname, name, email, password)
}
