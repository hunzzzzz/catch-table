package org.team.b6.catchtable.domain.member.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import org.team.b6.catchtable.domain.store.dto.response.StoreResponse
import org.team.b6.catchtable.domain.store.model.StoreRequirement
import java.time.LocalDateTime

data class AdminResponse(
    val requirement: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime,
    val store: StoreResponse
) {
    companion object {
        fun from(storeRequirement: StoreRequirement) = AdminResponse(
            requirement = storeRequirement.requirement.name,
            createdAt = storeRequirement.createdAt,
            store = StoreResponse.from(storeRequirement.store!!)
        )
    }
}