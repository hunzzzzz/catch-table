package org.team.b6.catchtable.global.exception

class StoreRequirementDeniedException(condition: String) : RuntimeException(
    when (condition) {
        "reservation" -> "현재 예약이 불가능한 식당입니다."
        "review" -> "현재 리뷰를 작성할 수 없는 식당입니다."
        else -> ""
    }
)