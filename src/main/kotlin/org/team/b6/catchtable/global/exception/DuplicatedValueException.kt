package org.team.b6.catchtable.global.exception

class DuplicatedValueException(value: String) :
    RuntimeException("이미 존재하는 $value 입니다.")
