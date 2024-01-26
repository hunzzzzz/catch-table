package org.team.b6.catchtable.domain.store.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team.b6.catchtable.domain.store.dto.request.TimeTableRequest
import org.team.b6.catchtable.domain.store.repository.TimeTableRepository
import org.team.b6.catchtable.global.service.GlobalService

@Service
@Transactional
class TimeTableService(
    val timeTableRepository: TimeTableRepository,
    val globalService: GlobalService
) {
    // 초기 타임 테이블 세팅
    fun setTimeTable(storeId: Long, request: TimeTableRequest) {
        validateTimeTable(storeId, request)
        val store = globalService.getStore(storeId)
        val timeTable = timeTableRepository.findByIdOrNull(store.timeTable.id) ?: throw Exception("")
        timeTable.update(request)
    }

    private fun validateTimeTable(storeId: Long, request: TimeTableRequest) {
        val store = globalService.getStore(storeId)
        val timeTableList = request.toMutableList()

        for (i in timeTableList.indices) {
            // [0 = time8, 1 = time9, 2 = time10, ..., 6 = time14, 7 = time15, ..., 14 = time22, 15 = time23]
            if (
                (i < store.openTime - 8 && timeTableList[i] != null)
                || (i > store.closeTime - 8 && timeTableList[i] != null)
            ) throw Exception("영업 시간이 아닙니다") // TODO : 추후 Exception 생성
        }
    }
}