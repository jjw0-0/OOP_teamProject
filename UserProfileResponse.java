package com.project.app.dto;

import java.util.List;

import com.project.app.model.Payment;

/**
 * 사용자 프로필 정보 응답 DTO
 *
 * 목적: 사용자 프로필 정보 (결제 내역 포함)
 *
 * 사용 시점: UserService -> Controller(or View) -> MyPageView
 *
 * 필드:
 *   - userId (String)
 *   - name (String)
 *   - birthDate (String)
 *   - grade (int)
 *   - enrolledLectureCount (int) - 수강 중인 강의 수
 *   - totalPaymentAmount (int) - 총 결제 금액 (음수 합계)
 *   - recentPayments (List<Payment>) - 최근 결제 내역 3개
 */
public class UserProfileResponse {

    private final String userId;
    private final String name;
    private final String birthDate;
    private final int grade;
    private final int enrolledLectureCount;
    private final int totalPaymentAmount;
    private final List<Payment> recentPayments;

    public UserProfileResponse(String userId,
                               String name,
                               String birthDate,
                               int grade,
                               int enrolledLectureCount,
                               int totalPaymentAmount,
                               List<Payment> recentPayments) {
        this.userId = userId;
        this.name = name;
        this.birthDate = birthDate;
        this.grade = grade;
        this.enrolledLectureCount = enrolledLectureCount;
        this.totalPaymentAmount = totalPaymentAmount;
        this.recentPayments = recentPayments;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public int getGrade() {
        return grade;
    }

    public int getEnrolledLectureCount() {
        return enrolledLectureCount;
    }

    public int getTotalPaymentAmount() {
        return totalPaymentAmount;
    }

    public List<Payment> getRecentPayments() {
        return recentPayments;
    }
}
