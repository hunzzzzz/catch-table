package org.team.b6.catchtable.domain.store.controller

import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.team.b6.catchtable.domain.store.dto.request.StoreRequest
import org.team.b6.catchtable.domain.store.service.StoreRequirementService
import org.team.b6.catchtable.domain.store.service.StoreService
import org.team.b6.catchtable.domain.store.dto.response.StoreResponse

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

    @GetMapping("/{storeId}")
    fun getStore(@PathVariable storeId: Long): ResponseEntity<StoreResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(storeService.findStore(storeId))
    }

    @PostMapping
    fun registerStore(@RequestBody request: StoreRequest): ResponseEntity<Unit> {
        storeRequirementService.applyForRegister(request)
        return ResponseEntity.ok().build()
    }

    @PutMapping("/{storeId}")
    fun updateStore(
        @PathVariable storeId: Long,
        @RequestBody updateStoreRequest: StoreRequest
    ): ResponseEntity<Unit> {
        storeRequirementService.applyForUpdate(storeId, updateStoreRequest)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{storeId}")
    fun deleteStore(
        @PathVariable storeId: Long
    ): ResponseEntity<Unit> {
        storeRequirementService.applyForDelete(storeId)
        return ResponseEntity.noContent().build()
    }
}