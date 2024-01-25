package org.team.b6.catchtable.domain.member.service

import org.intellij.lang.annotations.Pattern
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
import org.team.b6.catchtable.global.exception.InvalidCredentialException
import org.team.b6.catchtable.global.exception.ModelNotFoundException
import org.team.b6.catchtable.global.security.jwt.JwtPlugin

@Service
@Transactional
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin,
) {

    fun signUp(request: SignupMemberRequest): MemberResponse {
        isValidNickname(request.nickname)
//        isValidPassword(request.password)

        if (memberRepository.existsByEmail(request.email)){
            throw IllegalStateException("Email is already in use")
        }
        if (memberRepository.existsByNickname(request.nickname)){
            throw IllegalStateException("Nickname is already in use")
        }
        return memberRepository.save(
            Member(
                email = request.email,
                nickname = request.nickname,
                name = request.name,
                password = passwordEncoder.encode(request.password),
                role = when (request.role) {
                    "USER" -> MemberRole.USER
                    "OWNER" -> MemberRole.OWNER
                    else -> throw IllegalArgumentException("Invalid role")
                }
            )
        ).let { MemberResponse.from(it) }

    }

    fun login(request: LoginRequest): LoginResponse {
        val member = memberRepository.findByEmail(request.email) ?: throw ModelNotFoundException("member")
        if (member.role.name != request.role) throw InvalidCredentialException("role")
        if (!passwordEncoder.matches(request.password, member.password)) throw InvalidCredentialException("password")

        return LoginResponse(
            accessToken = jwtPlugin.generateAccessToken(
                subject = member.id.toString(),
                email = member.email,
                role = member.role.name
            )
        )
    }


    fun getMemberList(): List<MemberResponse> {
        val memberList = memberRepository.findAll().map { MemberResponse.from(it) }
        return memberList
    }

    fun getMember(id: Long): MemberResponse {
        val foundMember = memberRepository.findByIdOrNull(id)
            ?: throw ModelNotFoundException("member")
        return MemberResponse.from(foundMember)
    }

    fun isValidNickname(nickname: String?): Boolean {
        val trimmedNickname = nickname?.trim().toString()
        val exp = Regex("^(?=.*[a-z])(?=.*[0-9]).{4,10}\$")
        if (!trimmedNickname.isNullOrEmpty() && exp.matches(trimmedNickname)) {
            return true
        } else {
            throw IllegalArgumentException("Invalid nickname")
        }
    }

//    fun isValidPassword(password: String?): Boolean {
//        val trimmedPassword = password?.trim().toString()
//        val exp = Regex("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[@#$%^&+=]).{8,15}$")
//        if (!trimmedPassword.isNullOrEmpty() && exp.matches(trimmedPassword)) {
//            return true
//        } else {
//            throw IllegalArgumentException("Invalid password")
//        }
//    }
}

