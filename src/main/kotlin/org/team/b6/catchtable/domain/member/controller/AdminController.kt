package org.team.b6.catchtable.domain.member.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.team.b6.catchtable.domain.member.dto.request.SignupMemberRequest
import org.team.b6.catchtable.domain.member.service.AdminService

@RestController
@RequestMapping("/admins")
class AdminController(
    private val adminService: AdminService
) {
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stores")
    fun findAllStoreRequirements() =
        ResponseEntity.ok().body(adminService.findAllStoreRequirements())

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/stores/{storeId}/success")
    fun acceptStoreRequirement(@PathVariable storeId: Long) =
        ResponseEntity.ok().body(adminService.handleRequirement(storeId, true))

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/stores/{storeId}/fail")
    fun refuseStoreRequirement(@PathVariable storeId: Long) =
        ResponseEntity.ok().body(adminService.handleRequirement(storeId, false))

    // TODO: 추후 삭제 (테스트용)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun registerAdmin(@RequestBody request: SignupMemberRequest) {
        ResponseEntity.ok().body(adminService.registerAdmin(request))
    }
}