package com.project.app.service;

import com.project.app.model.Lecture;
import com.project.app.repository.LectureRepositoryImpl;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 마이페이지용 서비스
 *
 * - UserData.txt, MyLectureData.txt, PaymentData.txt, LectureData.txt 를 읽어서
 *   "내 정보 / 내 강의 / 내 일정 / 결제 내역" 데이터를 만들어줌
 */
public class MyPageService {

    // ===== 파일 이름 상수 =====
    private static final String USER_DATA_FILE = "UserData.txt";
    private static final String MY_LECTURE_DATA_FILE = "MyLectureData.txt";
    private static final String PAYMENT_DATA_FILE = "PaymentData.txt";
    private static final String LECTURE_DATA_FILE = "LectureData.txt";

    // Lecture 데이터는 기존 Repository 재사용
    private final LectureRepositoryImpl lectureRepository;

    // 싱글톤
    private static final MyPageService instance = new MyPageService(LectureRepositoryImpl.getInstance());

    public static MyPageService getInstance() {
        return instance;
    }

    private MyPageService(LectureRepositoryImpl lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    // ===== 내부 DTO =====

    /**
     * UserData.txt 한 줄을 파싱한 정보
     */
    public static class UserRecord {
        public final String userId;
        public final String password;
        public final String name;
        public final String birth;       // YYYY-MM-DD
        public final String gradeLabel;  // "고1", "고2", ...
        public final List<String> lectureIds; // 신청 강의 ID 목록
        public final List<String> paymentIds; // 결제 ID 목록

        public UserRecord(String userId, String password, String name,
                          String birth, String gradeLabel,
                          List<String> lectureIds, List<String> paymentIds) {
            this.userId = userId;
            this.password = password;
            this.name = name;
            this.birth = birth;
            this.gradeLabel = gradeLabel;
            this.lectureIds = lectureIds;
            this.paymentIds = paymentIds;
        }
    }

    /**
     * 시간표/내 강의 공통으로 쓰는 강의 요약 정보
     */
    public static class MyLectureInfo {
        public final String lectureId;
        public final String title;
        public final String subject;
        public final String academy;
        public final String instructor;
        public final String dayOfWeek;   // 월, 화, 수, ...
        public final String time;        // "7~10" 같이 LectureData.txt "시간" 컬럼 그대로
        public final String room;
        public final String status;      // 수강중, 수강예정 등

        public MyLectureInfo(String lectureId,
                             String title,
                             String subject,
                             String academy,
                             String instructor,
                             String dayOfWeek,
                             String time,
                             String room,
                             String status) {
            this.lectureId = lectureId;
            this.title = title;
            this.subject = subject;
            this.academy = academy;
            this.instructor = instructor;
            this.dayOfWeek = dayOfWeek;
            this.time = time;
            this.room = room;
            this.status = status;
        }
    }

    /**
     * 결제 내역 표시용 DTO
     */
    public static class MyPaymentInfo {
        public final String paymentId;
        public final String lectureId;
        public final String lectureTitle;
        public final String paymentDate;
        public final int amount;
        public final String method;
        public final String status;

        public MyPaymentInfo(String paymentId,
                             String lectureId,
                             String lectureTitle,
                             String paymentDate,
                             int amount,
                             String method,
                             String status) {
            this.paymentId = paymentId;
            this.lectureId = lectureId;
            this.lectureTitle = lectureTitle;
            this.paymentDate = paymentDate;
            this.amount = amount;
            this.method = method;
            this.status = status;
        }
    }

    /**
     * MyLectureData.txt에 있는 추가 메타 (수강상태, 강의실, 시작/종료교시 등)
     */
    private static class MyLectureMeta {
        String status;     // 수강중, 수강예정 등
        String dayOfWeek;  // 월, 화, ...
        String startTime;  // 1
        String endTime;    // 3
        String room;       // A105
        String note;       // 비고
    }

    /**
     * LectureData.txt 에서 뽑은 시간표 정보 (요일/시간/강의실)
     */
    private static class LectureScheduleInfo {
        String dayOfWeek;  // 월, 화, ...
        String time;       // "7~10", "5~7" 등
        String room;       // "A관 2301호" 등
    }

    // ====== Public API ======

    /**
     * 현재 사용자 ID 기준으로 UserData.txt 정보 읽기
     */
    public UserRecord getUserRecord(String userId) {
        File file = new File(USER_DATA_FILE);
        System.out.println("[MyPageService] USER_DATA_FILE: " + file.getAbsolutePath());

        if (!file.exists()) {
            System.err.println("[MyPageService] UserData.txt 파일을 찾을 수 없습니다.");
            return null;
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line;
            boolean first = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // 헤더 스킵
                if (first) {
                    first = false;
                    if (line.startsWith("사용자ID")) {
                        continue;
                    }
                }

                String[] p = line.split("/");
                if (p.length < 5) continue;

                String id = p[0].trim();
                if (!id.equals(userId)) continue;

                String pw          = p[1].trim();
                String name        = p[2].trim();
                String birth       = p[3].trim();
                String gradeLabel  = p[4].trim();

                // 신청 강의 목록
                List<String> lectureIds = new ArrayList<>();
                if (p.length >= 6 && !p[5].trim().isEmpty()) {
                    for (String lecId : p[5].split(",")) {
                        String t = lecId.trim();
                        if (!t.isEmpty()) lectureIds.add(t);
                    }
                }

                // 결제 ID 목록 (사용할 수도 있고, 안 쓸 수도 있음)
                List<String> paymentIds = new ArrayList<>();
                if (p.length >= 7 && !p[6].trim().isEmpty()) {
                    for (String payId : p[6].split(",")) {
                        String t = payId.trim();
                        if (!t.isEmpty()) paymentIds.add(t);
                    }
                }

                return new UserRecord(id, pw, name, birth, gradeLabel, lectureIds, paymentIds);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * "내 강의" / "내 일정" 에 사용할 강의 리스트
     */
    public List<MyLectureInfo> getMyLectures(String userId) {
        UserRecord user = getUserRecord(userId);
        if (user == null || user.lectureIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 1) MyLectureData.txt 메타 정보
        Map<String, MyLectureMeta> metaMap = loadMyLectureMeta();
        // 2) LectureData.txt 에서 요일/시간/강의실 전용 정보
        Map<String, LectureScheduleInfo> scheduleMap = loadLectureScheduleFromFile();

        // 기본 Lecture 정보 (제목, 과목, 학원, 강사 등)
        List<Lecture> lectures = lectureRepository.findById(user.lectureIds);
        List<MyLectureInfo> result = new ArrayList<>();

        for (Lecture lec : lectures) {
            if (lec == null) continue;

            String lecId = lec.getLectureId();
            MyLectureMeta meta = metaMap.get(lecId);
            LectureScheduleInfo sched = scheduleMap.get(lecId);

            // ===== 요일 =====
            String dayOfWeek;
            if (meta != null && meta.dayOfWeek != null && !meta.dayOfWeek.isEmpty()) {
                dayOfWeek = meta.dayOfWeek;
            } else if (sched != null && sched.dayOfWeek != null && !sched.dayOfWeek.isEmpty()) {
                dayOfWeek = sched.dayOfWeek;   // LectureData.txt 기반
            } else if (lec.getDayOfWeek() != null && !lec.getDayOfWeek().isEmpty()) {
                dayOfWeek = lec.getDayOfWeek(); // 혹시 Repository가 제대로 세팅한 경우
            } else {
                dayOfWeek = "-";
            }

            // ===== 시간 (LectureData.txt "시간" 컬럼 그대로) =====
            String time;
            if (sched != null && sched.time != null && !sched.time.isEmpty()) {
                // 예: "7~10", "5~7"
                time = sched.time;
            } else if (meta != null && meta.startTime != null && meta.endTime != null
                    && !meta.startTime.isEmpty() && !meta.endTime.isEmpty()) {
                // 혹시 LectureData.txt를 못 읽는 경우 최소 fallback
                time = meta.startTime + "~" + meta.endTime;
            } else if (lec.getTime() != null && !lec.getTime().isEmpty()) {
                time = lec.getTime();
            } else {
                time = "-";
            }

            // ===== 강의실 =====
            String room;
            if (meta != null && meta.room != null && !meta.room.isEmpty()) {
                // MyLectureData.txt 에 강의실이 따로 지정된 경우 우선
                room = meta.room;
            } else if (sched != null && sched.room != null && !sched.room.isEmpty()) {
                // LectureData.txt 에서 파싱한 강의실
                room = sched.room;
            } else if (lec.getLocation() != null && !lec.getLocation().isEmpty()) {
                // Lecture 모델에 강의실이 있을 경우
                room = lec.getLocation();
            } else {
                room = "-";
            }

            // ===== 수강 상태 =====
            String status = (meta != null && meta.status != null && !meta.status.isEmpty())
                    ? meta.status
                    : "수강중";

            result.add(new MyLectureInfo(
                    lecId,
                    lec.getTitle(),
                    lec.getSubject(),
                    lec.getAcademy(),
                    lec.getInstructor(),
                    dayOfWeek,
                    time,
                    room,
                    status
            ));
        }

        return result;
    }

    public List<MyLectureInfo> getMySchedule(String userId) {
        List<MyLectureInfo> lectures = getMyLectures(userId);

        // 요일(월~일) → 시간 순으로 정렬
        lectures.sort(
                Comparator
                        .comparingInt((MyLectureInfo m) -> getDayOrder(m.dayOfWeek))
                        .thenComparing(m -> m.time)
        );

        return lectures;
    }

    /**
     * "결제 내역" 탭에 사용할 정보
     *
     * PaymentData.txt 형식 (현재 파일 기준):
     *  결제ID/사용자ID/강의ID/결제금액/교재구매여부/결제수단/결제일시/결제상태
     */
    public List<MyPaymentInfo> getMyPayments(String userId) {
        return loadPaymentsByUserId(userId);
    }

    // ===== 내부 Helper 메서드들 =====

    /**
     * MyLectureData.txt 를 읽어서 lectureId → 메타 정보 맵 생성
     *
     * 형식:
     * 수강ID/강의ID/수강상태/요일/시작시간/종료시간/강의실/비고
     */
    private Map<String, MyLectureMeta> loadMyLectureMeta() {
        Map<String, MyLectureMeta> map = new HashMap<>();

        File file = new File(MY_LECTURE_DATA_FILE);
        System.out.println("[MyPageService] MY_LECTURE_DATA_FILE: " + file.getAbsolutePath());

        if (!file.exists()) {
            System.err.println("[MyPageService] MyLectureData.txt 파일을 찾을 수 없습니다. (시간표 메타 없음)");
            return map;
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line;
            boolean first = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // 헤더 스킵
                if (first) {
                    first = false;
                    if (line.startsWith("수강ID")) {
                        continue;
                    }
                }

                String[] p = line.split("/");
                if (p.length < 8) continue;

                String lectureId   = p[1].trim();
                String status      = p[2].trim();
                String dayOfWeek   = p[3].trim();
                String startTime   = p[4].trim();
                String endTime     = p[5].trim();
                String room        = p[6].trim();
                String note        = p[7].trim();

                MyLectureMeta meta = new MyLectureMeta();
                meta.status = status;
                meta.dayOfWeek = dayOfWeek;
                meta.startTime = startTime;
                meta.endTime = endTime;
                meta.room = room;
                meta.note = note;

                map.put(lectureId, meta);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    /**
     * LectureData.txt 를 읽어서 lectureId → (요일, 시간, 강의실) 정보 맵 생성
     *
     * 형식:
     * 강의ID/학원ID/과목/년도/강의 이름/강사/교재/강의 가격/교재 가격/
     * 강의실/강의 설명/대상 학년/별점/현재 인원/정원/요일/시간/이미지파일명
     *
     *   → 단, 강의 이름/교재/설명 등에 "/" 문자가 포함된 경우가 있어
     *      앞에서부터 인덱스로 접근하지 않고
     *      "끝에서부터 N번째" 규칙으로 강의실/요일/시간을 구한다.
     *
     *      - 이미지 파일명 있는 경우: [room, desc, grade, rating, current, capacity, day, time, image]
     *      - 이미지 파일명 없는 경우: [room, desc, grade, rating, current, capacity, day, time]
     */
    private Map<String, LectureScheduleInfo> loadLectureScheduleFromFile() {
        Map<String, LectureScheduleInfo> map = new HashMap<>();

        File file = new File(LECTURE_DATA_FILE);
        System.out.println("[MyPageService] LECTURE_DATA_FILE: " + file.getAbsolutePath());

        if (!file.exists()) {
            System.err.println("[MyPageService] LectureData.txt 파일을 찾을 수 없습니다.");
            return map;
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line;
            boolean first = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // 헤더 스킵
                if (first) {
                    first = false;
                    if (line.startsWith("강의ID")) {
                        continue;
                    }
                }

                String[] p = line.split("/");
                if (p.length < 5) continue;

                String lectureId = p[0].trim();
                int n = p.length;

                String dayOfWeek = "";
                String time = "";
                String room = "";

                if (n >= 9) {
                    String last = p[n - 1].trim();
                    String secondLast = p[n - 2].trim();
                    String thirdLast = p[n - 3].trim();

                    // 이미지 파일명 여부 판단
                    boolean hasImage =
                            last.contains(".png") || last.contains(".jpg") ||
                            last.contains(".jpeg") || last.contains(".gif") ||
                            last.contains(".bmp");

                    if (hasImage) {
                        // ... room / desc / grade / rating / current / capacity / day / time / image
                        time = secondLast;      // n-2
                        dayOfWeek = thirdLast;  // n-3
                        if (n >= 9) {
                            room = p[n - 9].trim(); // room
                        }
                    } else {
                        // ... room / desc / grade / rating / current / capacity / day / time
                        time = last;            // n-1
                        dayOfWeek = secondLast; // n-2
                        if (n >= 8) {
                            room = p[n - 8].trim(); // room
                        }
                    }
                }

                LectureScheduleInfo info = new LectureScheduleInfo();
                info.dayOfWeek = dayOfWeek;
                info.time = time;
                info.room = room;
                map.put(lectureId, info);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    /**
     * 요일 문자열을 정렬 순서를 위한 숫자로 변환
     * 월(1) ~ 일(7), 그 외/알 수 없으면 99 (맨 뒤)
     */
    private int getDayOrder(String dayOfWeek) {
        if (dayOfWeek == null) return 99;

        String d = dayOfWeek.trim();
        if (d.isEmpty()) return 99;

        // "월", "월요일" 둘 다 커버할 수 있게 첫 글자만 사용
        String first = d.substring(0, 1);

        switch (first) {
            case "월": return 1;
            case "화": return 2;
            case "수": return 3;
            case "목": return 4;
            case "금": return 5;
            case "토": return 6;
            case "일": return 7;
            default:   return 99;
        }
    }

    /**
     * PaymentData.txt에서 특정 userId의 결제 목록을 읽어옴
     *
     * 형식:
     * 결제ID/사용자ID/강의ID/결제금액/교재구매여부/결제수단/결제일시/결제상태
     */
    private List<MyPaymentInfo> loadPaymentsByUserId(String userId) {
        List<MyPaymentInfo> list = new ArrayList<>();

        File file = new File(PAYMENT_DATA_FILE);
        System.out.println("[MyPageService] PAYMENT_DATA_FILE: " + file.getAbsolutePath());

        if (!file.exists()) {
            System.err.println("[MyPageService] PaymentData.txt 파일을 찾을 수 없습니다.");
            return list;
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line;
            boolean first = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // 헤더 스킵
                if (first) {
                    first = false;
                    if (line.startsWith("결제ID")) {
                        continue;
                    }
                }

                String[] p = line.split("/");
                if (p.length < 8) continue;

                String paymentId     = p[0].trim();
                String uid           = p[1].trim();
                String lectureId     = p[2].trim();
                int amount           = parseInt(p[3], 0);
                String textbookFlag  = p[4].trim();  // 교재구매여부 (필요시 활용)
                String method        = p[5].trim();
                String paymentDate   = p[6].trim();
                String status        = p[7].trim();

                if (!uid.equals(userId)) continue;

                // 강의 제목 조회
                Lecture lec = lectureRepository.findById(lectureId);
                String lectureTitle = (lec != null) ? lec.getTitle() : "(알 수 없는 강의)";

                MyPaymentInfo info = new MyPaymentInfo(
                        paymentId,
                        lectureId,
                        lectureTitle,
                        paymentDate,
                        amount,
                        method,
                        status
                );
                list.add(info);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    private int parseInt(String s, int defaultValue) {
        if (s == null) return defaultValue;
        try {
            return Integer.parseInt(s.replace(",", "").trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
