package org.team.b6.catchtable.domain.store.model

import jakarta.persistence.*
import org.team.b6.catchtable.global.entity.BaseEntity

@Entity
@Table(name = "Stores")
class Store(
    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    var category: StoreCategory,

    @Column(name = "description", nullable = false)
    var description: String,

    @Column(name = "phone", nullable = false)
    var phone: String,

    @Column(name = "address", nullable = false)
    var address: String
) : BaseEntity() {
    @Id
    @Column(name = "store_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun update(name: String, category: StoreCategory, description: String, phone: String, address: String) {
        // this = 객체 자신을 의미!! (여기서 this는 (기존의) store 객체를 의미한다.)
        this.name = name
        this.category = category
        this.description = description
        this.phone = phone
        this.address = address
    }
}