package org.team.b6.catchtable.domain.store.model

import jakarta.persistence.*
import org.team.b6.catchtable.global.entity.BaseEntity

@Entity
@Table(name = "Stores")
class Store(
    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    val category: StoreCategory,

    @Column(name = "description", nullable = false)
    val description: String,

    @Column(name = "phone", nullable = false)
    val phone: String,

    @Column(name = "address", nullable = false)
    val address: String
) : BaseEntity() {
    @Id
    @Column(name = "store_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}