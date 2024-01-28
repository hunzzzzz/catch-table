package org.team.b6.catchtable.domain.member.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.team.b6.catchtable.domain.member.dto.request.LoginRequest
import org.team.b6.catchtable.domain.member.dto.request.RefreshRequest
import org.team.b6.catchtable.domain.member.dto.request.SignupMemberRequest
import org.team.b6.catchtable.domain.member.dto.request.UpdateMemberRequest
import org.team.b6.catchtable.domain.member.dto.response.LoginResponse
import org.team.b6.catchtable.domain.member.dto.response.MemberResponse
import org.team.b6.catchtable.domain.member.service.MemberService
import org.team.b6.catchtable.global.security.MemberPrincipal

@RestController
@RequestMapping("/members")
class MemberController(
    val memberService: MemberService
) {

    @PostMapping("/signup")
    fun signup(@RequestBody @Valid signupMemberRequest: SignupMemberRequest): ResponseEntity<MemberResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.signUp(signupMemberRequest))
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.login(loginRequest))
    }


    @PutMapping("/{memberId}")
    fun updateMember(
        @AuthenticationPrincipal memberPrincipal: MemberPrincipal,
        @RequestBody updateRequest: UpdateMemberRequest
    ): ResponseEntity<MemberResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.updateMember(memberPrincipal.id, updateRequest))
    }

    @PutMapping("/{memberId}/withdraw")
    fun withdrawMember(@AuthenticationPrincipal memberPrincipal: MemberPrincipal): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(memberService.withdrawMember(memberPrincipal.id))
    }

    @PostMapping("/{memberId}/refresh")
    fun refresh(@AuthenticationPrincipal memberPrincipal: MemberPrincipal, @RequestBody refreshRequest: RefreshRequest): ResponseEntity<LoginResponse> {
        return ResponseEntity.ok(memberService.refreshToken(memberPrincipal.id, refreshRequest))
    }


    @GetMapping
    fun getMemberList(): ResponseEntity<List<MemberResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.getMemberList())
    }

    @GetMapping("/{memberId}")
    fun getMember(
        @PathVariable memberId: Long
    ): ResponseEntity<MemberResponse?> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.getMember(memberId))
    }

}

