package com.project.app.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 강의 목록 조회 결과 DTO
 *
 * 목적: 강의 목록 조회 결과
 *
 * 사용 시점: LectureService -> Controller -> LecturePageView
 *
 * 필드:
 *   - lectures (List<LectureCardView>)
 *   - totalCount (int)
 *
 * 이유: List를 감싸서 추가 정보(총 개수) 제공. 없어도 되지만 있으면 편리.
 */
public class LectureListResponse {

    private List<LectureCardView> lectures;  // 강의 목록
    private int totalCount;                  // 전체 강의 개수

    // 기본 생성자
    public LectureListResponse() {
        this.lectures = new ArrayList<>();
        this.totalCount = 0;
    }

    // 전체 필드 생성자
    public LectureListResponse(List<LectureCardView> lectures, int totalCount) {
        this.lectures = lectures != null ? lectures : new ArrayList<>();
        this.totalCount = totalCount;
    }

    // Getter & Setter
    public List<LectureCardView> getLectures() {
        return lectures;
    }

    public void setLectures(List<LectureCardView> lectures) {
        this.lectures = lectures;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 강의 추가
     */
    public void addLecture(LectureCardView lecture) {
        if (this.lectures == null) {
            this.lectures = new ArrayList<>();
        }
        this.lectures.add(lecture);
    }

    /**
     * 강의 목록이 비어있는지 확인
     */
    public boolean isEmpty() {
        return lectures == null || lectures.isEmpty();
    }

    /**
     * 현재 목록의 강의 개수
     */
    public int getSize() {
        return lectures != null ? lectures.size() : 0;
    }

    @Override
    public String toString() {
        return String.format(
                "LectureListResponse{totalCount=%d, currentSize=%d}",
                totalCount, getSize()
        );
    }
}