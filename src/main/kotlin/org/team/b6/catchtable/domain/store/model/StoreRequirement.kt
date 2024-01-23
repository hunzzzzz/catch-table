package org.team.b6.catchtable.domain.store.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "StoreRequirements")
class StoreRequirement(
    @Column(name = "requirement", nullable = false)
    @Enumerated(EnumType.STRING)
    val requirement: StoreRequirementCategory,

    @Column(name = "is_accepted", nullable = false)
    var isAccepted: Boolean = false,

    @Column(name = "name", nullable = false)
    val name: String? = null,

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    val category: StoreCategory? = null,

    @Column(name = "description", nullable = false)
    val description: String? = null,

    @Column(name = "phone", nullable = false)
    val phone: String? = null,

    @Column(name = "address", nullable = false)
    val address: String? = null,

    @ManyToOne
    @JoinColumn(name = "store_id")
    val store: Store?,

    @Column(name = "created_at", columnDefinition = "TIMESTAMP(6)", nullable = false, updatable = false)
    val createdAt: LocalDateTime
) {
    @Id
    @Column(name = "store_requirement_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}