package org.team.b6.catchtable.domain.store.dto.request

import org.team.b6.catchtable.domain.store.model.Store
import org.team.b6.catchtable.domain.store.model.StoreCategory

data class StoreRequest(
    val name: String,
    val category: String,
    val description: String,
    val phone: String,
    val address: String
) {
    fun to() = Store(name, StoreCategory.valueOf(category), description, phone, address)
}