# DTO 작성 가이드

## DTO란?

DTO(Data Transfer Object)는 계층 간 데이터를 전달하는 객체입니다.

## DTO의 역할

1. 계층 간 결합도 감소: 객체 구조가 변경되어도 DTO만 수정하면 됩니다
2. 불필요한 데이터 노출 방지: View는 필요한 정보만 받습니다
3. 명확한 인터페이스: 메서드 시그니처만 봐도 어떤 데이터가 필요한지 알 수 있습니다

## DTO 종류

### 1. Request DTO
View에서 Service로 전달되는 요청 데이터입니다.

#### 특징
- 사용자 입력값을 담습니다
- 검증에 필요한 정보를 포함합니다
- 불변 객체로 만드는 것이 좋습니다

#### 사용 시점
- 생성(Create)
- 검색(Search)
- 수정(Update)

#### Request DTO (요청 데이터) 예시
```java
public class LoginRequest {
    // 1. 필드는 모두 private (불변성을 위해 final 권장)
    private final String userId;
    private final String password;

    // 2. 모든 필드를 받는 생성자
    public LoginRequest(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    // 3. Getter만 존재 (Setter 없음)
    public String getUserId() { return userId; }
    public String getPassword() { return password; }
}
```

### 2. Response DTO
Service에서 View로 반환되는 응답 데이터입니다.

#### 특징
- 작업 결과를 담습니다
- 성공/실패 여부, 메시지, 데이터를 포함합니다
- View에 표시할 정보만 선택적으로 포함합니다

#### 사용 시점
- 조회(Read)
- 생성/수정/삭제 결과

#### Response DTO (응답 데이터) 예시
```java
public class LoginResponse {
    private final boolean success;
    private final String message;
    private final String userName; // 데이터가 필요 없다면 생략 가능

    public LoginResponse(boolean success, String message, String userName) {
        this.success = success;
        this.message = message;
        this.userName = userName;
    }

    // 정적 팩토리 메서드 (성공/실패 생성을 쉽게)
    public static LoginResponse success(String userName) {
        return new LoginResponse(true, "로그인 성공", userName);
    }

    public static LoginResponse failure(String message) {
        return new LoginResponse(false, message, null);
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getUserName() { return userName; }
}
```

### 3. View DTO
View 내부에서만 사용하는 표시 전용 데이터입니다.

#### 특징
- UI 렌더링에 필요한 최소 정보만 포함합니다
- 카드, 리스트 아이템 등에 사용합니다
- Response DTO보다 더 간결합니다

#### 사용 시점
- 목록 카드 표시
- 요약 정보 표시

## 프로젝트의 DTO 목록

### 인증 관련 DTO

#### LoginRequest
- 역할: 로그인 요청 데이터
- 포함 필드: userId, password

#### LoginResponse
- 역할: 로그인 결과 반환
- 포함 필드: success, message, userId, userName, grade

#### RegisterRequest
- 역할: 회원가입 요청 데이터
- 포함 필드: userId, password, name, birthDate, grade

#### RegisterResponse
- 역할: 회원가입 결과 반환
- 포함 필드: success, message, errorType

### 강의 관련 DTO

#### LectureSearchRequest
- 역할: 강의 검색 조건
- 포함 필드: keyword, subject, academyName, targetGrade, sortOrder

#### LectureListResponse
- 역할: 강의 목록 조회 결과
- 포함 필드: lectures(List), totalCount

#### LectureCardView
- 역할: 강의 카드 UI 표시용
- 포함 필드: id, name, subject, instructorName, academyName, thumbnailPath, rating, price, dayOfWeek, time, remainingSeats

#### LectureDetailResponse
- 역할: 강의 상세 정보 조회 결과
- 포함 필드: 기본 정보, 강사/교재 정보, 가격, 정원, 시간/장소, 설명, isEnrolled, canEnroll

#### EnrollLectureRequest
- 역할: 강의 신청 요청(결제 정보 포함)
- 포함 필드: lectureId, userId, purchaseTextbook, paymentMethod

#### EnrollLectureResponse
- 역할: 강의 신청 결과(결제 ID 포함)
- 포함 필드: success, message, errorType, paymentId

### 리뷰 관련 DTO

#### CreateReviewRequest
- 역할: 리뷰 작성 요청
- 포함 필드: userId, targetType, targetId, rating, content

#### ReviewView
- 역할: 리뷰 UI 표시용
- 포함 필드: id, userName, userGrade, rating, content, createdAt

### 마이페이지 관련 DTO

#### UserProfileResponse
- 역할: 사용자 프로필 정보(결제 내역 포함)
- 포함 필드: userId, name, birthDate, grade, enrolledLectureCount, totalPaymentAmount, recentPayments

#### MyLectureListResponse
- 역할: 내 강의 목록 조회 결과
- 포함 필드: lectures(List)

#### MyLectureView
- 역할: 내 강의 카드 UI 표시용
- 포함 필드: lectureId, lectureName, instructorName, subject, dayOfWeek, time, location, nextScheduleDate

## DTO 네이밍 규칙

### Request DTO
형식: {동작}{대상}Request
예시: CreateUserRequest, SearchLectureRequest, UpdateProfileRequest

### Response DTO
형식: {대상}{동작}Response
예시: UserDetailResponse, LectureListResponse, LoginResponse

### View DTO
형식: {대상}{용도}View
예시: LectureCardView, UserSummaryView

## DTO 작성 시 포함해야 할 요소

### Request DTO 작성 시
1. 필수 입력 필드를 private로 선언
2. 선택적 입력 필드를 private로 선언
3. 필수 필드만 매개변수로 받는 생성자 작성
4. 모든 필드에 대한 Getter 메서드 작성
5. 선택적 필드에 대한 Setter 메서드 작성(필요시)

### Response DTO 작성 시
1. 반환할 데이터 필드들을 private로 선언
2. 모든 필드를 매개변수로 받는 생성자 작성
3. 모든 필드에 대한 Getter 메서드만 작성
4. 성공/실패 구분이 필요한 경우: success, message, errorType 필드 포함
5. 성공/실패 구분이 필요한 경우: 정적 팩토리 메서드(success, failure) 작성

### View DTO 작성 시
1. UI 표시에 필요한 최소 필드만 private로 선언(3~5개 정도)
2. 모든 필드를 매개변수로 받는 생성자 작성
3. 모든 필드에 대한 Getter 메서드만 작성

## DTO 작성 원칙

### 1. 단순함
로직 없이 데이터만 포함합니다.

### 2. 불변성
생성 후 변경이 불가능하도록 합니다.

### 3. 명확함
이름만 봐도 용도를 파악할 수 있어야 합니다.

## DTO vs Entity

### Entity
- 사용 위치: Repository, Service 내부
- 목적: 도메인 모델 표현
- 변경 빈도: 낮음(도메인 안정)
- 필드: 모든 정보
- 로직: 비즈니스 로직 가능

### DTO
- 사용 위치: Controller, View
- 목적: 데이터 전송
- 변경 빈도: 높음(요구사항 변경)
- 필드: 필요한 정보만
- 로직: 로직 없음

## DTO가 필요한 경우

1. 여러 필드를 함께 전달할 때
2. 검증이 필요한 입력 데이터
3. 성공/실패를 구분해야 할 때
4. View에 보여줄 데이터가 Entity와 다를 때

## DTO가 불필요한 경우

1. 단일 값(String, int) 전달
2. 내부 메서드 간 데이터 전달
3. 간단한 boolean 반환

