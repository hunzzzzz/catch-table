package org.team.b6.catchtable.domain.store.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
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
    @PreAuthorize("hasRole('USER') || hasRole('OWNER') || hasRole('ADMIN')")
    @GetMapping("/{category}")
    fun findAllStoresByCategory(@PathVariable category: String) =
        ResponseEntity.ok().body(storeService.findAllStoresByCategory(category))

    @PreAuthorize("hasRole('USER') || hasRole('OWNER') || hasRole('ADMIN')")
    @GetMapping("/{category}/sorted")
    fun findAllStoresByCategoryWithSortCriteria(
        @PathVariable category: String,
        @RequestParam criteria: String
    ) =
        ResponseEntity.ok().body(
            storeService.findAllStoresByCategoryWithSortCriteria(category, criteria)
        )

    @PreAuthorize("hasRole('USER') || hasRole('OWNER') || hasRole('ADMIN')")
    @GetMapping("/store/{storeId}")
    fun findStore(@PathVariable storeId: Long) =
        ResponseEntity.ok().body(storeService.findStore(storeId))

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping
    fun registerStore(
        @AuthenticationPrincipal memberPrincipal: MemberPrincipal,
        @RequestBody @Valid request: StoreRequest
    ): ResponseEntity<Unit> =
        ResponseEntity.created(
            URI.create("/stores/store/${storeService.registerStore(memberPrincipal, request)}")
        ).build()

    @PreAuthorize("hasRole('OWNER')")
    @PutMapping("/{storeId}")
    fun updateStore(
        @PathVariable storeId: Long,
        @RequestBody request: StoreRequest
    ): ResponseEntity<Unit> {
        storeService.updateStore(storeId, request)
        return ResponseEntity.ok().build()
    }

    @PreAuthorize("hasRole('OWNER')")
    @DeleteMapping("/{storeId}")
    fun deleteStore(
        @PathVariable storeId: Long
    ): ResponseEntity<Unit> {
        storeService.deleteStore(storeId)
        return ResponseEntity.ok().build()
    }
}