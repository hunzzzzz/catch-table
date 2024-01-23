package org.team.b6.catchtable.domain.store.dto.request

import org.team.b6.catchtable.domain.store.model.Store
import org.team.b6.catchtable.domain.store.model.StoreCategory

data class StoreRequest(
    val name: String,
    val category: StoreCategory,
    val description: String,
    val phone: String,
    val address: String
) {
    fun to() = Store(name, category, description, phone, address)
}