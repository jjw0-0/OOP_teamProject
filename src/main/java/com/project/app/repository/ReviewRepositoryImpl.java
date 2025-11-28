package com.project.app.repository;

import com.project.app.model.Review;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Review Repository Implementation
 *
 * 특징:
 * - 초기 로드 시 파일에서 리뷰 정보를 읽어옴
 * - 메모리 캐시 방식으로 빠른 조회 제공
 */
public class ReviewRepositoryImpl implements ReviewRepository {

    private static final String DATA_FILE_PATH = "src/main/data/ReviewData.txt";
    private static final String FIELD_DELIMITER = "/";

    private final Map<String, Review> reviewCache;

    /**
     * ReviewRepositoryImpl 생성자
     * 객체 생성 시 파일로부터 데이터를 로드하여 캐시를 초기화합니다.
     */
    public ReviewRepositoryImpl() {
        this.reviewCache = new HashMap<>();
        loadDataFromFile();
    }

    @Override
    public Review findById(String id) {
        return reviewCache.get(id);
    }

    @Override
    public List<Review> findAll() {
        return new ArrayList<>(reviewCache.values());
    }

    @Override
    public List<Review> findByInstructorId(String instructorId) {
        if (instructorId == null || instructorId.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return reviewCache.values().stream()
                .filter(review -> review.getInstructorId().equals(instructorId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Review> findByLectureId(String lectureId) {
        if (lectureId == null || lectureId.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return reviewCache.values().stream()
                .filter(review -> review.getLectureId().equals(lectureId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Review> findByInstructorIdAndLectureId(String instructorId, String lectureId) {
        if (instructorId == null || instructorId.trim().isEmpty() ||
            lectureId == null || lectureId.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return reviewCache.values().stream()
                .filter(review -> review.getInstructorId().equals(instructorId) &&
                                 review.getLectureId().equals(lectureId))
                .collect(Collectors.toList());
    }

    // ========== Private Helper Methods ==========

    /**
     * 데이터 파일로부터 리뷰 정보를 로드하여 캐시를 초기화합니다.
     */
    private void loadDataFromFile() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(DATA_FILE_PATH));
            // 첫 번째 줄(헤더)을 제외하고 각 라인을 파싱하여 캐시에 저장
            lines.stream().skip(1).forEach(line -> {
                Review review = parseLineToReview(line);
                if (review != null) {
                    reviewCache.put(review.getId(), review);
                }
            });
            System.out.println("Review data loaded: " + reviewCache.size() + " reviews");
        } catch (IOException e) {
            System.err.println("Error reading review data file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 데이터 파일의 한 줄 문자열을 파싱하여 Review 객체로 변환합니다.
     *
     * @param line 데이터 파일의 한 줄에 해당하는 문자열 (예: "강사ID/강의ID/리뷰ID/유저ID/별점/리뷰내용")
     * @return 파싱에 성공하면 Review 객체, 데이터 형식 오류나 파싱 중 예외 발생 시 null
     */
    private Review parseLineToReview(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        try {
            String[] parts = line.split(FIELD_DELIMITER, -1);
            if (parts.length < 6) {
                System.err.println("Invalid review data format: " + line);
                return null;
            }

            String instructorId = parts[0].trim();
            String lectureId = parts[1].trim();
            String id = parts[2].trim();
            String userId = parts[3].trim();
            double rating = Double.parseDouble(parts[4].trim());
            String content = parts[5].trim();

            return new Review(id, instructorId, lectureId, userId, rating, content);

        } catch (Exception e) {
            System.err.println("Review data parsing error: " + line);
            e.printStackTrace();
            return null;
        }
    }
}

