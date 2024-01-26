package org.team.b6.catchtable.domain.review.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team.b6.catchtable.domain.member.repository.MemberRepository
import org.team.b6.catchtable.domain.review.dto.request.ReviewRequest
import org.team.b6.catchtable.domain.review.dto.response.ReviewResponse
import org.team.b6.catchtable.domain.review.repository.ReviewRepository
import org.team.b6.catchtable.domain.store.repository.StoreRepository
import org.team.b6.catchtable.global.exception.EtiquetteException
import org.team.b6.catchtable.global.service.GlobalService
import org.team.b6.catchtable.global.variable.Variables

@Service
@Transactional
class ReviewService(
    private val memberRepository: MemberRepository,
    private val storeRepository: StoreRepository,
    private val reviewRepository: ReviewRepository,
    private val globalService: GlobalService
) {
    fun findReviews(storeId: Long) =
        reviewRepository.findAll().filter { it.store.id == storeId }

    fun findReview(storeId: Long, reviewId: Long) =
        ReviewResponse.from(globalService.getReview(reviewId))

    fun addReview(storeId: Long, request: ReviewRequest) {
        validateContent(request.content)
        reviewRepository.save(
            request.to(
                member = globalService.getMember(request.memberId),
                store = globalService.getStore(storeId)
            )
        ).id
    }

    private fun validateContent(content: String){
        if (Variables.BANNED_WORD_LIST.any { content.contains(it) })
            throw EtiquetteException()
    }

    fun updateReview(storeId: Long, reviewId: Long, request: ReviewRequest) =
        globalService.getReview(reviewId).update(request)

    fun deleteReview(storeId: Long, reviewId: Long) =
        reviewRepository.deleteById(reviewId)

    private fun validate(memberId: Long, storeId: Long, reviewId: Long) =
        globalService.getReview(reviewId).let {
            if (it.member.id != memberId) throw Exception("")
            else if (it.store.id != storeId) throw Exception("")
        } // TODO: 추후 memberId를 받는 로직이 추가되면 validate 구현

    private fun validateAndGetReview(memberId: Long, storeId: Long, reviewId: Long) =
        globalService.getReview(reviewId).let {
            if (it.member.id != memberId) throw Exception("")
            else if (it.store.id != storeId) throw Exception("")
            it
        } // TODO: 추후 memberId를 받는 로직이 추가되면 validate 구현
}