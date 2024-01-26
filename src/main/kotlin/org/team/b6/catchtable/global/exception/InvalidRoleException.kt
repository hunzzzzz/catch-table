package org.team.b6.catchtable.global.exception

class InvalidRoleException(value: String) :
    RuntimeException(
        when (value) {
            "Add Review" -> "해당 식당에 예약 이력이 없습니다."
            "Update Review" -> "해당 리뷰의 작성자만 리뷰 수정이 가능합니다."
            "Delete Review" -> "해당 리뷰의 작성자만 리뷰 삭제가 가능합니다."
            else -> ""
        }
    )