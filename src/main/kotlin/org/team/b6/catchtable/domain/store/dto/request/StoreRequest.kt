package org.team.b6.catchtable.domain.store.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Range
import org.team.b6.catchtable.domain.store.model.Store
import org.team.b6.catchtable.domain.store.model.StoreCategory
import org.team.b6.catchtable.global.aop.ValidCategory
import java.time.LocalTime

data class StoreRequest(
    @field:NotBlank
    val name: String,

    @field:NotBlank
    @field:ValidCategory(
        message = "유효하지 않은 카테고리입니다.",
        enumClass = StoreCategory::class
    )
    val category: String,

    @field:NotBlank
    val description: String,

    @field:NotBlank
    @field:Pattern(
        regexp = "^010-?([0-9]{4})-?([0-9]{4})$",
        message = "휴대폰 형식(010-1234-5678)을 확인해주세요."
    )
    val phone: String,

    @field:NotBlank
    val address: String,

    @NotNull
    @field:Range(
        min = 5,
        max = 23,
        message = "0부터 24까지의 숫자만 입력 가능합니다."
    )
    val openTime: Int,

    @NotNull
    @field:Range(
        min = 12,
        max = 23,
        message = "0부터 24까지의 숫자만 입력 가능합니다."
    )
    val closeTime: Int
) {
    fun to(ownerId: Long) = Store(
        belongTo = ownerId,
        name = name,
        category = StoreCategory.valueOf(category),
        description = description,
        phone = phone,
        address = address,
        openTime = LocalTime.of(openTime, 0),
        closeTime = LocalTime.of(closeTime, 0)
    )
}