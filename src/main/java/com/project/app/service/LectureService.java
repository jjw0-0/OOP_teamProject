package com.project.app.service;

import com.project.app.dto.EnrollLectureRequest;
import com.project.app.dto.EnrollLectureResponse;
import com.project.app.model.Lecture;
import com.project.app.repository.LectureRepositoryImpl;
import com.project.app.repository.UserRepositoryImpl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// 강의 신청 여부
public class LectureService{
    private final LectureRepositoryImpl lectureRepository;
    private final UserRepositoryImpl userRepository;


    public LectureService(){
        lectureRepository=new LectureRepositoryImpl();
        userRepository=new UserRepositoryImpl();
    }
    // (repository를 받는 생성자)
    public LectureService(LectureRepositoryImpl lectureRepository) {
        this.lectureRepository = lectureRepository;
        this.userRepository = new UserRepositoryImpl();
    }
    // 전체 강의 리스트 조회
    public List<Lecture> getLectures(String subject, String grade, String academy,
                                     String sortBy, String keyword) {
        // 1. Repository에서 전체 강의 가져오기
        List<Lecture> lectures = lectureRepository.findAll();

        // 2. 과목 필터링
        if (subject != null && !subject.equals("전체")) {
            lectures = sortLecturesBySubject(lectures, subject);
        }

        // 3. 학년 필터링
        if (grade != null && !grade.contains("전체")) {
            lectures = filterByGrade(lectures, grade);
        }

        // 4. 학원 필터링
        if (academy != null && !academy.equals("전체")) {
            lectures = filterByAcademy(lectures, academy);
        }

        // 5. 검색 키워드 필터링
        if (keyword != null && !keyword.isEmpty()) {
            lectures = searchLectures(lectures, keyword);
        }

        // 6. 정렬
        lectures = sortLectures(lectures, sortBy);

        return lectures;
    }

    private List<Lecture> sortLectures(List<Lecture> lectures, String sortBy) {
        switch (sortBy) {
            case "평점순":
                return lectures.stream()
                        .sorted(Comparator.comparing(Lecture::getRating).reversed())
                        .collect(Collectors.toList());

            default:
                return lectures; // 기본: 정렬하지 않음
        }
    }

    /**
     * getLecture의 별칭 메서드 (Controller에서 사용)
     */
    public Lecture getLectureById(String lectureId) {
        return getLecture(lectureId);
    }

    /**
     * 과목별 강의 정렬
     *
     * @param lectures 강의 목록
     * @param subject 과목
     * @return 해당 과목의 강의만 포함된 리스트
     */
    public List<Lecture> sortLecturesBySubject(List<Lecture> lectures, String subject) {
        return lectures.stream()
                .filter(lecture -> lecture.getSubject().equals(subject))
                .collect(Collectors.toList());
    }

    /**
     * 학년별 강의 필터링
     *
     * @param lectures 강의 목록
     * @param grade    학년
     * @return 해당 학년의 강의만 포함된 리스트
     */
    private List<Lecture> filterByGrade(List<Lecture> lectures, String grade) {
        return lectures.stream()
                .filter(lecture -> {
                    String lectureGrade = lecture.getGrade();

                    // "고3/N수" 버튼 클릭 시 -> "고3" 또는 "N수" 포함된 강의 필터링
                    if (grade.equals("고3/N수")) {
                        return lectureGrade.contains("고3") || lectureGrade.contains("N수");
                    }

                    // 일반 학년 필터링 (고1, 고2 등) - 포함 여부로 검색
                    return lectureGrade.contains(grade);
                })
                .collect(Collectors.toList());
    }

    /**
     * 학원별 강의 필터링
     *
     * @param lectures 강의 목록
     * @param academy 학원
     * @return 해당 학원의 강의만 포함된 리스트
     */
    private List<Lecture> filterByAcademy(List<Lecture> lectures, String academy) {
        return lectures.stream()
                .filter(lecture -> lecture.getAcademy().equals(academy))
                .collect(Collectors.toList());
    }

    /**
     * 검색 키워드로 강의 필터링
     *
     * 기능:
     * - 강의명, 강사명에서 키워드 검색
     * - 대소문자 구분 없이 검색
     *
     * @param lectures 강의 목록
     * @param keyword 검색 키워드
     * @return 키워드에 매칭되는 강의 리스트
     */
    public List<Lecture> searchLectures(List<Lecture> lectures, String keyword) {
        String lowerKeyword = keyword.toLowerCase();

        return lectures.stream()
                .filter(lecture ->
                        lecture.getTitle().toLowerCase().contains(lowerKeyword) ||        // ✅ 이미 맞음
                                lecture.getInstructor().toLowerCase().contains(lowerKeyword)  // ✅ 이미 맞음
                )
                .collect(Collectors.toList());
    }

    // 강의 신청 - 학생의 신청 과목에 해당 강의가 없는 경우 신청 가능
    // 유저가 신청 강의 리스트를 가지고 있음
    public EnrollLectureResponse enrollLecture(EnrollLectureRequest request) {
        String userId = request.getUserId();
        String lecId = request.getLectureId();

        // 이미 신청된 강의인 경우
        if(userRepository.checkEnrolled(userId, lecId)) {
            return EnrollLectureResponse.failure("이미 신청된 강의입니다.");
        }

        // 강의 존재 여부 확인
        Lecture lecture = lectureRepository.findById(lecId);
        if(lecture == null) {
            return EnrollLectureResponse.failure("존재하지 않는 강의입니다.");
        }

        // 정원 초과인 경우
        if(lecture.isFull()){
            return EnrollLectureResponse.failure("정원이 초과되었습니다.");
        }

        // 실제 신청 처리
        lecture.addEnrolled();
        userRepository.enrollLecture(userId, lecId);

        return EnrollLectureResponse.success();
    }

    public Lecture getLecture(String lectureId) {
        return lectureRepository.findById(lectureId);
    }

    /**
     * 강의의 평균 평점을 반환
     *
     * @param lectureId 강의 ID
     * @return 평균 평점 (강의가 없으면 0.0)
     */
    public double getLectureRating(String lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId);

        if (lecture != null) {
            return lecture.getRating();
        }

        return 0.0;
    }

    /**
     * 강사별 강의 목록 조회
     *
     * @param instructor 강사명
     * @return 해당 강사의 강의 목록
     */
    public List<Lecture> getLecturesByInstructor(String instructor) {
        List<Lecture> lectures = lectureRepository.findAll();

        return lectures.stream()
                .filter(lecture -> lecture.getInstructor().equals(instructor))  // ✅ 이미 맞음
                .collect(Collectors.toList());
    }

    /**
     * 추천 강의 조회 (평점 4.0 이상)
     *
     * @return 추천 강의 목록
     */
    public List<Lecture> getRecommendedLectures() {
        List<Lecture> lectures = lectureRepository.findAll();

        return lectures.stream()
                .filter(lecture -> lecture.getRating() >= 4.0)
                .sorted(Comparator.comparing(Lecture::getRating).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 과목별 평균 평점 계산
     *
     * @param subject 과목
     * @return 해당 과목 강의들의 평균 평점
     */
    public double getAverageRatingBySubject(String subject) {
        List<Lecture> lectures = lectureRepository.findAll();

        return lectures.stream()
                .filter(lecture -> lecture.getSubject().equals(subject))
                .mapToDouble(Lecture::getRating)
                .average()
                .orElse(0.0);
    }
}



