package org.team.b6.catchtable.domain.member.dto.request

data class LoginRequest(
    val email: String,
    val password: String,
    val role: String
)
