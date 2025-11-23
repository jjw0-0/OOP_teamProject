package com.project.app.model;

/**
 * Lecture Model
 *
 * 특징:
 * - 강의 기본 정보는 불변
 * - 수강생 수는 변경 가능
 */
public class Lecture {

    // ========== 불변 필드 ==========

    private final String id;
    private final String academy;
    private final String subject;
    private final int year;
    private final String name;
    private final String instructorName;
    private final String textbookName;
    private final int price;
    private final int textbookPrice;
    private final String description;
    private final String targetGrade;
    private final String syllabus;
    private final int capacity;
    private final String dayOfWeek;
    private final String time;

    // ========== 변경 가능 필드 ==========

    private int currentEnrollment;  // 현재 수강생 수

    // ========== 생성자 ==========

    public Lecture(String id, String academy, String subject, int year, String name,
                   String instructorName, String textbookName, int price, int textbookPrice,
                   String description, String targetGrade, String syllabus, int capacity,
                   String dayOfWeek, String time) {
        this.id = id;
        this.academy = academy;
        this.subject = subject;
        this.year = year;
        this.name = name;
        this.instructorName = instructorName;
        this.textbookName = textbookName;
        this.price = price;
        this.textbookPrice = textbookPrice;
        this.description = description;
        this.targetGrade = targetGrade;
        this.syllabus = syllabus;
        this.capacity = capacity;
        this.dayOfWeek = dayOfWeek;
        this.time = time;
        this.currentEnrollment = 0;
    }

    // ========== Getter 메서드 ==========

    public String getId() {
        return id;
    }

    public String getAcademy() {
        return academy;
    }

    public String getSubject() {
        return subject;
    }

    public int getYear() {
        return year;
    }

    public String getName() {
        return name;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public String getTextbookName() {
        return textbookName;
    }

    public int getPrice() {
        return price;
    }

    public int getTextbookPrice() {
        return textbookPrice;
    }

    public String getDescription() {
        return description;
    }

    public String getTargetGrade() {
        return targetGrade;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getTime() {
        return time;
    }

    public int getCurrentEnrollment() {
        return currentEnrollment;
    }

    // ========== Setter 메서드 ==========

    public void setCurrentEnrollment(int currentEnrollment) {
        this.currentEnrollment = currentEnrollment;
    }

    // ========== 비즈니스 메서드 ==========

    /**
     * 정원 초과 여부 확인
     */
    public boolean isFull() {
        return currentEnrollment >= capacity;
    }

    /**
     * 남은 자리 수 반환
     */
    public int getRemainingSeats() {
        return Math.max(0, capacity - currentEnrollment);
    }

    /**
     * 수강생 수 증가
     */
    public void increaseEnrollment() {
        if (!isFull()) {
            currentEnrollment++;
        }
    }

    /**
     * 수강생 수 감소
     */
    public void decreaseEnrollment() {
        if (currentEnrollment > 0) {
            currentEnrollment--;
        }
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", instructorName='" + instructorName + '\'' +
                ", subject='" + subject + '\'' +
                ", price=" + price +
                ", enrollment=" + currentEnrollment + "/" + capacity +
                '}';
    }
}

