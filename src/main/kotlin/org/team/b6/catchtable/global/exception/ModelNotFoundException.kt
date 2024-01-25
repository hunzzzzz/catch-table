package org.team.b6.catchtable.global.exception

class ModelNotFoundException(value: String) :
    RuntimeException("주어진 ID로 ${value}을 찾을 수 없습니다.")
