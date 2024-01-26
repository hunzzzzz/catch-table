package org.team.b6.catchtable.domain.member.dto.request

import jakarta.validation.constraints.Pattern
import org.springframework.security.crypto.password.PasswordEncoder
import org.team.b6.catchtable.domain.member.model.Member
import org.team.b6.catchtable.domain.member.model.MemberRole

data class SignupMemberRequest(
    val role: String?,
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9]).{4,10}\$", message = "닉네임은 영어 소문자와 숫자로 이루어져 있습니다.")
    val nickname: String,
    val name: String,
    val email: String,
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[@#$%^&+=]).{8,15}$",
        message = "비밀번호는 영어 소문자와 대문자, 숫자, 특수문자(@#$%^&+=)로 이루어져 있어야 합니다.")
    val password: String,
) {
    fun to(passwordEncoder: PasswordEncoder) =
        Member(MemberRole.valueOf(role!!), nickname, name, email, passwordEncoder.encode(password))
}
