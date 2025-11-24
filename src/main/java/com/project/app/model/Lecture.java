package com.project.app.model;

import java.util.ArrayList;

public class Lecture {
    //======= (기본 정보) ===========
    private String lectureId;        // 강의ID (M001, E001, D001)
    private String academy;          // 학원
    private String subject;          // 과목
    private int year;                // 년도
    private String title;            // 강의이름
    private String instructor;       // 강사
    private String textbook;         // 교재
    private int lecturePrice;        // 강의가격
    private int textbookPrice;       // 교재가격
    private String description;      // 강의설명
    private String targetGrade;      // 대상학년
    private int plan;                // 강의계획
    private String dayOfWeek;        // 요일
    private String time;             // 시간
    private double rating;           // 평점
    // ======= (수강 관리) =======
    private int currentEnrolled;     // 현재 수강 인원
    private int capacity;            // 최대 정원
    private String location;         // 강의실 위치
    private String thumbnailPath;    // 썸네일 이미지 경로
    private ArrayList<String> syllabus; // 주차별 강의계획

    // 기본 생성자
    public Lecture() {
        this.syllabus = new ArrayList<>();
        this.currentEnrolled = 0;
        this.capacity = 30;
    }


    // 전체 필드 생성자
    public Lecture(String lectureId, String academy, String subject, int year,
                   String title, String instructor, String textbook,
                   int lecturePrice, int textbookPrice, String description,
                   String targetGrade, int plan, int capacity,
                   String dayOfWeek, String time, double rating) {
        this();
        this.lectureId = lectureId;
        this.academy = academy;
        this.subject = subject;
        this.year = year;
        this.title = title;
        this.instructor = instructor;
        this.textbook = textbook;
        this.lecturePrice = lecturePrice;
        this.textbookPrice = textbookPrice;
        this.description = description;
        this.targetGrade = targetGrade;
        this.plan = plan;
        this.capacity = capacity;
        this.dayOfWeek = dayOfWeek;
        this.time = time;
        this.rating = rating;
    }

    // Getter & Setter
    public String getLectureId() {
        return lectureId;
    }

    public void setLectureId(String lectureId) {
        this.lectureId = lectureId;
    }

    public String getAcademy() {
        return academy;
    }

    public void setAcademy(String academy) {
        this.academy = academy;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
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
    }

    public int getTextbookPrice() {
        return textbookPrice;
    }

    public void setTextbookPrice(int textbookPrice) {
        this.textbookPrice = textbookPrice;
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

    //=========== (수강 관리 메서드) ============

    // <수강 인원 증가>
    public boolean addEnrolled() {
        if (!isFull()) {
            currentEnrolled++;
            return true;
        }
        return false;
    }

    // <수강 인원 감소>
    public boolean removeEnrolled() {
        if (currentEnrolled > 0) {
            currentEnrolled--;
            return true;
        }
        return false;
    }

    // <정원 초과 확인>
    public boolean isFull() {
        return currentEnrolled >= capacity;
    }

    // <남은 자리 수>
    public int getRemainingCapacity() {
        return capacity - currentEnrolled;
    }

    // <수강률 계산>
    public double getEnrollmentRate() {
        if (capacity == 0) return 0.0;
        return (currentEnrolled * 100.0) / capacity;
    }

    //===============(강의 계획 관리) ==================

    //<강의 계획 추가>
    public void addSyllabus(String weekPlan) {
        syllabus.add(weekPlan);
    }

    //<강의 계획 전체 설정>
    public void setSyllabus(ArrayList<String> syllabus) {
        this.syllabus = syllabus;
    }

    //<강의 계획 조회>
    public ArrayList<String> getSyllabus() {
        return syllabus;
    }

    //<특정 주차 강의 계획 조회>
    public String getSyllabus(int week) {
        if (week > 0 && week <= syllabus.size()) {
            return syllabus.get(week - 1);
        }
        return null;
    }

    // Service에서 사용하는 getGrade() 메서드 추가 (호환성)
    public String getGrade() {
        return targetGrade;
    }

    public void setGrade(String grade) {
        this.targetGrade = grade;
    }

    public int getPlan() {
        return plan;
    }

    public void setPlan(int plan) {
        this.plan = plan;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getCurrentEnrolled() {
        return currentEnrolled;
    }

    public void setCurrentEnrolled(int currentEnrolled) {
        this.currentEnrolled = currentEnrolled;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    // ========== toString ==========

    @Override
    public String toString() {
        return String.format(
                "Lecture{id='%s', title='%s', instructor='%s', subject='%s', " +
                        "academy='%s', grade='%s', price=%d, rating=%.1f, enrolled=%d/%d}",
                lectureId, title, instructor, subject, academy, targetGrade,
                lecturePrice, rating, currentEnrolled, capacity
        );
    }

    /**
     * 상세 정보 문자열
     */
    public String toDetailString() {
        return String.format(
                "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                        "강의 ID: %s\n" +
                        "강의명: %s\n" +
                        "강사: %s\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                        "과목: %s | 학년: %s | 학원: %s\n" +
                        "요일: %s | 시간: %s | 장소: %s\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                        "강의 가격: %,d원 | 교재 가격: %,d원\n" +
                        "평점: ★%.1f | 수강 인원: %d/%d명 (%.1f%%)\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                        "설명: %s\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━",
                lectureId, title, instructor,
                subject, targetGrade, academy,
                dayOfWeek, time, (location != null ? location : "미정"),
                lecturePrice, textbookPrice,
                rating, currentEnrolled, capacity, getEnrollmentRate(),
                description
        );
    }
}