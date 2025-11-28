package com.project.app.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Instructor Model
 *
 * 특징:
 * - 강사 기본 정보는 불변 (id, name, academyId, introduction, subject, rating)
 * - 교재 및 강의 정보는 읽기 전용 (textbookIds, lectureIds)
 * - 수강 학생 목록만 변경 가능 (studentIds)
 *
 * - 강사는 추가/삭제되지 않음
 * - 강의 개수 및 교재는 변경되지 않음
 * - 변경되는 것: 수강생 목록
 */
public class Instructor {

    // ========== 불변 필드 (Basic Information) ==========

    private final String id;
    private final String name;
    private final String academyId;
    private final String introduction; // 강사 소개글
    private final String subject;  // 담당 과목
    private final double rating; // 별점 (Rating) 필드 추가

    // ========== 읽기 전용 필드 (Related Data) ==========

    private final String textbookId;  // 읽기 전용
    private final List<String> lectureIds;   // 읽기 전용

    // ========== 변경 가능 필드 ==========

    private final List<String> studentIds;   // 변경 가능 (추가/제거만)

    // ========== 추가 정보 ==========

    private String profileImagePath;  // 프로필 이미지는 변경 가능

    /**
     * 전체 필드 생성자
     *
     * @param id 강사 ID
     * @param name 강사명
     * @param academyId 소속 학원 ID
     * @param introduction 강사 소개
     * @param subject 담당 과목
     * @param textbookId 교재 ID
     * @param lectureIds 강의 ID 목록
     * @param studentIds 수강생 ID 목록
     * @param rating 별점 (Rating) 추가
     */
    public Instructor(String id, String name, String academyId, String introduction, String subject,
                      String textbookId, List<String> lectureIds, List<String> studentIds, double rating) { // rating 인자 추가
        this.id = id;
        this.name = name;
        this.academyId = academyId;
        this.introduction = introduction;
        this.subject = subject != null ? subject : "";
        this.rating = rating; // rating 초기화
        this.textbookId = textbookId; // Can be null
        // 불변 리스트로 저장
        this.lectureIds = Collections.unmodifiableList(
                lectureIds != null ? new ArrayList<>(lectureIds) : new ArrayList<>()
        );
        // 학생 목록만 변경 가능
        this.studentIds = studentIds != null ? new ArrayList<>(studentIds) : new ArrayList<>();
    }

    // ========== Getter 메서드 ==========

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAcademyId() {
        return academyId;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getSubject() {
        return subject;
    }

    /**
     * 별점 조회 (새로 추가)
     */
    public double getRating() {
        return rating;
    }

    /**
     * 교재 ID 조회 (읽기 전용)
     */
    public String getTextbookId() {
        return textbookId;
    }

    /**
     * 강의 ID 목록 조회 (읽기 전용)
     * 불변 리스트이므로 직접 반환
     */
    public List<String> getLectureIds() {
        return lectureIds;
    }

    /**
     * 학생 ID 목록 조회
     * 방어적 복사본 반환 (외부 수정 방지)
     */
    public List<String> getStudentIds() {
        return new ArrayList<>(studentIds);
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    // ========== Setter 메서드 ==========

    /**
     * 프로필 이미지 경로 설정
     * 프로필 이미지만 변경 가능
     */
    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    // ========== 비즈니스 메서드 ==========

    /**
     * 수강 학생 추가
     *
     * @param studentId 추가할 학생 ID
     */
    public void addStudent(String studentId) {
        if (studentId != null && !studentId.trim().isEmpty()) {
            if (!studentIds.contains(studentId)) {
                studentIds.add(studentId);
            }
        }
    }

    /**
     * 특정 학생이 수강 중인지 확인
     *
     * @param studentId 확인할 학생 ID
     * @return 수강 여부
     */
    public boolean hasStudent(String studentId) {
        return studentIds.contains(studentId);
    }

    // ========== 조회 메서드 ==========


    /**
     * 특정 강의 진행 여부 확인
     */
    public boolean hasLecture(String lectureId) {
        return lectureIds.contains(lectureId);
    }

    @Override
    public String toString() {
        return "Instructor{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", academyId='" + academyId + '\'' +
                ", subject='" + subject + '\'' +
                ", rating=" + rating + // toString에 rating 추가
                ", textbookId='" + (textbookId != null ? textbookId : "없음") + '\'' +
                ", lectureCount=" + lectureIds.size() +
                ", studentCount=" + studentIds.size() +
                '}';
    }
}