package com.project.app.repository;

import com.project.app.model.Instructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Instructor Repository Implementation
 *
 * 특징:
 * - 초기 로드 시 파일에서 강사 정보를 읽어옴
 * - 강사 추가/삭제 없음 (고정된 데이터)
 * - 학생 추가/제거 시에만 파일에 저장
 * - 메모리 캐시 방식으로 빠른 조회 제공
 *
 */
public class InstructorRepositoryImpl implements InstructorRepository {

    private static final String DATA_FILE_PATH = "src/main/data/InstructorsData.txt";
    private static final String FIELD_DELIMITER = "/";
    private static final String LIST_DELIMITER = ",";

    private final Map<String, Instructor> instructorCache;

    /**
     * InstructorRepositoryImpl 생성자
     * 객체 생성 시 파일로부터 데이터를 로드하여 캐시를 초기화합니다.
     */
    public InstructorRepositoryImpl() {
        this.instructorCache = new HashMap<>();
        loadDataFromFile();
    }

    @Override
    public Instructor findById(String id) {
        return instructorCache.get(id);
    }

    @Override
    public List<Instructor> findAll() {
        return new ArrayList<>(instructorCache.values());
    }

    @Override
    public List<Instructor> findByAcademyId(String academyId) {
        return instructorCache.values().stream()
                .filter(instructor -> instructor.getAcademyId().equals(academyId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Instructor> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String keyword = name.toLowerCase();
        return instructorCache.values().stream()
                .filter(instructor -> instructor.getName().toLowerCase().contains(keyword))
                .collect(Collectors.toList());
    }

    @Override
    public boolean addStudentToInstructor(String instructorId, String studentId) {
        Instructor instructor = instructorCache.get(instructorId);
        if (instructor == null || studentId == null || studentId.trim().isEmpty()) {
            return false;
        }

        // 이미 존재하는 학생인지 확인
        if (instructor.hasStudent(studentId)) {
            return false;
        }

        instructor.addStudent(studentId);
        saveDataToFile();
        return true;
    }

    @Override
    public List<String> getStudentIdsByInstructor(String instructorId) {
        Instructor instructor = instructorCache.get(instructorId);
        if (instructor == null) {
            return new ArrayList<>();
        }
        return instructor.getStudentIds();
    }

    // ========== Private Helper Methods ==========
    // 파일 데이터 로딩, 저장, 파싱 등 내부 로직을 처리하는 보조 메서드들입니다.

    /**
     * 데이터 파일(`DATA_FILE_PATH`)로부터 강사 정보를 로드하여 `instructorCache`를 초기화합니다.
     * 파일이 존재하지 않거나 데이터를 읽는 도중 오류가 발생하면 콘솔에 에러 메시지를 출력합니다.
     */
    private void loadDataFromFile() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(DATA_FILE_PATH));
            // 첫 번째 줄(헤더)을 제외하고 각 라인을 파싱하여 캐시에 저장
            lines.stream().skip(1).forEach(line -> {
                Instructor instructor = parseLineToInstructor(line);
                if (instructor != null) {
                    instructorCache.put(instructor.getId(), instructor);
                }
            });
            System.out.println("Instructor data loaded: " + instructorCache.size() + " instructors");
        } catch (IOException e) {
            System.err.println("Error reading data file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * `instructorCache`에 저장된 현재 강사 데이터를 `DATA_FILE_PATH` 파일에 저장합니다.
     * 파일에 데이터를 쓰는 도중 오류가 발생하면 콘솔에 에러 메시지를 출력합니다.
     */
    private void saveDataToFile() {
        try {
            List<String> lines = new ArrayList<>();
            // 헤더 추가
            lines.add("InstructorID/InstructorName/AcademyID/Introduction/Subject/TextbookIDs/LectureIDs/StudentIDs");
            // 각 강사 정보를 라인으로 변환하여 추가
            instructorCache.values().forEach(instructor -> lines.add(instructorToLine(instructor)));
            Files.write(Paths.get(DATA_FILE_PATH), lines);
            System.out.println("Instructor data saved: " + instructorCache.size() + " instructors");
        } catch (IOException e) {
            System.err.println("Error writing data file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 데이터 파일의 한 줄 문자열을 파싱하여 `Instructor` 객체로 변환합니다.
     *
     * @param line 데이터 파일의 한 줄에 해당하는 문자열 (예: "id/name/academyId/introduction/subject/textbookId/lectureIds/studentIds")
     * @return 파싱에 성공하면 `Instructor` 객체, 데이터 형식 오류나 파싱 중 예외 발생 시 `null`
     */
    private Instructor parseLineToInstructor(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        try {
            String[] parts = line.split(FIELD_DELIMITER, -1);
            if (parts.length < 8) {
                System.err.println("Invalid data format: " + line);
                return null;
            }

            String id = parts[0].trim();
            String name = parts[1].trim();
            String academyId = parts[2].trim();
            String introduction = parts[3].trim();
            String subject = parts[4].trim();

            List<String> textbookIds = parseList(parts[5]);
            String textbookId = textbookIds.isEmpty() ? null : textbookIds.get(0); // 더 명시적인 코드로 변경
            List<String> lectureIds = parseList(parts[6]);
            List<String> studentIds = parseList(parts[7]);

            return new Instructor(id, name, academyId, introduction, subject,
                    textbookId, lectureIds, studentIds);

        } catch (Exception e) {
            System.err.println("Data parsing error: " + line);
e.printStackTrace();
            return null;
        }
    }

    /**
     * `Instructor` 객체를 데이터 파일에 저장하기 적합한 한 줄 문자열로 변환합니다.
     * 각 필드는 `FIELD_DELIMITER`로 구분됩니다.
     *
     * @param instructor 문자열로 변환할 `Instructor` 객체
     * @return `Instructor` 객체의 필드들을 조합한 한 줄 문자열
     */
    private String instructorToLine(Instructor instructor) {
        return String.join(FIELD_DELIMITER,
                instructor.getId(),
                instructor.getName(),
                instructor.getAcademyId(),
                instructor.getIntroduction(),
                instructor.getSubject() != null ? instructor.getSubject() : "",
                instructor.getTextbookId() != null ? instructor.getTextbookId() : "", // getTextbookId() 사용
                listToString(instructor.getLectureIds()),
                listToString(instructor.getStudentIds())
        );
    }

    /**
     * 콤마(`LIST_DELIMITER`)로 구분된 문자열을 파싱하여 문자열 리스트(`List<String>`)로 변환합니다.
     * 입력 문자열이 `null`이거나 비어있으면 빈 리스트를 반환합니다.
     *
     * @param str 파싱할 콤마로 구분된 문자열
     * @return 파싱된 문자열 요소들을 담은 `List<String>`
     */
    private List<String> parseList(String str) {
        if (str == null || str.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(str.split(LIST_DELIMITER))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * 문자열 리스트(`List<String>`)를 콤마(`LIST_DELIMITER`)로 구분된 단일 문자열로 변환합니다.
     * 입력 리스트가 `null`이거나 비어있으면 빈 문자열을 반환합니다.
     *
     * @param list 콤마로 구분된 문자열로 변환할 `List<String>`
     * @return `List<String>`의 요소들을 콤마로 연결한 단일 문자열
     */
    private String listToString(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return String.join(LIST_DELIMITER, list);
    }
}
