package org.team.b6.catchtable.domain.store.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team.b6.catchtable.domain.store.repository.StoreRepository

@Service
@Transactional
class StoreService(
    private val storeRepository: StoreRepository
) {
}