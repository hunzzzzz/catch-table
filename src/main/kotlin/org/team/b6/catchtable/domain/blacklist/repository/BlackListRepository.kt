package org.team.b6.catchtable.domain.blacklist.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.team.b6.catchtable.domain.blacklist.entity.BlackList

interface BlackListRepository : JpaRepository<BlackList, Long>