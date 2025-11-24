package com.project.app.model;

/**
 * User 엔티티
 *
 * - 로그인/회원가입 등에서 공통으로 사용하는 사용자 정보
 * - 현재는 최소 필드만 정의 (임시, 추후 요구사항에 맞게 확장 가능)
 *
 * grade 필드 규칙:
 * 1 = 고1
 * 2 = 고2
 * 3 = 고3
 * 4 = N수
 */
public class User {

    private final String id;
    private final String password;
    private final String name;
    private final int grade;   // 1: 고1, 2: 고2, 3: 고3, 4: N수
    private final String birth; // YYYY-MM-DD 형식

    /**
     * 전체 필드를 받는 생성자
     */
    public User(String id, String password, String name, int grade, String birth) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.grade = grade;
        this.birth = birth;
    }

    /**
     * birth가 아직 필요 없을 때 사용할 수 있는 보조 생성자
     * (birth는 null로 설정)
     */
    public User(String id, String password, String name, int grade) {
        this(id, password, name, grade, null);
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public int getGrade() {
        return grade;
    }

    public String getBirth() {
        return birth;
    }

    /**
     * UI 학년 표현하는 헬퍼 메서드
     *
     * @return "고1", "고2", "고3", "N수", "미지정" 중 하나
     */
    public String getGradeLabel() {
        return switch (grade) {
            case 1 -> "고1";
            case 2 -> "고2";
            case 3 -> "고3";
            case 4 -> "N수";
            default -> "미지정";
        };
    }
}
