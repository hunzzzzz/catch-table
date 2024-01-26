package org.team.b6.catchtable.domain.store.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.team.b6.catchtable.domain.store.dto.request.StoreRequest
import org.team.b6.catchtable.domain.store.service.StoreService
import org.team.b6.catchtable.global.security.MemberPrincipal
import java.net.URI

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
        @RequestParam criteria: String
    ) =
        ResponseEntity.ok().body(
            storeService.findAllStoresByCategoryWithSortCriteria(category, criteria)
        )

    @GetMapping("/store/{storeId}")
    fun findStore(@PathVariable storeId: Long) =
        ResponseEntity.ok().body(storeService.findStore(storeId))

    @PostMapping
    fun registerStore(
        @AuthenticationPrincipal memberPrincipal: MemberPrincipal,
        @RequestBody request: StoreRequest
    ): ResponseEntity<Unit> =
        ResponseEntity.created(
            URI.create("/stores/store/${storeService.registerStore(memberPrincipal, request)}")
        ).build()

    @PutMapping("/{storeId}")
    fun updateStore(
        @PathVariable storeId: Long,
        @RequestBody request: StoreRequest
    ): ResponseEntity<Unit> {
        storeService.updateStore(storeId, request)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{storeId}")
    fun deleteStore(
        @PathVariable storeId: Long
    ): ResponseEntity<Unit> {
        storeService.deleteStore(storeId)
        return ResponseEntity.ok().build()
    }
}