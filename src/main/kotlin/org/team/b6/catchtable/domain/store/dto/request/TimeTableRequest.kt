package org.team.b6.catchtable.domain.store.dto.request

import org.team.b6.catchtable.domain.store.model.TimeTable

data class TimeTableRequest(
    val time8: Int? = null,
    val time9: Int? = null,
    val time10: Int? = null,
    val time11: Int? = null,
    val time12: Int? = null,
    val time13: Int? = null,
    val time14: Int? = null,
    val time15: Int? = null,
    val time16: Int? = null,
    val time17: Int? = null,
    val time18: Int? = null,
    val time19: Int? = null,
    val time20: Int? = null,
    val time21: Int? = null,
    val time22: Int? = null,
    val time23: Int? = null
) {
    fun to() = TimeTable(
        time8, time9, time10, time11, time12, time13, time14,
        time15, time16, time17, time18, time19, time20, time21, time22, time23
    )

    fun toMutableList() = mutableListOf(
        time8, time9, time10, time11, time12, time13, time14,
        time15, time16, time17, time18, time19, time20, time21, time22, time23
    )
}