package com.project.app.dto;

/**
 * 사용자 프로필 정보 응답 DTO
 *
 * 목적: 사용자 프로필 정보 (결제 내역 포함)
 *
 * 사용 시점: UserService -> Controller -> MyPageView
 *
 * 필드:
 *   - userId (String)
 *   - name (String)
 *   - birthDate (String)
 *   - grade (int)
 *   - enrolledLectureCount (int) - 수강 중인 강의 수
 *   - totalPaymentAmount (int) - 총 결제 금액
 *   - recentPayments (List<Payment>) - 최근 결제 내역 3개
 *
 * 이유: User 엔티티 + 통계 정보(강의 수, 총 금액) 조합. 계산된 값 포함.
 */
public class UserProfileResponse {

}
