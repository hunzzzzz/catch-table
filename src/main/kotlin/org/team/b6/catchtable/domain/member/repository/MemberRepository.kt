package org.team.b6.catchtable.domain.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.team.b6.catchtable.domain.member.model.Member

@Repository
interface MemberRepository : JpaRepository<Member, Long> {
}