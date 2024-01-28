package org.team.b6.catchtable.global.variable

object Variables {
    val BANNED_WORD_LIST = mutableListOf("나쁜 말", "진짜 나쁜 말", "엄마 사랑해요")

    const val MAIL_SUBJECT_ACCEPTED = "[캐치테이블] 사장님의 요청이 정상적으로 승인되었습니다."
    const val MAIL_SUBJECT_REFUSED = "[캐치테이블] 사장님의 요청이 아쉽게도 거절되었습니다."
    const val MAIL_SUBJECT_WARN = "[캐치테이블] 회원님이 작성한 리뷰가 삭제 처리되었습니다."
    const val MAIL_SUBJECT_BANNED = "[캐치테이블] 회원님의 계정이 7일 간 사용 정지됩니다."

    const val MAIL_CONTENT_STORE_CREATE_ACCEPTED = "식당 등록이 완료되었습니다. 캐치 테이블의 소중한 일원이 되신 것을 환영합니다!"
    const val MAIL_CONTENT_STORE_DELETE_ACCEPTED = "식당 삭제가 완료되었습니다. 다음에 또 볼 수 있는 기회가 있었으면 좋겠습니다ㅠㅠ"
    const val MAIL_CONTENT_STORE_CREATE_REFUSED = "식당 등록이 거절되었습니다. 자세한 사유는 고객센터(1234-5678)로 연락주세요."
    const val MAIL_CONTENT_STORE_DELETE_REFUSED = "식당이 정상적으로 삭제되지 않았습니다. 자세한 사유는 고객센터(1234-5678)을 통해 확인해주세요."

    const val MAIL_CONTENT_REVIEW_DELETE_ACCEPTED = "리뷰 삭제가 완료되었습니다. 해당 유저에게 경고 1회가 부여되었고, 경고 3회 누적 시 애플리케이션 이용이 제한될 예정입니다."
    const val MAIL_CONTENT_REVIEW_DELETE_REFUSED = "리뷰 삭제 요청이 거절되었습니다. 자세한 사유는 고객센터(1234-5678)로 연락주세요."
    const val MAIL_CONTENT_REVIEW_WARN = "회원님이 아래와 같이 작성한 리뷰가 악의성 리뷰로 판단되어 삭제 처리되었습니다. 회원님께는 경고 1회가 부여되며, 경고 3회 누적 시 애플리케이션 이용이 제한됩니다."

    const val MAIL_CONTENT_BANNED = "캐치테이블 운영 정책 3회 위반으로 인해 회원님의 계정이 7일 간 애플리케이션 사용이 정지됩니다. 자세한 문의는 고객센터(1234-5678)를 통해 연락주세요."
}