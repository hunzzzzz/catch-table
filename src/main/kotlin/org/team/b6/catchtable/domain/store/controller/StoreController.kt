package org.team.b6.catchtable.domain.store.controller

import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.team.b6.catchtable.domain.store.service.StoreRequirementService
import org.team.b6.catchtable.domain.store.service.StoreService
import org.team.b6.catchtable.domain.store.dto.request.CreateStoreRequest
import org.team.b6.catchtable.domain.store.dto.request.DeleteStoreRequest
import org.team.b6.catchtable.domain.store.dto.request.UpdateStoreRequest
import org.team.b6.catchtable.domain.store.dto.response.StoreResponse
import org.team.b6.catchtable.domain.store.service.StoreServiceImpl

@RestController
@RequestMapping("/stores")
class StoreController(
    private val storeService: StoreService,
    private val storeServiceImpl: StoreServiceImpl,
    private val storeRequirementService: StoreRequirementService
) {
    @GetMapping("/{storeId}")
    fun getStore(@PathVariable storeId: Long): ResponseEntity<StoreResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(storeServiceImpl.getStoreById(storeId))
    }
    //store 단건 조회

    @PostMapping
    fun createStore(@RequestBody createStoreRequest: CreateStoreRequest): ResponseEntity<StoreResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(storeServiceImpl.createStore(createStoreRequest))
    }
    //store 생성

    @PutMapping("/{storeId}")
    fun updateStore(
        @PathVariable storeId: Long,
        @RequestBody updateStoreRequest: UpdateStoreRequest
    ): ResponseEntity<StoreResponse> {
        // Controller의 역할은 단순히 클라이언트로부터 넘어온 파라미터를 서비스로 넘겨주는 역할
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(storeServiceImpl.updateStore(storeId, updateStoreRequest))
    }

    @DeleteMapping("/{storeId}")
    fun deleteStore(
        @PathVariable storeId: Long
    ): ResponseEntity<Unit> {
        storeServiceImpl.deleteStore(storeId)

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(null)
    }

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