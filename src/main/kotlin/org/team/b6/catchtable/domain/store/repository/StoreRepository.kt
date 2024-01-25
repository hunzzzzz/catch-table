package org.team.b6.catchtable.domain.store.repository

import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.team.b6.catchtable.domain.store.model.Store
import org.team.b6.catchtable.domain.store.model.StoreCategory

@Repository
interface StoreRepository : JpaRepository<Store, Long> {

    fun findAllByCategory(category: StoreCategory): List<Store>

    fun findAllByCategory(category: StoreCategory, sort: Sort): List<Store>

    fun existsByAddress(address: String): Boolean

    fun existsByName(name: String): Boolean
}