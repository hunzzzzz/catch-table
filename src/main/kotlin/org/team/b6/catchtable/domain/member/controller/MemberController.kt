package org.team.b6.catchtable.domain.member.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.team.b6.catchtable.domain.member.dto.response.MemberResponse
import org.team.b6.catchtable.domain.member.service.MemberService

@RequestMapping("/members")
class MemberController(
    val memberService: MemberService
) {


    fun signup(){
        TODO()
    }

    fun login(){
        TODO()
    }

    fun emailCheck(){
        TODO()
    }

    fun signupCheck(){
        TODO()
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