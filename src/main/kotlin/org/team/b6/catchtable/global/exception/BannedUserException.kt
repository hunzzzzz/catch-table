package org.team.b6.catchtable.global.exception

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BannedUserException(until: LocalDateTime) : RuntimeException(
    "${until.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}까지 계정이 정지되어, 해당 기능을 사용할 수 없습니다."
)