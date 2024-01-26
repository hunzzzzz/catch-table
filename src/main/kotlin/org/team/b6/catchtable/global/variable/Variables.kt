package org.team.b6.catchtable.global.variable

object Variables {
    val CRITERIA_LIST = mutableListOf("name")
    val BANNED_WORD_LIST = mutableListOf("나쁜 말", "진짜 나쁜 말", "엄마 사랑해요")

    const val MAIL_SUBJECT_ACCEPTED = "[캐치테이블] 사장님의 요청이 정상적으로 승인되었습니다."
    const val MAIL_SUBJECT_REFUSED = "[캐치테이블] 사장님의 요청이 거절되었습니다."
    const val MAIL_CONTENT_STORE_CREATE_ACCEPTED = "식당 등록이 완료되었습니다. 캐치 테이블의 소중한 일원이 되신 것을 환영합니다!"
    const val MAIL_CONTENT_STORE_DELETE_ACCEPTED = "식당 삭제가 완료되었습니다. 다음에 또 볼 수 있는 기회가 있었으면 좋겠습니다ㅠㅠ"
    const val MAIL_CONTENT_STORE_CREATE_REFUSED = "식당 등록이 거절되었습니다. 자세한 사유는 고객센터(1234-5678)로 연락주세요."
    const val MAIL_CONTENT_STORE_DELETE_REFUSED = "식당이 정상적으로 삭제되지 않았습니다. 자세한 사유는 고객센터(1234-5678)을 통해 확인해주세요."
}