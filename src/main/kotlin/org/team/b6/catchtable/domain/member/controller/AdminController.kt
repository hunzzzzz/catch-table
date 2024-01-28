package org.team.b6.catchtable.domain.member.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.team.b6.catchtable.domain.member.dto.request.SignupMemberRequest
import org.team.b6.catchtable.domain.member.service.AdminService

@RestController
@RequestMapping("/admins")
//@PreAuthorize("hasRole('ADMIN')")
class AdminController(
    private val adminService: AdminService
) {
    @GetMapping("/stores")
    fun findAllStoreRequirements() =
        ResponseEntity.ok().body(adminService.findAllStoreRequirements())

    @GetMapping("/reviews")
    fun findAllReviewDeleteRequirements() =
        ResponseEntity.ok().body(adminService.findAllReviewDeleteRequirements())

    @GetMapping("/banned-members")
    fun findAllBannedMembers() =
        ResponseEntity.ok().body(adminService.findAllBannedMembers())

    @PostMapping("/stores/{storeId}/success")
    fun acceptStoreRequirement(@PathVariable storeId: Long) =
        ResponseEntity.ok().body(adminService.handleStoreRequirement(storeId, true))

    @PostMapping("/stores/{storeId}/fail")
    fun refuseStoreRequirement(@PathVariable storeId: Long) =
        ResponseEntity.ok().body(adminService.handleStoreRequirement(storeId, false))

    @DeleteMapping("/reviews/{reviewId}/success")
    fun acceptReviewRequirement(@PathVariable reviewId: Long) =
        ResponseEntity.ok().body(adminService.handleReviewRequirement(reviewId, true))

    @DeleteMapping("/reviews/{reviewId}/fail")
    fun refuseReviewRequirement(@PathVariable reviewId: Long) =
        ResponseEntity.ok().body(adminService.handleReviewRequirement(reviewId, false))

    // TODO: 추후 삭제 (테스트용)
    @PostMapping("/signup")
    fun registerAdmin(@RequestBody request: SignupMemberRequest) {
        ResponseEntity.ok().body(adminService.registerAdmin(request))
    }
}