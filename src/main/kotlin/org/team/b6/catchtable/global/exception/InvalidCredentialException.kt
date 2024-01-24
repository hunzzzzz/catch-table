package org.team.b6.catchtable.global.exception

class InvalidCredentialException(value: String) :
    RuntimeException(
        when (value) {
            "role" -> "역할이 일치하지 않습니다."
            "password" -> "비밀번호가 일치하지 않습니다."
            else -> "잘못된 요청입니다."
        }
    )
