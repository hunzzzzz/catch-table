//package org.team.b6.catchtable.domain.store.model
//
//import jakarta.persistence.*
//import org.team.b6.catchtable.domain.store.dto.request.TimeTableRequest
//import org.team.b6.catchtable.global.entity.BaseEntity
//
//@Entity
//@Table(name = "TimeTables")
//class TimeTable(
//    @Column(name = "time_8")
//    var time8: Int? = null,
//
//    @Column(name = "time_9")
//    var time9: Int? = null,
//
//    @Column(name = "time_10")
//    var time10: Int? = null,
//
//    @Column(name = "time_11")
//    var time11: Int? = null,
//
//    @Column(name = "time_12")
//    var time12: Int? = null,
//
//    @Column(name = "time_13")
//    var time13: Int? = null,
//
//    @Column(name = "time_14")
//    var time14: Int? = null,
//
//    @Column(name = "time_15")
//    var time15: Int? = null,
//
//    @Column(name = "time_16")
//    var time16: Int? = null,
//
//    @Column(name = "time_17")
//    var time17: Int? = null,
//
//    @Column(name = "time_18")
//    var time18: Int? = null,
//
//    @Column(name = "time_19")
//    var time19: Int? = null,
//
//    @Column(name = "time_20")
//    var time20: Int? = null,
//
//    @Column(name = "time_21")
//    var time21: Int? = null,
//
//    @Column(name = "time_22")
//    var time22: Int? = null,
//
//    @Column(name = "time_23")
//    var time23: Int? = null,
//) : BaseEntity() {
//    @Id
//    @Column(name = "time_table_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    val id: Long? = null
//
//    fun update(request: TimeTableRequest) {
//        this.time8 = request.time8
//        this.time9 = request.time9
//        this.time10 = request.time10
//        this.time11 = request.time11
//        this.time12 = request.time12
//        this.time13 = request.time13
//        this.time14 = request.time14
//        this.time15 = request.time15
//        this.time16 = request.time16
//        this.time17 = request.time17
//        this.time18 = request.time18
//        this.time19 = request.time19
//        this.time20 = request.time20
//        this.time21 = request.time21
//        this.time22 = request.time22
//        this.time23 = request.time23
//    }
//}