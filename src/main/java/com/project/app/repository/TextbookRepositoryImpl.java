package com.project.app.repository;

import com.project.app.model.Textbook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Textbook Repository Implementation
 *
 * 특징:
 * - 초기 로드 시 파일에서 교재 정보를 읽어옴
 * - 메모리 캐시 방식으로 빠른 조회 제공
 */
public class TextbookRepositoryImpl implements TextbookRepository {

    private static final String DATA_FILE_PATH = "src/main/data/TextbookData.txt";
    private static final String FIELD_DELIMITER = "/";

    private final Map<String, Textbook> textbookCache;
    private final Map<String, Textbook> lectureIdCache;  // 강의ID -> 교재

    /**
     * TextbookRepositoryImpl 생성자
     * 객체 생성 시 파일로부터 데이터를 로드하여 캐시를 초기화합니다.
     */
    public TextbookRepositoryImpl() {
        this.textbookCache = new HashMap<>();
        this.lectureIdCache = new HashMap<>();
        loadDataFromFile();
    }

    @Override
    public Textbook findById(String id) {
        return textbookCache.get(id);
    }

    @Override
    public List<Textbook> findAll() {
        return new ArrayList<>(textbookCache.values());
    }

    @Override
    public List<Textbook> findByInstructorName(String instructorName) {
        if (instructorName == null || instructorName.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return textbookCache.values().stream()
                .filter(textbook -> textbook.getInstructorName().equals(instructorName))
                .collect(Collectors.toList());
    }

    @Override
    public Textbook findByLectureId(String lectureId) {
        if (lectureId == null || lectureId.trim().isEmpty()) {
            return null;
        }
        return lectureIdCache.get(lectureId);
    }

    @Override
    public List<Textbook> findByIds(List<String> textbookIds) {
        if (textbookIds == null || textbookIds.isEmpty()) {
            return new ArrayList<>();
        }
        return textbookIds.stream()
                .map(textbookCache::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // ========== Private Helper Methods ==========

    /**
     * 데이터 파일로부터 교재 정보를 로드하여 캐시를 초기화합니다.
     */
    private void loadDataFromFile() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(DATA_FILE_PATH));
            // 첫 번째 줄(헤더)을 제외하고 각 라인을 파싱하여 캐시에 저장
            lines.stream().skip(1).forEach(line -> {
                Textbook textbook = parseLineToTextbook(line);
                if (textbook != null) {
                    textbookCache.put(textbook.getId(), textbook);
                    
                    // 강의ID 캐시에 추가 (강의ID가 있으면)
                    if (textbook.getLectureId() != null && !textbook.getLectureId().trim().isEmpty()) {
                        lectureIdCache.put(textbook.getLectureId(), textbook);
                    }
                }
            });
            System.out.println("Textbook data loaded: " + textbookCache.size() + " textbooks");
        } catch (IOException e) {
            System.err.println("Error reading textbook data file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 데이터 파일의 한 줄 문자열을 파싱하여 Textbook 객체로 변환합니다.
     *
     * 형식: 교재ID/교재명/가격/과목/강의ID/강사명
     */
    private Textbook parseLineToTextbook(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        try {
            String[] parts = line.split(FIELD_DELIMITER, -1);
            if (parts.length < 6) {
                System.err.println("Invalid textbook data format: " + line);
                return null;
            }

            String id = parts[0].trim();
            String name = parts[1].trim();
            int price = Integer.parseInt(parts[2].trim());
            String subject = parts[3].trim();
            String lectureId = parts[4].trim();
            String instructorName = parts[5].trim();

            return new Textbook(id, name, price, subject, lectureId, instructorName);

        } catch (Exception e) {
            System.err.println("Textbook data parsing error: " + line);
            e.printStackTrace();
            return null;
        }
    }
}

