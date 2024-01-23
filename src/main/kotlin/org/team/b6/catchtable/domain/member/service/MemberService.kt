package org.team.b6.catchtable.domain.member.service

import org.springframework.boot.fromApplication
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team.b6.catchtable.domain.member.dto.response.MemberResponse
import org.team.b6.catchtable.domain.member.model.Member
import org.team.b6.catchtable.domain.member.repository.MemberRepository

@Service
@Transactional
class MemberService(
    private val memberRepository: MemberRepository,
) {
    fun getMemberList(): List<MemberResponse>{
        val memberList = memberRepository.findAll().map { MemberResponse.from(it) }
        return memberList
    }

    fun getMember(id: Long): MemberResponse {
        val foundMember =  memberRepository.findByIdOrNull(id)
            ?: throw Exception("Temp")
        return MemberResponse.from(foundMember)
       }
}

