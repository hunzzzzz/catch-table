package org.team.b6.catchtable.global.exception

class ModelNotFoundException(value: String) :
    RuntimeException(
        when (value) {
            "modelName" -> "모델을 찾을 수 없습니다."
            "id" -> "주어진 ID로 모델을 찾을 수 없습니다."
            else -> "잘못된 요청입니다."
        }
    )
