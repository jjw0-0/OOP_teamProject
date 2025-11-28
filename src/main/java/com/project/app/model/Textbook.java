package com.project.app.model;

/**
 * Textbook Model
 *
 * 특징:
 * - 교재 정보는 불변
 */
public class Textbook {

    // ========== 불변 필드 ==========

    private final String id;
    private final String name;
    private final int price;
    private final String subject;
    private final String lectureId;
    private final String instructorName;

    // ========== 생성자 ==========

    public Textbook(String id, String name, int price, String subject,
                   String lectureId, String instructorName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.subject = subject;
        this.lectureId = lectureId;
        this.instructorName = instructorName;
    }

    // ========== Getter 메서드 ==========

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getSubject() {
        return subject;
    }

    public String getLectureId() {
        return lectureId;
    }

    public String getInstructorName() {
        return instructorName;
    }

    @Override
    public String toString() {
        return "Textbook{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", subject='" + subject + '\'' +
                ", instructorName='" + instructorName + '\'' +
                '}';
    }
}

