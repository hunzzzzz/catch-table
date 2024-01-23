package org.team.b6.catchtable.domain.store.controller

import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.team.b6.catchtable.domain.store.service.StoreRequirementService
import org.team.b6.catchtable.domain.store.service.StoreService

@RestController
@RequestMapping("/stores")
class StoreController(
    private val storeService: StoreService,
    private val storeRequirementService: StoreRequirementService
) {
    @GetMapping("/{category}")
    fun findAllStoresByCategory(@PathVariable category: String) =
        ResponseEntity.ok().body(storeService.findAllStoresByCategory(category))

    @GetMapping("/{category}/sorted")
    fun findAllStoresByCategoryWithSortCriteria(
        @PathVariable category: String,
        @RequestParam direction: Sort.Direction,
        @RequestParam criteria: String
    ) =
        ResponseEntity.ok().body(
            storeService.findAllStoresByCategoryWithSortCriteria(category, direction, criteria)
        )

    // TODO: 향후 추가/수정/삭제 메서드 구현시 storeRequirementService를 호출
}