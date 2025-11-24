package com.project.app.dto;

/**
 * 강의 카드 UI 표시용 DTO
 *
 * 목적: 강의 카드 UI 표시용
 *
 * 사용 시점: LectureService -> View (UI 렌더링)
 *
 * 필드:
 *   - id (String)
 *   - name (String)
 *   - subject (String)
 *   - instructorName (String)
 *   - academyName (String)
 *   - thumbnailPath (String)
 *   - rating (double)
 *   - price (int)
 *   - dayOfWeek (String)
 *   - time (String)
 *   - remainingSeats (int)
 *
 * 이유: Lecture 엔티티의 모든 정보가 아닌 카드 표시에 필요한 정보만 선택. Entity와 분리 필수.
 */
public class LectureCardView {

    private String id;                  // 강의 ID
    private String name;                // 강의명
    private String subject;             // 과목
    private String instructorName;      // 강사명
    private String academyName;         // 학원명
    private String thumbnailPath;       // 썸네일 경로
    private double rating;              // 평점
    private int price;                  // 가격
    private String dayOfWeek;           // 요일
    private String time;                // 시간
    private int remainingSeats;         // 남은 자리 수

    // 기본 생성자
    public LectureCardView() {
    }

    // 전체 필드 생성자
    public LectureCardView(String id, String name, String subject,
                           String instructorName, String academyName,
                           String thumbnailPath, double rating, int price,
                           String dayOfWeek, String time, int remainingSeats) {
        this.id = id;
        this.name = name;
        this.subject = subject;
        this.instructorName = instructorName;
        this.academyName = academyName;
        this.thumbnailPath = thumbnailPath;
        this.rating = rating;
        this.price = price;
        this.dayOfWeek = dayOfWeek;
        this.time = time;
        this.remainingSeats = remainingSeats;
    }

    // Getter & Setter
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

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getAcademyName() {
        return academyName;
    }

    public void setAcademyName(String academyName) {
        this.academyName = academyName;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public int getRemainingSeats() {
        return remainingSeats;
    }

    public void setRemainingSeats(int remainingSeats) {
        this.remainingSeats = remainingSeats;
    }

    @Override
    public String toString() {
        return String.format(
                "LectureCardView{id='%s', name='%s', subject='%s', instructor='%s', " +
                        "academy='%s', rating=%.1f, price=%d, dayOfWeek='%s', time='%s', remainingSeats=%d}",
                id, name, subject, instructorName, academyName, rating, price,
                dayOfWeek, time, remainingSeats
        );
    }
}
