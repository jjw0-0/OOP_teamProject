package com.project.app.model;

/**
 * Review Model
 *
 * 특징:
 * - 리뷰 정보는 불변 (id, instructorId, lectureId, userId, rating, content)
 */
public class Review {

    private final String id;
    private final String instructorId;
    private final String lectureId;
    private final String userId;
    private final double rating;
    private final String content;

    public Review(String id, String instructorId, String lectureId, String userId, double rating, String content) {
        this.id = id;
        this.instructorId = instructorId;
        this.lectureId = lectureId;
        this.userId = userId;
        this.rating = rating;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public String getLectureId() {
        return lectureId;
    }

    public String getUserId() {
        return userId;
    }

    public double getRating() {
        return rating;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id='" + id + '\'' +
                ", instructorId='" + instructorId + '\'' +
                ", lectureId='" + lectureId + '\'' +
                ", userId='" + userId + '\'' +
                ", rating=" + rating +
                '}';
    }
}

