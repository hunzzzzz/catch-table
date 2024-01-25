package org.team.b6.catchtable.global.exception

class InvalidStoreSearchingValuesException(value: String) :
    RuntimeException(
        when (value) {
            "category" -> "유효하지 않은 카테고리입니다."
            "criteria" -> "잘못된 검색 조건입니다."
            else -> "잘못된 요청입니다."
        }
    )