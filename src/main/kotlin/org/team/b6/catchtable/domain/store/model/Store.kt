package org.team.b6.catchtable.domain.store.model

import jakarta.persistence.*
import org.team.b6.catchtable.domain.store.dto.request.StoreRequest
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

    fun update(request: StoreRequest) {
        this.name = request.name
        this.category = StoreCategory.valueOf(request.category)
        this.description = request.description
        this.phone = request.phone
        this.address = request.address
    }
}