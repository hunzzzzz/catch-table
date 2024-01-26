package org.team.b6.catchtable.domain.member.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.team.b6.catchtable.domain.member.dto.request.LoginRequest
import org.team.b6.catchtable.domain.member.dto.request.SignupMemberRequest
import org.team.b6.catchtable.domain.member.dto.request.UpdateMemberRequest
import org.team.b6.catchtable.domain.member.dto.response.LoginResponse
import org.team.b6.catchtable.domain.member.dto.response.MemberResponse
import org.team.b6.catchtable.domain.member.service.MemberService

@RestController
@RequestMapping("/members")
class MemberController(
    val memberService: MemberService
) {

    @PostMapping("/signup")
    fun signup(@RequestBody @Valid signupMemberRequest: SignupMemberRequest): ResponseEntity<MemberResponse>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.signUp(signupMemberRequest))
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest ): ResponseEntity<LoginResponse>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.login(loginRequest))
    }

    fun emailCheck(){
        TODO()
    }

    fun signupCheck() {
        TODO()
    }

    @PutMapping("/{memberId}")
    fun updateMember(@PathVariable memberId: Long, @RequestBody updateRequest: UpdateMemberRequest): ResponseEntity<MemberResponse>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.updateMember(updateRequest))
    }

    @GetMapping
    fun getMemberList(): ResponseEntity<List<MemberResponse>>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.getMemberList())
    }

    @GetMapping("/{memberId}")
    fun getMember(
        @PathVariable memberId: Long
    ): ResponseEntity<MemberResponse?>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.getMember(memberId))
    }

    //fun logout(){}

    fun memberWithDrawal(){
        TODO()
    }
}