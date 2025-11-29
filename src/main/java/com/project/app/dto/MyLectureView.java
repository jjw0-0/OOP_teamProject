package com.project.app.dto;

/**
 * 내 강의 카드 UI 표시용 DTO
 *
 * 사용 위치:
 * - UserService.getMyLectures()
 * - MyPageView 의 "내 강의" 영역
 *
 * 필드:
 *  - lectureId (int)          : 강의 ID (숫자형, 예: 1, 27 ...)
 *  - lectureName (String)     : 강의명
 *  - instructorName (String)  : 강사명
 *  - subject (String)         : 과목
 *  - dayOfWeek (String)       : 수업 요일 (예: "월", "화, 목")
 *  - time (String)            : 수업 시간/교시 (예: "1~3교시", "7,8,9")
 *  - location (String)        : 강의실 위치
 *  - nextScheduleDate (String): 다음 수업 날짜(문자열 표시용, 임시로 "다음 수업일 계산 예정" 등)
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

    /**
     * UserService에서 사용하는 생성자
     *
     * 사용 예:
     *   new MyLectureView(
     *       numericLecId,
     *       lecture.getTitle(),
     *       lecture.getInstructor(),
     *       lecture.getSubject(),
     *       lecture.getDayOfWeek(),
     *       lecture.getTime(),
     *       lecture.getLocation(),
     *       "다음 수업일 계산 예정"
     *   );
     */
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

    @Override
    public String toString() {
        return "MyLectureView{" +
                "lectureId=" + lectureId +
                ", lectureName='" + lectureName + '\'' +
                ", instructorName='" + instructorName + '\'' +
                ", subject='" + subject + '\'' +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", time='" + time + '\'' +
                ", location='" + location + '\'' +
                ", nextScheduleDate='" + nextScheduleDate + '\'' +
                '}';
    }
}
