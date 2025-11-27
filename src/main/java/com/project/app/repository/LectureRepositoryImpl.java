package com.project.app.repository;

import com.project.app.model.Lecture;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LectureRepository 구현체 (인메모리 버전)
 *
 * - 애플리케이션 실행 중 메모리에 강의 정보를 보관
 * - 데모용 기본 강의 데이터 몇 개를 미리 등록
 */
public class LectureRepositoryImpl implements LectureRepository {

    private static final LectureRepositoryImpl instance = new LectureRepositoryImpl();

    public static LectureRepositoryImpl getInstance() {
        return instance;
    }

    private final Map<Integer, Lecture> store = new ConcurrentHashMap<>();

    private LectureRepositoryImpl() {
        // 데모용 강의 데이터
        save(new Lecture(
                1,
                "고등 수학 I",
                "수학",
                "김선생",
                "일타학원",
                "월, 수",
                "18:00~20:00",
                "강의실 301",
                150_000,
                "수학의 정석",
                25_000
        ));

        save(new Lecture(
                2,
                "물리 기본 개념",
                "과학",
                "이선생",
                "일타학원",
                "수, 금",
                "19:00~21:00",
                "강의실 202",
                140_000,
                "고등 물리 개념서",
                22_000
        ));

        save(new Lecture(
                3,
                "한국사 입문",
                "한국사",
                "박선생",
                "일타학원",
                "월",
                "17:00~19:00",
                "강의실 105",
                120_000,
                "한국사 바로알기",
                15_000
        ));

        save(new Lecture(
                4,
                "문학 읽기 I",
                "국어",
                "최선생",
                "일타학원",
                "화",
                "18:30~20:00",
                "강의실 210",
                135_000,
                "현대문학 작품 읽기",
                20_000
        ));

        save(new Lecture(
                5,
                "수학 II",
                "수학",
                "정선생",
                "일타학원",
                "목, 금",
                "18:00~20:30",
                "강의실 302",
                155_000,
                "수학의 정석 (심화)",
                28_000
        ));
    }

    @Override
    public Lecture findById(int id) {
        return store.get(id);
    }

    @Override
    public List<Lecture> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void save(Lecture lecture) {
        if (lecture == null) return;
        store.put(lecture.getId(), lecture);
    }
}
