package org.team.b6.catchtable.domain.member.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.team.b6.catchtable.domain.member.service.AdminService

@RestController
@RequestMapping("/admins")
class AdminController(
    private val adminService: AdminService
) {
    @GetMapping("/stores")
    fun findAllStoreRequirements() =
        ResponseEntity.ok().body(adminService.findAllStoreRequirements())

    @PostMapping("/stores/{storeId}")
    fun acceptStoreRequirement(@PathVariable storeId: Long) =
        ResponseEntity.ok().body(adminService.accept(storeId))

    // TODO: declineStoreRequest 메서드 추후 생성
}