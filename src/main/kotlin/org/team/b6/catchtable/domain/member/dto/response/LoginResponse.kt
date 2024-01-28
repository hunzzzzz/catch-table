package org.team.b6.catchtable.domain.member.dto.response

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
)