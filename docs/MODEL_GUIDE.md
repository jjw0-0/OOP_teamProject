# Model 작성 가이드

## Model(Entity)란?

Model은 비즈니스 도메인의 핵심 개념을 표현하는 객체입니다. 데이터베이스 테이블과 매핑되는 엔티티로, 애플리케이션의 데이터 구조를 정의합니다.

## Model의 역할

1. 데이터 구조 정의
2. 도메인 개념 표현
3. 엔티티 간 관계 정의
4. 기본적인 도메인 로직 포함 가능
5. 데이터 무결성 보장

## Model 작성 시 포함해야 할 요소

### 1. 필드 선언
- 모든 필드를 private로 선언
- 기본 타입(int, long) 대신 참조 타입(Integer, Long) 사용 권장
- 컬렉션 타입(List, Set)은 초기화하여 null 방지

### 2. 생성자
- 필수 필드를 매개변수로 받는 생성자 작성
- 기본 생성자(매개변수 없는 생성자) 제공
- 생성 시 검증이 필요한 경우 생성자에서 처리

### 3. Getter 메서드
- 모든 필드에 대한 Getter 메서드 작성
- boolean 타입은 is 접두사 사용

### 4. Setter 메서드
- 변경 가능한 필드에만 Setter 메서드 작성
- 불변 필드(ID, 생성일시 등)는 Setter 제공하지 않음
- Setter 대신 의미 있는 메서드명 사용 권장

### 5. 비즈니스 메서드
- 간단한 도메인 로직은 Entity에 포함 가능
- 예: 할인 적용, 재고 감소, 상태 변경 등

## 💡 Entity(Model) 코드 템플릿

```java
public class Lecture {
    // 1. 필드 (private)
    private int id;              // ID는 보통 DB나 Repository에서 부여
    private String name;
    private int currentEnrollment;
    private int capacity;

    // 2. 생성자 (ID 제외, 필수 데이터만)
    public Lecture(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.currentEnrollment = 0; // 초기값 설정
    }
    
    // 3. ID Setter (필요한 경우에만 제한적으로 허용)
    public void setId(int id) {
        this.id = id;
    }

    // 4. Getter (모든 필드)
    public String getName() { return name; }
    public int getCurrentEnrollment() { return currentEnrollment; }

    // 5. 비즈니스 로직 (Setter 대신 사용)
    // ❌ bad: setCurrentEnrollment(currentEnrollment + 1);
    // ⭕ good: 의미 있는 메서드 제공
    public void increaseEnrollment() {
        if (currentEnrollment >= capacity) {
            throw new IllegalStateException("정원이 초과되었습니다.");
        }
        this.currentEnrollment++;
    }
}
```

## 프로젝트의 Model 목록

### 1. User
사용자 정보를 표현하는 엔티티입니다.

#### 주요 필드
- id: 사용자 ID(String)
- password: 비밀번호(String)
- name: 이름(String)
- birthDate: 생년월일(String)
- grade: 학년(int)
- enrolledLectures: 신청한 강의 ID 목록(List<Integer>)
- paymentHistory: 결제 내역 ID 목록(List<Integer>)

#### 비즈니스 메서드
- addEnrolledLecture: 수강 목록에 강의 추가
- removeEnrolledLecture: 수강 목록에서 강의 제거
- addPayment: 결제 내역 추가

### 2. Lecture
강의 정보를 표현하는 엔티티입니다.

#### 주요 필드
기본 정보:
- id: 강의 ID(int)
- academyName: 학원 이름(String)
- name: 강의명(String)
- subject: 과목(String)

강사/교재:
- instructorName: 강사명(String)
- textbookName: 교재명(String)

가격/정원:
- price: 강의료(int)
- capacity: 최대 정원(int)
- currentEnrollment: 현재 수강생 수(int)

시간/장소:
- dayOfWeek: 요일(String)
- time: 시간(String)
- location: 장소(String)

상세 정보:
- rating: 평점(double)
- description: 강의 설명(String)
- syllabus: 강의 계획서(String)
- thumbnailPath: 썸네일 경로(String)

#### 비즈니스 메서드
- increaseEnrollment: 수강생 수 증가
- decreaseEnrollment: 수강생 수 감소
- isFull: 정원 초과 여부 확인
- getRemainingSeats: 남은 자리 수 반환

### 3. Instructor
강사 정보를 표현하는 엔티티입니다.

#### 주요 필드
- id: 강사 ID(int)
- name: 강사명(String)
- profileImagePath: 프로필 이미지 경로(String)
- introduction: 소개(String)

### 4. Textbook
교재 정보를 표현하는 엔티티입니다.

#### 주요 필드
- id: 교재 ID(int)
- name: 교재명(String)
- price: 가격(int)
- subject: 과목(String)
- lectureId: 연관 강의 ID(int)
- instructorName: 저자/강사명(String)

### 5. Review
리뷰 정보를 표현하는 엔티티입니다.

#### 주요 필드
- id: 리뷰 ID(int)
- userId: 작성자 ID(String)
- targetType: 대상 타입(String) - "LECTURE" 또는 "INSTRUCTOR"
- targetId: 대상 ID(int)
- rating: 평점(double)
- content: 리뷰 내용(String)
- createdAt: 작성 시간(String)

### 6. Payment
결제 내역 정보를 표현하는 엔티티입니다.

#### 주요 필드
- id: 결제 ID(int)
- userId: 사용자 ID(String)
- lectureId: 강의 ID(int)
- amount: 결제 금액(int)
- purchasedTextbook: 교재 구매 여부(boolean)
- paymentMethod: 결제 수단(String)
- paymentDate: 결제 일시(String)
- status: 결제 상태(String) - "COMPLETED", "CANCELLED"

#### 비즈니스 메서드
- cancel: 결제 취소
- isCompleted: 결제 완료 여부 확인

## Model 작성 패턴

### 기본 패턴
1. 모든 필드를 private로 선언
2. 필수 필드를 받는 생성자 작성
3. 기본 생성자 작성
4. 모든 필드에 대한 Getter 작성
5. 필요한 필드에 대한 Setter 작성
6. 의미 있는 비즈니스 메서드 작성

### ID 필드
- 각 Entity는 고유 식별자(ID)를 가져야 함
- ID는 불변(Setter 제공하지 않음)
- 자동 생성 또는 외부에서 할당

### 컬렉션 필드
- List, Set 등은 필드 선언 시 초기화
- null 대신 빈 컬렉션 반환
- 외부에서 직접 수정 불가하도록 방어적 복사 고려

### 날짜/시간 필드
- String 타입 사용(간단한 프로젝트의 경우)
- 형식 통일(예: "2025-01-01 12:00:00")

## 비즈니스 메서드 작성 예시

### 상태 변경 메서드
Entity의 상태를 변경하는 메서드입니다.
- 메서드명은 동사로 시작(add, remove, increase, decrease 등)
- 상태 변경 시 유효성 검사 수행
- 불가능한 상태 변경은 예외 발생 또는 false 반환

### 조회 메서드
Entity의 상태를 조회하는 메서드입니다.
- 메서드명은 get 또는 is로 시작
- 계산된 값을 반환할 수 있음
- 부수효과(side effect) 없음

### 검증 메서드
Entity의 유효성을 검사하는 메서드입니다.
- 메서드명은 is 또는 can으로 시작
- boolean 반환
- 비즈니스 규칙 캡슐화

## Model 작성 시 주의사항

### 해야 할 것
- 도메인 개념을 명확하게 표현
- 불변 필드는 Setter 제공하지 않기
- 컬렉션은 초기화하여 null 방지
- 간단한 도메인 로직은 Entity에 포함
- 유효성 검증은 생성자 또는 Setter에서 수행

### 하지 말아야 할 것
- UI 관련 코드 포함 금지
- 데이터 접근 로직(DB 쿼리 등) 포함 금지
- 복잡한 비즈니스 로직은 Service로 이동
- 다른 계층(Controller, Service 등) 참조 금지
- 모든 필드에 Setter 제공하지 않기

## Model과 DTO의 차이

### Model(Entity)
- 목적: 도메인 개념 표현
- 사용 위치: Repository, Service 내부
- 필드: 도메인의 모든 정보
- 로직: 간단한 도메인 로직 포함 가능
- 변경: 도메인이 안정적이므로 변경 빈도 낮음

### DTO
- 목적: 데이터 전송
- 사용 위치: Controller, View
- 필드: 필요한 정보만
- 로직: 로직 없음
- 변경: 요구사항에 따라 변경 빈도 높음

## Entity 설계 원칙

### 1. 단일 책임 원칙
각 Entity는 하나의 도메인 개념만 표현합니다.

### 2. 캡슐화
내부 상태를 외부에 직접 노출하지 않습니다.
Setter 대신 의미 있는 메서드명을 사용합니다.

### 3. 일관성
Entity의 상태는 항상 유효해야 합니다.
생성자 또는 메서드에서 유효성을 검증합니다.

### 4. 불변성
변경 불가능한 필드는 final로 선언하거나 Setter를 제공하지 않습니다.

## 연관 관계 표현

### 단방향 참조
- 한쪽 Entity만 다른 Entity를 참조
- ID만 저장하는 방식 권장(간단한 프로젝트)

### 양방향 참조
- 양쪽 Entity가 서로를 참조
- 순환 참조 주의
- 필요한 경우에만 사용

### ID 참조 방식
- Entity 객체 대신 ID만 저장
- 복잡도 감소
- 순환 참조 방지
- 프로젝트에서 권장하는 방식

## Entity 생성 및 수정

### 생성
- 생성자를 통해 필수 필드 설정
- 선택적 필드는 Setter로 설정
- 생성 시 기본값 설정

### 수정
- Setter 대신 의미 있는 메서드 사용 권장
- 수정 시 유효성 검증
- 수정 불가능한 필드는 Setter 제공 안 함

### 삭제
- 물리적 삭제: DB에서 완전히 제거
- 논리적 삭제: 상태 필드로 관리(예: isDeleted)
- 프로젝트 요구사항에 따라 선택
