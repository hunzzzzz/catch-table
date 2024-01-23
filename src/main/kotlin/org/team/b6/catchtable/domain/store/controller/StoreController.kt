package org.team.b6.catchtable.domain.store.controller

import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.team.b6.catchtable.domain.store.dto.request.StoreRequest
import org.team.b6.catchtable.domain.store.model.Store
import org.team.b6.catchtable.domain.store.service.StoreService

@RestController
@RequestMapping("/stores")
class StoreController(
    private val storeService: StoreService
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
}