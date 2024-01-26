package org.team.b6.catchtable.domain.store.model

import jakarta.persistence.*
import org.team.b6.catchtable.domain.store.dto.request.StoreRequest
import org.team.b6.catchtable.global.entity.BaseEntity

@Entity
@Table(name = "Stores")
class Store(
    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "belong_to", nullable = false)
    var belongTo: Long,

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    var category: StoreCategory,

    @Column(name = "description", nullable = false)
    var description: String,

    @Column(name = "phone", nullable = false)
    var phone: String,

    @Column(name = "address", nullable = false)
    var address: String,

    @Column(name = "open_time", nullable = false)
    var openTime: Int,

    @Column(name = "close_time", nullable = false)
    var closeTime: Int,

    @JoinColumn(name = "time_table_id")
    @OneToOne(targetEntity = TimeTable::class, fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var timeTable: TimeTable
) : BaseEntity() {
    @Id
    @Column(name = "store_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: StoreStatus = StoreStatus.WAITING_FOR_CREATE

    fun update(request: StoreRequest) {
        this.name = request.name
        this.category = StoreCategory.valueOf(request.category)
        this.description = request.description
        this.phone = request.phone
        this.address = request.address
    }

    fun updateStatus(status: StoreStatus) {
        this.status = status
    }

    fun updateForDelete() {
        this.name = "영업 종료된 식당"
        this.description = "영업 종료된 식당입니다."
        updateStatus(StoreStatus.DELETED)
    }
}