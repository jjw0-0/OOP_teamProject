package com.project.app.model;

/**
 * Lecture 엔티티
 *
 * - 강의 기본 정보를 담는 도메인 객체
 * - 추후 LectureService, LectureRepository 등에서 사용
 */
public class Lecture {

    private final int id;
    private final String name;
    private final String subject;
    private final String instructorName;
    private final String academyName;
    private final String dayOfWeek;    // "월, 수" 형태
    private final String time;         // "18:00~20:00" 형태
    private final String location;     // "강의실 301" 등
    private final int price;           // 강의 수강료
    private final String textbookName; // 교재 이름
    private final int textbookPrice;   // 교재 가격

    public Lecture(
            int id,
            String name,
            String subject,
            String instructorName,
            String academyName,
            String dayOfWeek,
            String time,
            String location,
            int price,
            String textbookName,
            int textbookPrice
    ) {
        this.id = id;
        this.name = name;
        this.subject = subject;
        this.instructorName = instructorName;
        this.academyName = academyName;
        this.dayOfWeek = dayOfWeek;
        this.time = time;
        this.location = location;
        this.price = price;
        this.textbookName = textbookName;
        this.textbookPrice = textbookPrice;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSubject() {
        return subject;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public String getAcademyName() {
        return academyName;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public int getPrice() {
        return price;
    }

    public String getTextbookName() {
        return textbookName;
    }

    public int getTextbookPrice() {
        return textbookPrice;
    }
}
