package com.project.app.dto;

/**
 * 내 강의 카드 UI 표시용 DTO
 *
 * 목적: 내 강의 카드 UI 표시용
 *
 * 사용 시점: LectureService -> MyPageView
 *
 * 필드:
 *   - lectureId (int)
 *   - lectureName (String)
 *   - instructorName (String)
 *   - subject (String)
 *   - dayOfWeek (String)
 *   - time (String)
 *   - location (String)
 *   - nextScheduleDate (String) - 다음 수업 날짜 (계산된 값)
 *
 * 이유: Lecture 엔티티 + 계산된 값(다음 수업 날짜). LectureCardView와 다른 정보 구성.
 */
public class MyLectureView {

    private final int lectureId;
    private final String lectureName;
    private final String instructorName;
    private final String subject;
    private final String dayOfWeek;
    private final String time;
    private final String location;
    private final String nextScheduleDate;

    public MyLectureView(int lectureId,
                         String lectureName,
                         String instructorName,
                         String subject,
                         String dayOfWeek,
                         String time,
                         String location,
                         String nextScheduleDate) {
        this.lectureId = lectureId;
        this.lectureName = lectureName;
        this.instructorName = instructorName;
        this.subject = subject;
        this.dayOfWeek = dayOfWeek;
        this.time = time;
        this.location = location;
        this.nextScheduleDate = nextScheduleDate;
    }

    public int getLectureId() {
        return lectureId;
    }

    public String getLectureName() {
        return lectureName;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public String getSubject() {
        return subject;
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

    public String getNextScheduleDate() {
        return nextScheduleDate;
    }
}
