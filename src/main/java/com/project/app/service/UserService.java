package com.project.app.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.project.app.dto.MyLectureView;
import com.project.app.dto.UserProfileResponse;
import com.project.app.model.Lecture;
import com.project.app.model.Payment;
import com.project.app.model.User;
import com.project.app.repository.LectureRepository;
import com.project.app.repository.LectureRepositoryImpl;
import com.project.app.repository.PaymentRepository;
import com.project.app.repository.PaymentRepositoryImpl;

/**
 * 사용자 관련 조회 서비스
 *
 * 역할:
 * - 마이페이지에 필요한 데이터(프로필, 내 강의, 결제내역 등)를 조합해서 제공
 */
public class UserService {

    private final PaymentRepository paymentRepository;
    private final LectureRepository lectureRepository;

    public UserService() {
        this.paymentRepository = PaymentRepositoryImpl.getInstance();
        this.lectureRepository = LectureRepositoryImpl.getInstance();
    }

    // 사용자 프로필 + 통계 정보 조회
    public UserProfileResponse getUserProfile(User user) {
        if (user == null) {
            return null;
        }

        List<Payment> payments = paymentRepository.findByUserId(user.getId());

        // 총 결제 금액 (음수 합계)
        int totalAmount = 0;
        for (Payment p : payments) {
            totalAmount += p.getAmount();
        }

        // 수강 강의 수 (LECTURE 타입 결제 기준, 중복 제거)
        Set<Integer> lectureIds = new HashSet<>();
        for (Payment p : payments) {
            if ("LECTURE".equalsIgnoreCase(p.getType()) && p.getLectureId() > 0) {
                lectureIds.add(p.getLectureId());
            }
        }
        int enrolledLectureCount = lectureIds.size();

        // 최근 결제 내역 3개만 (뒤에서부터 3개)
        List<Payment> recent = new ArrayList<>();
        int start = Math.max(0, payments.size() - 3);
        for (int i = start; i < payments.size(); i++) {
            recent.add(payments.get(i));
        }

        String birth = user.getBirth();
        if (birth == null || birth.isBlank()) {
            birth = "미등록";
        }

        return new UserProfileResponse(
                user.getId(),
                user.getName(),
                birth,
                user.getGrade(),
                enrolledLectureCount,
                totalAmount,
                recent
        );
    }

    /**
     * 사용자가 수강 중인 강의 목록 조회
     *
     * 기준:
     * - Payment.type = "LECTURE"
     * - userId가 일치하는 결제를 기반으로 Lecture 정보 추출
     */
    public List<MyLectureView> getMyLectures(User user) {
        List<MyLectureView> result = new ArrayList<>();
        if (user == null) {
            return result;
        }

        List<Payment> payments = paymentRepository.findByUserId(user.getId());
        Set<Integer> lectureIds = new HashSet<>();

        for (Payment p : payments) {
            if ("LECTURE".equalsIgnoreCase(p.getType()) && p.getLectureId() > 0) {
                lectureIds.add(p.getLectureId());
            }
        }

        for (Integer lectureId : lectureIds) {
            Lecture lecture = lectureRepository.findById(lectureId);
            if (lecture != null) {
                // nextScheduleDate는 추후 실제 날짜 계산 로직으로 대체 가능
                String nextScheduleDate = "다음 수업일 계산 예정";

                result.add(new MyLectureView(
                        lecture.getId(),
                        lecture.getName(),
                        lecture.getInstructorName(),
                        lecture.getSubject(),
                        lecture.getDayOfWeek(),
                        lecture.getTime(),
                        lecture.getLocation(),
                        nextScheduleDate
                ));
            }
        }

        return result;
    }
}
