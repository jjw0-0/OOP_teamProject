package com.project.app.repository;

import com.project.app.model.Lecture;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LectureRepository 구현체 (파일 기반 버전)
 *
 * - LectureData.txt 파일에서 강의 정보를 읽어서 메모리에 보관
 * - 강의 ID는 문자열("L001")과 숫자(1) 두 가지 방식으로 모두 조회 가능하도록 저장
 *
 * LectureData.txt 형식 (14개 컬럼, '/' 구분):
 * 0: lectureId      (예: L001)
 * 1: academy        (예: A1)
 * 2: subject        (국어, 수학, ...)
 * 3: year           (예: 2027)
 * 4: title
 * 5: instructor
 * 6: textbook
 * 7: lecturePrice
 * 8: textbookPrice
 * 9: description
 * 10: targetGrade   (예: 고3,N수)
 * 11: capacity
 * 12: dayOfWeek     (예: 월, 화, 수, ...)
 * 13: time          (예: 1,2,3,4 or 7,8,9)
 */
public class LectureRepositoryImpl implements LectureRepository {

    private static final LectureRepositoryImpl instance = new LectureRepositoryImpl();

    public static LectureRepositoryImpl getInstance() {
        return instance;
    }

    private static final String LECTURE_DATA_FILE = "LectureData.txt";

    // "L001" → Lecture
    private final Map<String, Lecture> storeById = new ConcurrentHashMap<>();
    // 1 → Lecture
    private final Map<Integer, Lecture> storeByNumericId = new ConcurrentHashMap<>();

    /**
     * 생성자
     * - App.java에서 new LectureRepositoryImpl() 로도 사용할 수 있도록 public 유지
     * - 생성 시 데이터 파일을 읽어 메모리에 로딩
     */
    public LectureRepositoryImpl() {
        loadFromFile();
    }

    /**
     * LectureData.txt 파일을 읽어 강의 정보를 메모리에 적재
     */
    private void loadFromFile() {
        storeById.clear();
        storeByNumericId.clear();

        File file = new File(LECTURE_DATA_FILE);
        if (!file.exists()) {
            System.err.println("[LectureRepositoryImpl] 데이터 파일 없음: " + file.getAbsolutePath());
            return;
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // 헤더 스킵 (첫 줄에 "강의ID" 들어있는지 체크)
                if (firstLine) {
                    firstLine = false;
                    if (line.contains("강의ID")) {
                        continue;
                    }
                }

                String[] parts = line.split("/");
                if (parts.length < 14) {
                    System.err.println("[LectureRepositoryImpl] 잘못된 형식의 라인: " + line);
                    continue;
                }

                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim();
                }

                String lectureId   = parts[0]; // 예: L001
                String academy     = parts[1];
                String subject     = parts[2];
                int year           = parseInt(parts[3], 0);
                String title       = parts[4];
                String instructor  = parts[5];
                String textbook    = parts[6];
                int lecturePrice   = parseInt(parts[7], 0);
                int textbookPrice  = parseInt(parts[8], 0);
                String description = parts[9];
                String targetGrade = parts[10];
                int capacity       = parseInt(parts[11], 30);
                String dayOfWeek   = parts[12];
                String time        = parts[13];

                // 데이터 파일에는 rating/currentEnrolled/location/thumbnail 정보가 없으므로 기본값 사용
                double rating       = 0.0;
                int currentEnrolled = 0;
                String location     = null;
                String thumbnail    = null;

                Lecture lecture = new Lecture(
                        lectureId,
                        academy,
                        subject,
                        year,
                        title,
                        instructor,
                        textbook,
                        lecturePrice,
                        textbookPrice,
                        location,
                        description,
                        targetGrade,
                        rating,
                        currentEnrolled,
                        capacity,
                        dayOfWeek,
                        time,
                        thumbnail
                );

                // plan은 파일에 없으므로 기본값(0) 그대로 두거나 필요하면 setPlan 호출 가능
                // lecture.setPlan(0);

                save(lecture); // 내부 맵에 저장
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int parseInt(String s, int defaultValue) {
        if (s == null) return defaultValue;
        try {
            return Integer.parseInt(s.replace(",", "").trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private double parseDouble(String s, double defaultValue) {
        if (s == null) return defaultValue;
        try {
            return Double.parseDouble(s.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * "L001" → 1 같이 문자열 ID에서 숫자 부분만 추출
     */
    private int extractNumericId(String lectureId) {
        if (lectureId == null) return -1;
        String digits = lectureId.replaceAll("\\D", ""); // 숫자만 남기기
        if (digits.isEmpty()) return -1;
        try {
            return Integer.parseInt(digits);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // ========== 인터페이스 구현 ==========

    @Override
    public Lecture findById(String lectureId) {
        if (lectureId == null) return null;
        return storeById.get(lectureId);
    }

    @Override
    public Lecture findById(int numericId) {
        return storeByNumericId.get(numericId);
    }

    @Override
    public List<Lecture> findById(List<String> lectureIds) {
        List<Lecture> result = new ArrayList<>();
        if (lectureIds == null) return result;

        for (String id : lectureIds) {
            Lecture lec = findById(id);
            if (lec != null) {
                result.add(lec);
            }
        }
        return result;
    }

    @Override
    public List<Lecture> findAll() {
        return new ArrayList<>(storeById.values());
    }

    @Override
    public void save(Lecture lecture) {
        if (lecture == null) return;

        String lectureId = lecture.getLectureId();
        if (lectureId != null) {
            storeById.put(lectureId, lecture);

            int numericId = extractNumericId(lectureId);
            if (numericId > 0) {
                storeByNumericId.put(numericId, lecture);
            }
        }
    }
}
