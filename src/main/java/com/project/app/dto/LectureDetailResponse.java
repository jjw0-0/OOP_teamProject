package com.project.app.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 강의 상세 정보 조회 결과 DTO
 *
 @@ -20,4 +23,333 @@
 */
public class LectureDetailResponse {

    // ===== 기본 정보 =====
    private String id;                      // 강의 ID
    private String name;                    // 강의명
    private String subject;                 // 과목
    private String academyName;             // 학원명
    private int year;                       // 년도

    // ===== 강사/교재 정보 =====
    private String instructorName;          // 강사명
    private String textbook;                // 교재명

    // ===== 가격 정보 =====
    private int lecturePrice;               // 강의 가격
    private int textbookPrice;              // 교재 가격
    private int totalPrice;                 // 총 가격 (강의 + 교재)

    // ===== 수강 정보 =====
    private int capacity;                   // 최대 정원
    private int currentEnrolled;            // 현재 수강 인원
    private int remainingSeats;             // 남은 자리
    private double enrollmentRate;          // 수강률 (%)

    // ===== 시간/장소 정보 =====
    private String dayOfWeek;               // 요일
    private String time;                    // 시간
    private String location;                // 강의실 위치

    // ===== 설명/계획 정보 =====
    private String description;             // 강의 설명
    private String targetGrade;             // 대상 학년
    private int plan;                       // 강의 계획 (주차)
    private List<String> syllabus;          // 주차별 강의 계획

    // ===== 평가 정보 =====
    private double rating;                  // 평점
    private String thumbnailPath;           // 썸네일 경로

    // ===== 사용자별 상태 =====
    private boolean isEnrolled;             // 현재 사용자가 신청했는지
    private boolean canEnroll;              // 신청 가능 여부 (정원 미달 && 미신청)

    // 기본 생성자
    public LectureDetailResponse() {
        this.syllabus = new ArrayList<>();
        this.isEnrolled = false;
        this.canEnroll = true;
    }

    // 전체 필드 생성자
    public LectureDetailResponse(String id, String name, String subject, String academyName,
                                 int year, String instructorName, String textbook,
                                 int lecturePrice, int textbookPrice,
                                 int capacity, int currentEnrolled,
                                 String dayOfWeek, String time, String location,
                                 String description, String targetGrade, int plan,
                                 double rating, String thumbnailPath,
                                 boolean isEnrolled, boolean canEnroll) {
        this();
        this.id = id;
        this.name = name;
        this.subject = subject;
        this.academyName = academyName;
        this.year = year;
        this.instructorName = instructorName;
        this.textbook = textbook;
        this.lecturePrice = lecturePrice;
        this.textbookPrice = textbookPrice;
        this.totalPrice = lecturePrice + textbookPrice;
        this.capacity = capacity;
        this.currentEnrolled = currentEnrolled;
        this.remainingSeats = capacity - currentEnrolled;
        this.enrollmentRate = capacity > 0 ? (currentEnrolled * 100.0 / capacity) : 0.0;
        this.dayOfWeek = dayOfWeek;
        this.time = time;
        this.location = location;
        this.description = description;
        this.targetGrade = targetGrade;
        this.plan = plan;
        this.rating = rating;
        this.thumbnailPath = thumbnailPath;
        this.isEnrolled = isEnrolled;
        this.canEnroll = canEnroll;
    }

    // ===== Getter & Setter =====

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAcademyName() {
        return academyName;
    }

    public void setAcademyName(String academyName) {
        this.academyName = academyName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getTextbook() {
        return textbook;
    }

    public void setTextbook(String textbook) {
        this.textbook = textbook;
    }

    public int getLecturePrice() {
        return lecturePrice;
    }

    public void setLecturePrice(int lecturePrice) {
        this.lecturePrice = lecturePrice;
        this.totalPrice = this.lecturePrice + this.textbookPrice;
    }

    public int getTextbookPrice() {
        return textbookPrice;
    }

    public void setTextbookPrice(int textbookPrice) {
        this.textbookPrice = textbookPrice;
        this.totalPrice = this.lecturePrice + this.textbookPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
        updateEnrollmentInfo();
    }

    public int getCurrentEnrolled() {
        return currentEnrolled;
    }

    public void setCurrentEnrolled(int currentEnrolled) {
        this.currentEnrolled = currentEnrolled;
        updateEnrollmentInfo();
    }

    public int getRemainingSeats() {
        return remainingSeats;
    }

    public void setRemainingSeats(int remainingSeats) {
        this.remainingSeats = remainingSeats;
    }

    public double getEnrollmentRate() {
        return enrollmentRate;
    }

    public void setEnrollmentRate(double enrollmentRate) {
        this.enrollmentRate = enrollmentRate;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTargetGrade() {
        return targetGrade;
    }

    public void setTargetGrade(String targetGrade) {
        this.targetGrade = targetGrade;
    }

    public int getPlan() {
        return plan;
    }

    public void setPlan(int plan) {
        this.plan = plan;
    }

    public List<String> getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(List<String> syllabus) {
        this.syllabus = syllabus;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public boolean isEnrolled() {
        return isEnrolled;
    }

    public void setEnrolled(boolean enrolled) {
        isEnrolled = enrolled;
    }

    public boolean isCanEnroll() {
        return canEnroll;
    }

    public void setCanEnroll(boolean canEnroll) {
        this.canEnroll = canEnroll;
    }

    // ===== 유틸리티 메서드 =====

    /**
     * 수강 정보 업데이트 (남은 자리, 수강률 계산)
     */
    private void updateEnrollmentInfo() {
        this.remainingSeats = capacity - currentEnrolled;
        this.enrollmentRate = capacity > 0 ? (currentEnrolled * 100.0 / capacity) : 0.0;
    }

    /**
     * 강의 계획 추가
     */
    public void addSyllabusItem(String item) {
        if (this.syllabus == null) {
            this.syllabus = new ArrayList<>();
        }
        this.syllabus.add(item);
    }

    /**
     * 정원 초과 여부 확인
     */
    public boolean isFull() {
        return currentEnrolled >= capacity;
    }

    @Override
    public String toString() {
        return String.format(
                "LectureDetailResponse{id='%s', name='%s', subject='%s', instructor='%s', " +
                        "academy='%s', price=%d+%d=%d, enrolled=%d/%d(%.1f%%), " +
                        "isEnrolled=%b, canEnroll=%b}",
                id, name, subject, instructorName, academyName,
                lecturePrice, textbookPrice, totalPrice,
                currentEnrolled, capacity, enrollmentRate,
                isEnrolled, canEnroll
        );
    }
}