package org.team.b6.catchtable.domain.review.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team.b6.catchtable.domain.review.dto.request.ReviewRequest
import org.team.b6.catchtable.domain.review.dto.response.ReviewResponse
import org.team.b6.catchtable.domain.review.model.Review
import org.team.b6.catchtable.domain.review.model.ReviewStatus
import org.team.b6.catchtable.domain.review.repository.ReviewRepository
import org.team.b6.catchtable.domain.store.model.StoreStatus
import org.team.b6.catchtable.global.exception.BannedUserException
import org.team.b6.catchtable.global.exception.EtiquetteException
import org.team.b6.catchtable.global.exception.InvalidRoleException
import org.team.b6.catchtable.global.exception.StoreRequirementDeniedException
import org.team.b6.catchtable.global.security.MemberPrincipal
import org.team.b6.catchtable.global.util.EntityFinder
import org.team.b6.catchtable.global.variable.Variables

@Service
@Transactional
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val entityFinder: EntityFinder
) {
    // 전체 리뷰 조회
    fun findReviews(storeId: Long) =
        entityFinder.getAllReviews().filter { it.store.id == storeId }.map { ReviewResponse.from(it) }

    // 단일 리뷰 조회
    fun findReview(storeId: Long, reviewId: Long) =
        ReviewResponse.from(entityFinder.getReview(reviewId))

    // 리뷰 등록
    fun addReview(memberPrincipal: MemberPrincipal, storeId: Long, request: ReviewRequest) {
        availableToAddComment(memberPrincipal.id, storeId)
        validateContent(request.content)
        reviewRepository.save(
            request.to(
                member = entityFinder.getMember(memberPrincipal.id),
                store = entityFinder.getStore(storeId)
            )
        ).id
    }

    // 리뷰 수정
    fun updateReview(memberPrincipal: MemberPrincipal, storeId: Long, reviewId: Long, request: ReviewRequest) {
        validateContent(request.content)
        entityFinder.getReview(reviewId).let {
            availableToUpdateComment(it, memberPrincipal.id)
            it.update(request)
        }
    }

    // 리뷰 삭제
    fun deleteReview(memberPrincipal: MemberPrincipal, storeId: Long, reviewId: Long) =
        entityFinder.getReview(reviewId).let {
            availableToDeleteComment(it, memberPrincipal.id)
            reviewRepository.delete(it)
        }

    // 리뷰 삭제 요청 (OWNER)
    fun requireForDeleteReview(memberPrincipal: MemberPrincipal, storeId: Long, reviewId: Long) {
        entityFinder.getReview(reviewId).let {
            validateOwner(it, memberPrincipal.id)
            it.updateStatus(ReviewStatus.REQUIRED_FOR_DELETE)
        }
    }

    // 리뷰 내용에 욕설이 포함된 경우 등록 불가
    private fun validateContent(content: String) {
        if (Variables.BANNED_WORD_LIST.any { content.contains(it) })
            throw EtiquetteException()
    }

    // 리뷰 등록이 가능한지 확인 (해당 식당에 예약 이력이 있는지 + 식당이 예약 가능한 상태인지 + 계정 정지 여부 확인)
    private fun availableToAddComment(memberId: Long, storeId: Long) {
        if (!entityFinder.getAllReservations().any { it.store.id == storeId && it.member.id == memberId })
            throw InvalidRoleException("Add Review")
        else if (entityFinder.getStore(storeId).status != StoreStatus.OK)
            throw StoreRequirementDeniedException("review")
        else if (entityFinder.getMember(memberId).bannedExpiration != null)
            throw BannedUserException(entityFinder.getMember(memberId).bannedExpiration!!)
    }

    // 리뷰 수정이 가능한지 확인 (해당 리뷰를 본인이 작성했는지)
    private fun availableToUpdateComment(review: Review, memberId: Long) {
        if (review.member.id != memberId) throw InvalidRoleException("Update Review")
    }

    // 리뷰 수정이 가능한지 확인 (해당 리뷰를 본인이 작성했는지)
    private fun availableToDeleteComment(review: Review, memberId: Long) {
        if (review.member.id != memberId) throw InvalidRoleException("Delete Review")
    }

    // 리뷰 삭제 요청이 가능한지 확인 (요청의 주체가 리뷰가 달린 식당의 주인인지)
    private fun validateOwner(review: Review, memberId: Long) {
        if (review.store.belongTo != memberId) throw InvalidRoleException("Require for Delete Review")
    }
}