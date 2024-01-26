package org.team.b6.catchtable.domain.member.dto.request

import org.springframework.web.bind.annotation.PathVariable
import org.team.b6.catchtable.domain.member.model.Member

data class UpdateMemberRequest(
    val nickname: String,
    val name: String,
    val email: String,
    val password: String,
    val newPassword: String? = null,
){
    //fun to()= Member(role,nickname,name,email,password)
}
