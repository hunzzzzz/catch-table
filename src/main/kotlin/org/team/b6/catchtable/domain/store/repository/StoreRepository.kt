package org.team.b6.catchtable.domain.store.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.team.b6.catchtable.domain.store.model.Store

@Repository
interface StoreRepository : JpaRepository<Store, Long>