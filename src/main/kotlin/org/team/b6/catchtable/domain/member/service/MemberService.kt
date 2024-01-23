package org.team.b6.catchtable.domain.member.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team.b6.catchtable.domain.member.repository.MemberRepository

@Service
//@Transactional
class MemberService(
    private val memberRepository: MemberRepository,
) {

}