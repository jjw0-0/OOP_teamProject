# Service 작성 가이드

## Service란?

Service는 비즈니스 로직을 처리하는 계층입니다. Controller와 Repository 사이에서 핵심 기능을 수행합니다.

## Service의 역할

1. 비즈니스 로직 구현
2. 데이터 검증 및 유효성 검사
3. 여러 Repository를 조합하여 복잡한 작업 수행
4. Entity를 DTO로 변환
5. 트랜잭션 관리
6. 예외 처리 및 에러 응답 생성

## Service 작성 시 포함해야 할 요소

### 1. 필드 선언
- Repository 인스턴스: 데이터 접근을 위한 Repository
- 필요한 경우 다른 Service 인스턴스도 포함 가능

### 2. 생성자
- Repository를 매개변수로 받아서 필드 초기화
- 의존성 주입(Dependency Injection) 패턴 사용

### 3. 비즈니스 메서드
각 비즈니스 메서드는 다음 순서로 작성:
- Request DTO를 매개변수로 받음
- 데이터 검증 및 유효성 검사
- Repository를 통해 데이터 조회/저장
- Entity를 DTO로 변환
- Response DTO 반환

### 4. 검증 메서드
- 비즈니스 규칙 검증
- 데이터 유효성 검사
- private 메서드로 작성

### 5. 변환 메서드
- Entity를 DTO로 변환
- DTO를 Entity로 변환
- private 또는 package-private 메서드로 작성

## Service 작성 순서

1. 필요한 Repository를 필드로 선언
2. 생성자에서 Repository를 받아 초기화
3. public 비즈니스 메서드 작성(Request DTO → Response DTO)
4. private 검증 메서드 작성
5. private 변환 메서드 작성

## Service 메서드 구현 패턴

Service 메서드는 항상 **[검증 -> 처리 -> 반환]** 3단계를 따릅니다.

```java
public class LectureService {
    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;

    public LectureService(LectureRepository lectureRepository, UserRepository userRepository) {
        this.lectureRepository = lectureRepository;
        this.userRepository = userRepository;
    }

    // 예시: 수강 신청 기능
    public EnrollResponse enrollLecture(EnrollRequest request) {
        // 1. 검증 (Validation)
        Lecture lecture = lectureRepository.findById(request.getLectureId());
        if (lecture == null) {
            return EnrollResponse.failure("강의를 찾을 수 없습니다.");
        }
        
        // Entity 내부의 비즈니스 로직 활용 (캡슐화)
        if (lecture.isFull()) {
            return EnrollResponse.failure("정원이 마감되었습니다.");
        }

        // 2. 처리 (Process & Save)
        lecture.increaseEnrollment(); // 상태 변경
        lectureRepository.save(lecture); // 변경사항 저장

        // 3. 반환 (Return Response DTO)
        return EnrollResponse.success(lecture.getName() + " 신청 완료");
    }
}
```

## 프로젝트의 Service 목록

### 1. AuthService
인증 관련 비즈니스 로직을 처리합니다.

#### 주요 메서드
- login: 로그인 처리
- register: 회원가입 처리
- validatePassword: 비밀번호 검증
- isDuplicateId: 아이디 중복 확인

#### 사용하는 Repository
- UserRepository

### 2. LectureService
강의 관련 비즈니스 로직을 처리합니다.

#### 주요 메서드
강의 조회/검색:
- getAllLectures: 전체 강의 목록 조회
- getLecturesBySubject: 과목별 강의 목록 조회
- searchLectures: 강의 검색
- getLectureDetail: 강의 상세 정보 조회

수강 신청/취소:
- enrollLecture: 강의 신청
- cancelEnrollment: 수강 취소
- checkTimeConflict: 시간 충돌 확인

결제 처리:
- processPayment: 결제 처리
- calculateTotalAmount: 총 금액 계산

내 강의:
- getMyEnrollments: 내가 신청한 강의 목록 조회

#### 사용하는 Repository
- LectureRepository
- UserRepository
- PaymentRepository
- TextbookRepository

### 3. ReviewService
리뷰 관련 비즈니스 로직을 처리합니다.

#### 주요 메서드
- createReview: 리뷰 작성
- getReviewsByTarget: 대상(강의/강사)별 리뷰 조회
- calculateAverageRating: 평균 평점 계산

#### 사용하는 Repository
- ReviewRepository
- UserRepository

### 4. UserService
사용자 정보 관련 비즈니스 로직을 처리합니다.

#### 주요 메서드
- getUserProfile: 사용자 프로필 조회
- getPaymentHistory: 결제 내역 조회

#### 사용하는 Repository
- UserRepository
- PaymentRepository
- LectureRepository

## Service 메서드 작성 패턴

### 패턴 1: 조회 메서드
1. Request DTO로 검색 조건 받기
2. Repository에서 Entity 조회
3. Entity를 Response DTO로 변환
4. Response DTO 반환

### 패턴 2: 생성/수정 메서드
1. Request DTO 받기
2. 데이터 검증(비즈니스 규칙 확인)
3. Request DTO를 Entity로 변환
4. Repository를 통해 Entity 저장
5. 결과를 Response DTO로 반환

### 패턴 3: 복합 작업 메서드
1. Request DTO 받기
2. 여러 검증 수행
3. 여러 Repository를 순차적으로 호출
4. 각 단계의 결과를 확인하고 롤백 처리
5. 최종 결과를 Response DTO로 반환

## 비즈니스 로직 예시

### 로그인 처리 로직
1. Request DTO에서 userId와 password 추출
2. UserRepository에서 userId로 사용자 조회
3. 사용자가 없으면 실패 Response 반환
4. 비밀번호 일치 확인
5. 일치하지 않으면 실패 Response 반환
6. 성공 시 사용자 정보를 포함한 성공 Response 반환

### 강의 신청 처리 로직
1. Request DTO에서 신청 정보 추출
2. 강의 존재 여부 확인
3. 정원 초과 여부 확인
4. 학년 조건 확인
5. 시간 충돌 확인
6. 결제 처리
7. 사용자의 수강 목록에 추가
8. 강의의 현재 수강생 수 증가
9. 성공 Response 반환(결제 ID 포함)

### 리뷰 작성 처리 로직
1. Request DTO에서 리뷰 정보 추출
2. 대상(강의/강사) 존재 여부 확인
3. 사용자의 수강 여부 확인
4. 중복 리뷰 확인
5. 리뷰 Entity 생성
6. Repository를 통해 리뷰 저장
7. 대상의 평균 평점 재계산
8. 성공 Response 반환

## 검증 메서드 작성 예시

### 비밀번호 검증
- 길이 확인(최소 8자)
- 영문, 숫자 포함 확인
- 특수문자 포함 확인(선택)

### 시간 충돌 확인
- 사용자의 기존 수강 목록 조회
- 각 강의의 요일과 시간 확인
- 신청하려는 강의와 겹치는지 확인

### 정원 초과 확인
- 강의의 최대 정원 조회
- 현재 수강생 수 조회
- 비교하여 신청 가능 여부 반환

## Entity와 DTO 변환 예시

### Entity → DTO 변환
Entity의 필드를 추출하여 DTO 생성자에 전달합니다.
필요한 경우 여러 Entity의 정보를 조합할 수 있습니다.
계산된 값(평균 평점, 다음 수업 날짜 등)을 추가할 수 있습니다.

### DTO → Entity 변환
Request DTO의 필드를 추출하여 Entity 생성자에 전달합니다.
필요한 경우 기본값을 설정합니다.
생성 시간, ID 등 자동 생성되는 필드는 별도로 설정합니다.

## Service 작성 시 주의사항

### 해야 할 것
- 비즈니스 로직은 반드시 Service에 작성
- 데이터 검증은 Service에서 수행
- Entity와 DTO 변환은 Service에서 처리
- 예외 상황에 대한 명확한 응답 반환
- 여러 Repository를 조합하여 복잡한 작업 수행

### 하지 말아야 할 것
- UI 관련 코드(JOptionPane 등) 포함 금지
- View 클래스 참조 금지
- 직접적인 파일/DB 접근(Repository를 통해서만)
- DTO 없이 여러 개의 단순 타입 매개변수 사용
- 비즈니스 로직을 Controller에 작성

## Response DTO 생성 가이드

### 성공 Response
- success = true
- message = 성공 메시지
- errorType = NONE
- 필요한 데이터 포함

### 실패 Response
- success = false
- message = 실패 이유 설명
- errorType = 구체적인 에러 타입
- 데이터 = null

### 에러 타입 정의
각 Service에서 발생 가능한 에러를 Enum으로 정의합니다.
- 명확한 에러 타입으로 Controller가 적절한 처리 가능
- 동일한 실패라도 원인에 따라 다른 안내 메시지 제공 가능

## 트랜잭션 관리

### 단일 작업
하나의 Repository 메서드만 호출하는 경우 별도 처리 불필요

### 복합 작업
여러 Repository 메서드를 호출하는 경우:
- 각 단계의 성공/실패를 확인
- 실패 시 이전 단계를 롤백(원래 상태로 복구)
- 모든 단계 성공 시에만 최종 커밋

### 롤백 처리 예시
강의 신청 시:
1. 결제 처리 성공
2. 사용자 수강 목록 추가 성공
3. 강의 수강생 수 증가 실패
4. 롤백: 사용자 수강 목록에서 제거, 결제 취소

## Service 간 의존성

### 허용되는 경우
- 하나의 Service가 다른 Service를 호출 가능
- 순환 의존성이 없어야 함

### 주의사항
- Service 간 의존이 너무 많으면 리팩토링 고려
- 공통 로직은 별도 Util 클래스로 분리
- Service가 너무 비대해지면 분리 고려
