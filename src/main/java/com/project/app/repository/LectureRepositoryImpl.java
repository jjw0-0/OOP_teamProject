package com.project.app.repository;

import com.project.app.model.Lecture;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Lecture Repository Implementation
 *
 * 특징:
 * - 초기 로드 시 파일에서 강의 정보를 읽어옴
 * - 메모리 캐시 방식으로 빠른 조회 제공
 */
public class LectureRepositoryImpl implements LectureRepository {

    private static final String DATA_FILE_PATH = "src/main/data/LeactureData.txt";
    private static final String FIELD_DELIMITER = "/";

    private final Map<String, Lecture> lectureCache;
    private final Map<String, List<Lecture>> instructorNameCache;  // 강사명 -> 강의 목록

    /**
     * LectureRepositoryImpl 생성자
     * 객체 생성 시 파일로부터 데이터를 로드하여 캐시를 초기화합니다.
     */
    public LectureRepositoryImpl() {
        this.lectureCache = new HashMap<>();
        this.instructorNameCache = new HashMap<>();
        loadDataFromFile();
    }

    @Override
    public Lecture findById(String id) {
        return lectureCache.get(id);
    }

    @Override
    public List<Lecture> findAll() {
        return new ArrayList<>(lectureCache.values());
    }

    @Override
    public List<Lecture> findByInstructorName(String instructorName) {
        if (instructorName == null || instructorName.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return instructorNameCache.getOrDefault(instructorName, new ArrayList<>());
    }

    @Override
    public List<Lecture> findByInstructorId(String instructorId) {
        // 강사 ID로 강사를 찾아서 강사명을 얻어야 하는데,
        // 일단 강사명으로 찾는 방식 사용 (InstructorRepository 필요)
        // 여기서는 강사명으로 직접 찾는 방식 사용
        if (instructorId == null || instructorId.trim().isEmpty()) {
            return new ArrayList<>();
        }
        // 강사 ID가 강사명과 같다고 가정 (실제로는 InstructorRepository에서 조회 필요)
        return findByInstructorName(instructorId);
    }

    @Override
    public List<Lecture> findBySubject(String subject) {
        if (subject == null || subject.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return lectureCache.values().stream()
                .filter(lecture -> lecture.getSubject().equals(subject))
                .collect(Collectors.toList());
    }

    @Override
    public List<Lecture> findByIds(List<String> lectureIds) {
        if (lectureIds == null || lectureIds.isEmpty()) {
            return new ArrayList<>();
        }
        return lectureIds.stream()
                .map(lectureCache::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // ========== Private Helper Methods ==========

    /**
     * 데이터 파일로부터 강의 정보를 로드하여 캐시를 초기화합니다.
     */
    private void loadDataFromFile() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(DATA_FILE_PATH));
            // 첫 번째 줄(헤더)을 제외하고 각 라인을 파싱하여 캐시에 저장
            lines.stream().skip(1).forEach(line -> {
                Lecture lecture = parseLineToLecture(line);
                if (lecture != null) {
                    lectureCache.put(lecture.getId(), lecture);
                    
                    // 강사명 캐시에 추가
                    String instructorName = lecture.getInstructorName();
                    instructorNameCache.computeIfAbsent(instructorName, k -> new ArrayList<>()).add(lecture);
                }
            });
            System.out.println("Lecture data loaded: " + lectureCache.size() + " lectures");
        } catch (IOException e) {
            System.err.println("Error reading lecture data file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 데이터 파일의 한 줄 문자열을 파싱하여 Lecture 객체로 변환합니다.
     *
     * 형식: 강의ID/학원/과목/년도/강의 이름/강사/교재/강의 가격/교재 가격/강의 설명/대상 학년/강의 계획/정원/요일/시간
     */
    private Lecture parseLineToLecture(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        try {
            String[] parts = line.split(FIELD_DELIMITER, -1);
            if (parts.length < 14) {
                System.err.println("Invalid lecture data format: " + line);
                return null;
            }
            String id = parts[0].trim();
            String academy = parts[1].trim();
            String subject = parts[2].trim();
            int year = Integer.parseInt(parts[3].trim());
            String name = parts[4].trim();
            String instructorName = parts[5].trim();
            String textbookName = parts[6].trim();
            int price = Integer.parseInt(parts[7].trim());
            int textbookPrice = Integer.parseInt(parts[8].trim());
            String description = parts[9].trim();
            String targetGrade = parts[10].trim();
            //String syllabus = parts[11].trim(); << 데이터에 아직 없음
            int capacity = Integer.parseInt(parts[11].trim());
            String dayOfWeek = parts[12].trim();
            String time = parts[13].trim();

            return new Lecture(id, academy, subject, year, name, instructorName,
                    textbookName, price, textbookPrice, description, targetGrade,
                    "", capacity, dayOfWeek, time);

        } catch (Exception e) {
            System.err.println("Lecture data parsing error: " + line);
            e.printStackTrace();
            return null;
        }
    }
}

