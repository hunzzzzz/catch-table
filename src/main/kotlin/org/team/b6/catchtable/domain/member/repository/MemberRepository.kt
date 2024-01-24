package org.team.b6.catchtable.domain.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.team.b6.catchtable.domain.member.model.Member

interface MemberRepository : JpaRepository<Member,Long> {
    fun findByEmail(email: String): Member?
}