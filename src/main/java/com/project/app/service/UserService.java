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

    /**
     * 사용자 프로필 + 통계 정보 조회
     *
     * - 총 결제금액
     * - 수강 강의 수
     * - 최근 결제 3건
     */
    public UserProfileResponse getUserProfile(User user) {
        if (user == null) {
            return null;
        }

        // 1. 결제 내역 조회
        List<Payment> payments = paymentRepository.findByUserId(user.getId());

        // 2. 총 결제 금액
        int totalAmount = 0;
        for (Payment p : payments) {
            totalAmount += p.getAmount();
        }

        // 3. 수강 강의 수 (결제 기록 기준, 강의ID 중복 제거)
        //    Payment.getLectureId()가 String이므로 String Set으로 중복 제거
        Set<String> lectureIds = new HashSet<>();
        for (Payment p : payments) {
            String lecId = p.getLectureId();   // 예: "L027"
            if (lecId != null && !lecId.isBlank()) {
                lectureIds.add(lecId);
            }
        }
        int enrolledLectureCount = lectureIds.size();

        // 4. 최근 결제 내역 3개 (마지막 3건)
        List<Payment> recent = new ArrayList<>();
        int start = Math.max(0, payments.size() - 3);
        for (int i = start; i < payments.size(); i++) {
            recent.add(payments.get(i));
        }

        // 5. 생년월일 정리
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
     * - Payment.userId 일치
     * - Payment의 lectureId(String, 예: "L027")를 숫자로 변환해서 Lecture 조회
     */
    public List<MyLectureView> getMyLectures(User user) {
        List<MyLectureView> result = new ArrayList<>();
        if (user == null) {
            return result;
        }

        // 1. 결제 내역 기반으로 강의 ID 수집
        List<Payment> payments = paymentRepository.findByUserId(user.getId());
        Set<Integer> lectureIds = new HashSet<>();

        for (Payment p : payments) {
            String lectureIdStr = p.getLectureId();  // "L027" 형태
            int lecId = extractNumericId(lectureIdStr);  // 27 같은 숫자로 변환
            if (lecId > 0) {
                lectureIds.add(lecId);
            }
        }

        // 2. 강의 ID → Lecture 조회 → MyLectureView로 변환
        for (Integer numericLecId : lectureIds) {
            Lecture lecture = lectureRepository.findById(numericLecId);  // 숫자 ID로 조회
            if (lecture != null) {
                String nextScheduleDate = "다음 수업일 계산 예정";

                result.add(new MyLectureView(
                        numericLecId,               // int lectureId
                        lecture.getTitle(),
                        lecture.getInstructor(),
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

    /**
     * "L027" → 27 같이 문자열 ID에서 숫자 부분만 추출
     * 숫자 부분이 없거나 파싱 실패하면 0 반환
     */
    private int extractNumericId(String lectureId) {
        if (lectureId == null) return 0;
        String digits = lectureId.replaceAll("\\D", ""); // 숫자만 남김
        if (digits.isEmpty()) return 0;
        try {
            return Integer.parseInt(digits);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
