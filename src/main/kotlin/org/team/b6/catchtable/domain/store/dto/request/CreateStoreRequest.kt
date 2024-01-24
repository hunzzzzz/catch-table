package org.team.b6.catchtable.domain.store.dto.request

data class CreateStoreRequest(
    val name: String,
    val category: String,
    val description: String,
    val phone: String,
    val address: String
)