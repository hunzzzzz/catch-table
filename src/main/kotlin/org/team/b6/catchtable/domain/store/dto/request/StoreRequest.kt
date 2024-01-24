package org.team.b6.catchtable.domain.store.dto.request

import org.team.b6.catchtable.domain.store.model.Store
import org.team.b6.catchtable.domain.store.model.StoreCategory
import org.team.b6.catchtable.domain.store.model.StoreRequirement
import org.team.b6.catchtable.domain.store.model.StoreRequirementCategory
import java.time.LocalDateTime

data class StoreRequest(
    val name: String,
    val category: String,
    val description: String,
    val phone: String,
    val address: String
) {
    fun to() = Store(
        name = name,
        category = StoreCategory.valueOf(category),
        description = description,
        phone = phone,
        address = address
    )

    fun to(requirement: StoreRequirementCategory, store: Store?) = StoreRequirement(
        requirement = requirement,
        store = store,
        createdAt = LocalDateTime.now()
    )
}