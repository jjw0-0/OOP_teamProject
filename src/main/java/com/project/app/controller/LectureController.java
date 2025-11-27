package com.project.app.controller;

import com.project.app.model.Lecture;
import com.project.app.repository.LectureRepositoryImpl;
import com.project.app.service.LectureService;
import com.project.app.view.LecturePageView;

import java.util.List;

public class LectureController {
    private final LecturePageView view;
    private final LectureService service;
    private final LectureRepositoryImpl lectureRepository;

    // 현재 필터 상태 관리
    private String currentSubject = "전체";
    private String currentGrade = "전체";
    private String currentAcademy = "전체";
    private String currentSortBy = "평점순";
    private String currentKeyword = "";

    /**
     * 생성자: View와 Service를 받아서 연결
     *
     * @param view LecturePageView 인스턴스
     * @param service LectureService 인스턴스
     */
    public LectureController(LecturePageView view, LectureService service, LectureRepositoryImpl lectureRepository) {
        this.view = view;
        this.service = service;
        this.lectureRepository=lectureRepository;

        // 이벤트 리스너 등록
        initListeners();

        // 초기 데이터 로드
        loadLectures();
    }

    /**
     * 모든 이벤트 리스너를 한 곳에서 등록
     */
    private void initListeners() {
        // 과목 버튼들 리스너 등록
        String[] subjects = {"국어", "수학", "영어", "사회", "과학", "한국사", "모의고사"};
        for (String subject : subjects) {
            view.addSubjectButtonListener(subject, e -> handleSubjectFilter(subject));
        }

        // 학년 버튼들 리스너 등록
        String[] grades = {"고1", "고2", "고3/N수"};
        for (String grade : grades) {
            view.addGradeButtonListener(grade, e -> handleGradeFilter(grade));
        }

        // 학원 콤보박스 리스너 등록
        view.addAcademyComboListener(e -> handleAcademyFilter());

        // 정렬 콤보박스 리스너 등록
        view.addSortComboListener(e -> handleSort());

        // 검색 버튼 리스너 등록
        view.addSearchButtonListener(e -> handleSearch());

        // 강의 카드 클릭 리스너 등록
        view.addLectureCardClickListener(lectureId -> handleLectureDetail(lectureId));
    }

    /**
     * 과목 필터 처리
     *
     * @param subject 선택된 과목
     */
    private void handleSubjectFilter(String subject) {
        // 1. 현재 상태 업데이트
        this.currentSubject = subject;

        // 2. 로그 출력
        System.out.println("[Controller] 과목 선택: " + subject);

        // 3. Service 호출하여 데이터 로드
        loadLectures();
    }

    /**
     * 학년 필터 처리
     *
     * @param grade 선택된 학년
     */
    private void handleGradeFilter(String grade) {
        // 1. 현재 상태 업데이트
        this.currentGrade = grade;

        // 2. 로그 출력
        System.out.println("[Controller] 학년 선택: " + grade);

        // 3. Service 호출하여 데이터 로드
        loadLectures();
    }

    /**
     * 학원 필터 처리
     */
    private void handleAcademyFilter() {
        // 1. View에서 선택된 학원 가져오기
        String academy = view.getSelectedAcademy();

        // 2. 현재 상태 업데이트
        this.currentAcademy = academy;

        // 3. 로그 출력
        System.out.println("[Controller] 학원 선택: " + academy);

        // 4. Service 호출하여 데이터 로드
        loadLectures();
    }

    /**
     * 정렬 기준 처리
     */
    private void handleSort() {
        // 1. View에서 선택된 정렬 기준 가져오기
        String sortBy = view.getSelectedSort();

        // 2. 현재 상태 업데이트
        this.currentSortBy = sortBy;

        // 3. 로그 출력
        System.out.println("[Controller] 정렬 선택: " + sortBy);

        // 4. Service 호출하여 데이터 로드
        loadLectures();
    }

    /**
     * 검색 처리
     */
    private void handleSearch() {
        // 1. View에서 입력값 가져오기
        String keyword = view.getSearchKeyword();

        // 2. 빈 값 체크
        if (keyword == null || keyword.trim().isEmpty()) {
            view.showMessage("검색어를 입력해주세요.");
            return;
        }

        // 3. 현재 상태 업데이트
        this.currentKeyword = keyword.trim();

        // 4. 로그 출력
        System.out.println("[Controller] 검색: " + currentKeyword);

        // 5. Service 호출하여 데이터 로드
        loadLectures();
    }


    /**
     * 강의 상세 정보 처리
     *
     * @param lectureId 선택된 강의 ID
     */
    private void handleLectureDetail(String lectureId) {
        // 1. 로그 출력
        System.out.println("[Controller] 강의 상세 조회: " + lectureId);

        // 2. Service에서 강의 상세 정보 조회
        Lecture lecture = service.getLecture(lectureId);

        // 3. 결과 처리
        if (lecture != null) {
            // 평점 정보도 함께 조회
            double rating = service.getLectureRating(lectureId);
            System.out.println("[Controller] 강의 평점: " + rating);

            // View에 상세 정보 전달
            view.showLectureDetail(lecture);
        } else {
            view.showMessage("강의 정보를 찾을 수 없습니다.");
        }
    }

    /**
     * 현재 필터 조건으로 강의 목록 로드
     *
     * 기능:
     * - 모든 필터 조건을 Service에 전달
     * - Service.getLectures() 호출
     * - 결과 데이터를 View에 업데이트
     */
    private void loadLectures() {
        // 1. Service 호출 - 필터링, 정렬, 검색 모두 처리
        List<Lecture> lectures = service.getLectures(
                currentSubject,
                currentGrade,
                currentAcademy,
                currentSortBy,
                currentKeyword
        );

        // 2. 로그 출력
        System.out.println("[Controller] 로드된 강의 수: " + lectures.size());
        System.out.println("[Controller] 필터 상태 - 과목:" + currentSubject +
                ", 학년:" + currentGrade +
                ", 학원:" + currentAcademy +
                ", 정렬:" + currentSortBy +
                ", 검색:" + currentKeyword);

        // 3. View에 데이터 전달
        view.updateLectureCards(lectures);
    }

    /**
     * 필터 초기화
     *
     * 기능:
     * - 모든 필터 조건을 기본값으로 리셋
     * - View 초기화
     * - 전체 강의 목록 다시 로드
     */
    public void resetFilters() {
        // 1. 필터 상태 초기화
        this.currentSubject = "전체";
        this.currentGrade = "전체";
        this.currentAcademy = "전체";
        this.currentSortBy = "평점순";
        this.currentKeyword = "";

        // 2. View 초기화
        view.resetFilters();

        // 3. 데이터 다시 로드
        loadLectures();

        // 4. 로그 출력
        System.out.println("[Controller] 필터 초기화 완료");
    }

    /**
     * 추천 강의 조회 (평점 4.0 이상)
     */
    public void loadRecommendedLectures() {
        // 1. Service에서 추천 강의 조회
        List<Lecture> recommendedLectures = service.getRecommendedLectures();

        // 2. 로그 출력
        System.out.println("[Controller] 추천 강의 조회 (평점 4.0 이상)");

        // 3. View에 데이터 전달
        view.updateLectureCards(recommendedLectures);
    }

    /**
     * 특정 강사의 강의 목록 조회
     *
     * @param instructor 강사명
     */
    public void loadLecturesByInstructor(String instructor) {
        // 1. Service에서 강사별 강의 조회
        List<Lecture> lectures = service.getLecturesByInstructor(instructor);

        // 2. 로그 출력
        System.out.println("[Controller] 강사별 강의 조회: " + instructor);

        // 3. View에 데이터 전달
        view.updateLectureCards(lectures);
    }

    // ========== Getter 메서드 (필요시 사용) ==========

    public String getCurrentSubject() {
        return currentSubject;
    }

    public String getCurrentGrade() {
        return currentGrade;
    }

    public String getCurrentAcademy() {
        return currentAcademy;
    }

    public String getCurrentSortBy() {
        return currentSortBy;
    }

    public String getCurrentKeyword() {
        return currentKeyword;
    }
}
    


