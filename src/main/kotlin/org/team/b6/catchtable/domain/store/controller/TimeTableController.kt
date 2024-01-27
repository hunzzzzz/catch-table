//package org.team.b6.catchtable.domain.store.controller
//
//import org.springframework.http.ResponseEntity
//import org.springframework.web.bind.annotation.PathVariable
//import org.springframework.web.bind.annotation.PutMapping
//import org.springframework.web.bind.annotation.RequestBody
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RestController
//import org.team.b6.catchtable.domain.store.dto.request.TimeTableRequest
//import org.team.b6.catchtable.domain.store.service.TimeTableService
//
//@RestController
//@RequestMapping("/stores/{storeId}/time-table")
//class TimeTableController(
//    private val timeTableService: TimeTableService
//) {
//    @PutMapping
//    fun setTimeTable(
//        @PathVariable storeId: Long,
//        @RequestBody request: TimeTableRequest
//    ) = ResponseEntity.ok().body(timeTableService.setTimeTable(storeId, request))
//}