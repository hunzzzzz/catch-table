package org.team.b6.catchtable.domain.store.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import org.team.b6.catchtable.domain.member.model.Member
import org.team.b6.catchtable.domain.store.model.Store
import org.team.b6.catchtable.domain.store.model.StoreCategory
import java.time.LocalDateTime
import java.time.LocalTime

data class StoreResponse(
    val name: String,
    val owner: String,
    val category: StoreCategory,
    val description: String,
    val phone: String,
    val address: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    val openTime: LocalTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    val closeTime: LocalTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(store: Store, member: Member) = StoreResponse(
            name = store.name,
            owner = member.name,
            category = store.category,
            description = store.description,
            phone = store.phone,
            address = store.address,
            openTime = store.openTime,
            closeTime = store.closeTime,
            createdAt = store.createdAt
        )
    }
}