package org.team.b6.catchtable.domain.store.dto.request

import com.fasterxml.jackson.annotation.JsonFormat
import org.team.b6.catchtable.domain.store.model.Store
import org.team.b6.catchtable.domain.store.model.StoreCategory
import java.time.LocalTime

data class StoreRequest(
    val name: String,
    val category: String,
    val description: String,
    val phone: String,
    val address: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    val openTime: LocalTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    val closeTime: LocalTime
) {
    fun to(ownerId: Long) = Store(
        belongTo = ownerId,
        name = name,
        category = StoreCategory.valueOf(category),
        description = description,
        phone = phone,
        address = address,
        openTime = openTime,
        closeTime = closeTime
    )
}