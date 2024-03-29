package org.team.b6.catchtable.domain.member.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team.b6.catchtable.domain.member.dto.request.LoginRequest
import org.team.b6.catchtable.domain.member.dto.request.RefreshRequest
import org.team.b6.catchtable.domain.member.dto.request.SignupMemberRequest
import org.team.b6.catchtable.domain.member.dto.request.UpdateMemberRequest
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
        if (memberRepository.existsByEmail(request.email)) {
            throw IllegalStateException("Email is already in use")
        }
        if (memberRepository.existsByNickname(request.nickname)) {
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
            ),
            refreshToken = jwtPlugin.generateRefreshToken(
                subject = member.id.toString(),
                email = member.email,
                role = member.role.name
            )
        )
    }

    fun refreshToken(memberId: Long, refreshRequest: RefreshRequest): LoginResponse {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("member")
        return LoginResponse(
            accessToken = jwtPlugin.generateAccessToken(
                subject = member.id.toString(),
                email = member.email,
                role = member.role.name
            ),
            refreshToken = refreshRequest.refreshToken
        )
    }


    fun updateMember(memberId: Long, request: UpdateMemberRequest): MemberResponse {
        val foundIdMember = memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("member")
        val foundMember = memberRepository.findByEmail(request.email) ?: throw ModelNotFoundException("member")

        if (foundIdMember != foundMember) {
            throw InvalidCredentialException("Member")
        }

        if (foundMember.nickname != request.nickname && memberRepository.existsByNickname(request.nickname)) {
            throw IllegalStateException("Nickname is already in use")
        }

        if (!passwordEncoder.matches(request.password, foundMember.password)) {
            if (request.newPassword == null || !passwordEncoder.matches(request.password, request.newPassword)) {
                throw IllegalArgumentException("Passwords do not match")
            }
            foundMember.password = passwordEncoder.encode(request.password)
        }

        foundMember.nickname = request.nickname
        foundMember.name = request.name

        return MemberResponse.from(foundMember)
    }

    fun withdrawMember(id: Long) {
        val member = memberRepository.findByIdOrNull(id) ?: throw ModelNotFoundException("Memeber")

        when (member.role) {
            MemberRole.USER -> {
                member.nickname = "탈퇴한 회원${member.id}"
                member.role = MemberRole.WITHDRAWN
                member.isDeleted = true
            }

            MemberRole.OWNER -> {
//                val stores = storeRepository.findByBelongsTo(memberId)
//                stores.forEach { store ->
//                    reviewRepository.deleteByStoreId(store.id!!)
//                    storeRepository.delete(store)
//                }
                member.nickname = "탈퇴한 회원${member.id}"
                member.role = MemberRole.WITHDRAWN
                member.isDeleted = true
            }

            MemberRole.ADMIN -> throw IllegalArgumentException("Admin cannot withdraw")
            else -> throw IllegalArgumentException("Invalid role")
        }

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
}
