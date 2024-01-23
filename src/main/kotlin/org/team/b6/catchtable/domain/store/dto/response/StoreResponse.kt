package org.team.b6.catchtable.domain.store.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import org.team.b6.catchtable.domain.store.model.Store
import org.team.b6.catchtable.domain.store.model.StoreCategory
import java.time.LocalDateTime

data class StoreResponse(
    val name: String,
    val category: StoreCategory,
    val description: String,
    val phone: String,
    val address: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(store: Store) = StoreResponse(
            name = store.name,
            category = store.category,
            description = store.description,
            phone = store.phone,
            address = store.address,
            createdAt = store.createdAt
        )
    }
}