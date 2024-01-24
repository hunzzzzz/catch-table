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

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "store_id")
    val store: Store? = null,

    @Column(name = "require_to")
    val requireTo: Long? = null, // UPDATE, DELETE 시 대상이 되는 Store의 Id (FK가 아님을 구분하기 위해 requireTo라는 컬럼명 사용)

    @Column(name = "created_at", columnDefinition = "TIMESTAMP(6)", nullable = false, updatable = false)
    val createdAt: LocalDateTime
) {
    @Id
    @Column(name = "store_requirement_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}