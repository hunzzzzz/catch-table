package org.team.b6.catchtable.domain.review.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team.b6.catchtable.domain.review.dto.request.ReviewRequest
import org.team.b6.catchtable.domain.review.dto.response.ReviewResponse
import org.team.b6.catchtable.domain.review.model.Review
import org.team.b6.catchtable.domain.review.repository.ReviewRepository
import org.team.b6.catchtable.global.exception.EtiquetteException
import org.team.b6.catchtable.global.exception.InvalidRoleException
import org.team.b6.catchtable.global.security.MemberPrincipal
import org.team.b6.catchtable.global.service.GlobalService
import org.team.b6.catchtable.global.variable.Variables

@Service
@Transactional
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val globalService: GlobalService
) {
    // 전체 리뷰 조회
    fun findReviews(storeId: Long) =
        globalService.getAllReviews().filter { it.store.id == storeId }

    // 단일 리뷰 조회
    fun findReview(storeId: Long, reviewId: Long) =
        ReviewResponse.from(globalService.getReview(reviewId))

    // 리뷰 등록
    fun addReview(memberPrincipal: MemberPrincipal, storeId: Long, request: ReviewRequest) {
        availableToAddComment(memberPrincipal.id, storeId)
        validateContent(request.content)
        reviewRepository.save(
            request.to(
                member = globalService.getMember(request.memberId),
                store = globalService.getStore(storeId)
            )
        ).id
    }

    // 리뷰 수정
    fun updateReview(memberPrincipal: MemberPrincipal, storeId: Long, reviewId: Long, request: ReviewRequest) {
        validateContent(request.content)
        globalService.getReview(reviewId).let {
            availableToUpdateComment(it, memberPrincipal.id)
            it.update(request)
        }
    }

    // 리뷰 삭제
    fun deleteReview(memberPrincipal: MemberPrincipal, storeId: Long, reviewId: Long) =
        globalService.getReview(reviewId).let {
            availableToDeleteComment(it, memberPrincipal.id)
            reviewRepository.delete(it)
        }

    // 리뷰 내용에 욕설이 포함된 경우 등록 불가
    private fun validateContent(content: String) {
        if (Variables.BANNED_WORD_LIST.any { content.contains(it) })
            throw EtiquetteException()
    }

    // 리뷰 등록이 가능한지 확인 (해당 식당에 예약 이력이 있는지)
    private fun availableToAddComment(memberId: Long, storeId: Long) {
        if (!globalService.getAllReservations().any { it.store.id == storeId && it.member.id == memberId })
            throw InvalidRoleException("Add Review")
    }

    // 리뷰 수정이 가능한지 확인 (해당 리뷰를 본인이 작성했는지)
    private fun availableToUpdateComment(review: Review, memberId: Long) {
        if (review.member.id != memberId) throw InvalidRoleException("Update Review")
    }

    // 리뷰 수정이 가능한지 확인 (해당 리뷰를 본인이 작성했는지)
    private fun availableToDeleteComment(review: Review, memberId: Long) {
        if (review.member.id != memberId) throw InvalidRoleException("Delete Review")
    }
}