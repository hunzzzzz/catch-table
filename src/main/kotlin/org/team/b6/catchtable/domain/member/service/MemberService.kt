package org.team.b6.catchtable.domain.member.service

import org.springframework.boot.fromApplication
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team.b6.catchtable.domain.member.dto.request.LoginRequest
import org.team.b6.catchtable.domain.member.dto.request.SignupMemberRequest
import org.team.b6.catchtable.domain.member.dto.response.LoginResponse
import org.team.b6.catchtable.domain.member.dto.response.MemberResponse
import org.team.b6.catchtable.domain.member.model.Member
import org.team.b6.catchtable.domain.member.model.MemberRole
import org.team.b6.catchtable.domain.member.repository.MemberRepository

@Service
@Transactional
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    fun signUp(request: SignupMemberRequest): MemberResponse {

        return memberRepository.save(
            Member(
                email = request.email,
                nickname = request.nickname,
                name = request.name,
                password = passwordEncoder.encode(request.password),
                role = when (request.role) {
                    "USER" -> MemberRole.USER
                    "OWNER" -> MemberRole.OWNER
                    else -> throw Exception("Temp")
                }
            )
        ).let { MemberResponse.from(it) }
    }

    fun login(request: LoginRequest): LoginResponse {
        val member = memberRepository.findByEmail(request.email) ?: throw Exception("Temp")
        if (member.role.name != request.role || !passwordEncoder.matches(request.password, member.password))
            throw Exception("Temp")

        return LoginResponse(
            email = member.email,
            role = member.role.name,
            name = member.name,
            nickname = member.nickname
        )
    }


    fun getMemberList(): List<MemberResponse> {
        val memberList = memberRepository.findAll().map { MemberResponse.from(it) }
        return memberList
    }

    fun getMember(id: Long): MemberResponse {
        val foundMember = memberRepository.findByIdOrNull(id)
            ?: throw Exception("Temp")
        return MemberResponse.from(foundMember)
    }
}

