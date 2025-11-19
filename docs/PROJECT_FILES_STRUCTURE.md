# 프로젝트 필요 파일 목록 (최종 간소화 버전)

## 프로젝트 개요
- **주제**: 고등학생 수능 강의 오프라인 신청 프로그램
- **학원**: 메가스터디, 대성마이맥, 이투스
- **주요 기능**: 강의 검색/조회, 강의 신청, 결제, 리뷰
- **프로젝트 규모**: Toy Project (간결한 구조 우선)

---

## 📁 전체 파일 구조

```
src/main/java/com/project/app/
├── model/           (6개)  - 도메인 객체
├── dto/             (15개) - 데이터 전송 객체
├── repository/      (12개) - 데이터 접근 계층 (인터페이스 6 + 구현체 6)
├── service/         (4개)  - 비즈니스 로직
├── controller/      (6개)  - 이벤트 처리
└── view/            (7개)  - UI 화면 (대부분 이미 존재)

총 50개 파일
```

---

## 1. Model (엔티티) - 6개

### User.java
**역할**: 사용자 정보
```
- id, password, name, birthDate, grade
- enrolledLectures (신청한 강의 ID 목록)
- paymentHistory (결제 내역 ID 목록)
```

### Lecture.java
**역할**: 강의 정보
```
- id, academyName, name, subject
- instructorName, textbookName
- price, capacity, currentEnrollment
- dayOfWeek, time, location
- rating, description, syllabus
- thumbnailPath
```

### Instructor.java
**역할**: 강사 정보
```
- id, name
- profileImagePath, introduction
```

### Textbook.java
**역할**: 교재 정보
```
- id, name, price, subject
- lectureId, instructorName
```

### Review.java
**역할**: 리뷰
```
- id, userId, targetType, targetId
- rating, content, createdAt
```

### Payment.java
**역할**: 결제 내역
```
- id, userId, lectureId, amount
- purchasedTextbook, paymentMethod
- paymentDate, status
```

---

## 2. DTO - 15개 (간소화됨!)

### 인증 관련 (4개)

1. **LoginRequest.java** - 로그인 요청
2. **LoginResponse.java** - 로그인 결과
3. **RegisterRequest.java** - 회원가입 요청
4. **RegisterResponse.java** - 회원가입 결과

### 강의 관련 (6개)

5. **LectureSearchRequest.java** - 검색 조건
6. **LectureListResponse.java** - 목록 응답
7. **LectureCardView.java** - 카드 표시용 (UI)
8. **LectureDetailResponse.java** - 상세 정보
9. **EnrollLectureRequest.java** - 신청 요청 (결제 정보 포함)
10. **EnrollLectureResponse.java** - 신청 결과 (결제 ID 포함)

### 리뷰 관련 (2개)

11. **CreateReviewRequest.java** - 리뷰 작성 요청
12. **ReviewView.java** - 리뷰 표시용

### 마이페이지 관련 (3개)

13. **UserProfileResponse.java** - 사용자 프로필 (결제 내역 포함)
14. **MyLectureListResponse.java** - 내 강의 목록
15. **MyLectureView.java** - 내 강의 표시용

---

## 3. Repository - 12개

### 인터페이스 (6개)

1. **UserRepository.java**
```java
- Optional<User> findById(String userId)
- boolean existsById(String userId)
- void save(User user)
- void update(User user)
```

2. **LectureRepository.java**
```java
- List<Lecture> findAll()
- Optional<Lecture> findById(int lectureId)
- List<Lecture> findBySubject(String subject)
- List<Lecture> findByAcademy(String academyName)
- List<Lecture> searchByKeyword(String keyword)
- void update(Lecture lecture)
```

3. **InstructorRepository.java**
```java
- Optional<Instructor> findById(int instructorId)
- List<Instructor> findAll()
```

4. **TextbookRepository.java**
```java
- Optional<Textbook> findById(int textbookId)
- List<Textbook> findByLectureId(int lectureId)
```

5. **ReviewRepository.java**
```java
- List<Review> findByTargetTypeAndId(String targetType, int targetId)
- void save(Review review)
```

6. **PaymentRepository.java**
```java
- List<Payment> findByUserId(String userId)
- void save(Payment payment)
- void updateStatus(int paymentId, String status)
```

### 구현체 (6개)

7. **UserRepositoryImpl.java**
8. **LectureRepositoryImpl.java**
9. **InstructorRepositoryImpl.java**
10. **TextbookRepositoryImpl.java**
11. **ReviewRepositoryImpl.java**
12. **PaymentRepositoryImpl.java**

---

## 4. Service - 4개 (핵심 간소화!)

### 1. AuthService.java
**역할**: 인증 (로그인, 회원가입)
```java
- LoginResponse login(LoginRequest request)
- RegisterResponse register(RegisterRequest request)
- boolean validatePassword(String password)
- boolean isDuplicateId(String userId)
```

### 2. LectureService.java ⭐ 확장됨
**역할**: 강의 조회 + 수강 신청 + 결제 처리 (통합!)
```java
// === 강의 조회/검색 ===
- LectureListResponse getAllLectures(String sortOrder)
- LectureListResponse getLecturesBySubject(String subject, String sortOrder)
- LectureListResponse searchLectures(LectureSearchRequest request)
- LectureDetailResponse getLectureDetail(int lectureId, String userId)

// === 수강 신청/취소 ===
- EnrollLectureResponse enrollLecture(EnrollLectureRequest request)
- boolean cancelEnrollment(String userId, int lectureId)
- boolean checkTimeConflict(String userId, int lectureId)

// === 결제 처리 ===
- Payment processPayment(String userId, int lectureId, boolean purchaseTextbook, String method)
- int calculateTotalAmount(int lectureId, boolean purchaseTextbook)

// === 내 강의 ===
- MyLectureListResponse getMyEnrollments(String userId)
```

**통합 이유**: 수강 신청 = 강의 선택 + 결제 → 하나의 트랜잭션

### 3. ReviewService.java
**역할**: 리뷰 관리
```java
- void createReview(CreateReviewRequest request)
- List<ReviewView> getReviewsByTarget(String targetType, int targetId)
- double calculateAverageRating(String targetType, int targetId)
```

### 4. UserService.java
**역할**: 사용자 정보 관리
```java
- UserProfileResponse getUserProfile(String userId)
- List<Payment> getPaymentHistory(String userId)
```

---

## 5. Controller - 6개

### 1. SignInController.java
**역할**: 로그인 화면 제어
```java
- handleLogin()
- navigateToSignUp()
- navigateToHome()
```

### 2. SignUpController.java
**역할**: 회원가입 화면 제어
```java
- handleRegister()
- navigateToSignIn()
```

### 3. LectureController.java
**역할**: 강의 목록 화면 제어
```java
- handleSubjectFilter(String subject)
- handleAcademyFilter(String academy)
- handleSearch()
- handleSortChange()
- navigateToDetail(int lectureId)
```

### 4. LectureDetailController.java
**역할**: 강의 상세 화면 제어
```java
- handleEnroll()              // 신청 + 결제
- handleCancelEnrollment()    // 수강 취소
- handleWriteReview()
- navigateBack()
```

### 5. MyPageController.java
**역할**: 마이페이지 제어
```java
- loadUserData()
- handleCancelEnrollment(int lectureId)
- handleViewPaymentHistory()
```

### 6. InstructorController.java
**역할**: 강사 페이지 제어
```java
- loadInstructorDetail(int instructorId)
- navigateToLecture(int lectureId)
```

---

## 6. View - 7개 (대부분 이미 존재)

1. **SignInView.java** ✅ 존재
2. **SignUpView.java** ✅ 존재
3. **HomePageView.java** ✅ 존재
4. **LecturePageView.java** ✅ 존재
5. **LectureDetailView.java** ✅ 존재
6. **InstructorsPageView.java** ✅ 존재
7. **MyPageView.java** ✅ 존재
8. **SidePanel.java** ✅ 존재 (네비게이션)

---

## 📊 파일 개수 요약

| 계층 | 파일 수 | 비고 |
|------|---------|------|
| **Model** | 6개 | User, Lecture, Instructor, Textbook, Review, Payment |
| **DTO** | 15개 | Request(7) + Response(5) + View(3) |
| **Repository** | 12개 | 인터페이스(6) + 구현체(6) |
| **Service** | 4개 | Auth, Lecture(통합), Review, User |
| **Controller** | 6개 | 화면별 컨트롤러 |
| **View** | 8개 | 이미 존재 |
| **합계** | **51개** | Toy Project에 적합한 규모 |

---

## 🎯 간소화 포인트

### ❌ 삭제된 파일 (기존 대비 -9개)

**Service (2개)**
- EnrollmentService → LectureService에 통합
- PaymentService → LectureService에 통합

**DTO (7개)**
- CancelEnrollmentRequest → 매개변수로 충분
- CancelEnrollmentResponse → boolean 반환
- PurchaseTextbookRequest → EnrollLectureRequest에 포함
- PaymentHistoryResponse → UserProfileResponse에 포함
- InstructorDetailResponse → 당장 불필요
- InstructorSummaryView → 당장 불필요
- TextbookView → 당장 불필요

### ✅ 통합의 장점

1. **연관 기능 응집**: 강의 신청-결제가 하나의 Service
2. **코드 중복 감소**: Lecture 조회 로직 재사용
3. **관리 편의성**: 강의 관련 로직이 한 곳에
4. **Toy Project 규모에 적합**: 50개 파일로 관리 가능

---

## 🚀 우선순위별 개발 순서

### Phase 1: 핵심 기능 (필수)
1. **Model** (6개) - 도메인 객체 정의
2. **인증 DTO** (4개) + AuthService
3. **강의 DTO** (6개) + LectureService
4. **Repository 인터페이스** (6개)
5. **Repository 구현체** (UserRepositoryImpl, LectureRepositoryImpl)
6. **Controller** (SignInController, LectureController)

### Phase 2: 신청 기능
1. EnrollLectureRequest/Response
2. LectureService의 신청/결제 메서드 구현
3. LectureDetailController
4. PaymentRepository

### Phase 3: 부가 기능
1. Review 관련 (DTO + Service + Repository)
2. MyPage 관련 (DTO + Controller)
3. Instructor 페이지

---

## 📝 파일명 네이밍 규칙

- **Model**: `{도메인}.java` (예: `User.java`)
- **DTO Request**: `{동작}{대상}Request.java` (예: `EnrollLectureRequest.java`)
- **DTO Response**: `{대상}{동작}Response.java` (예: `LectureListResponse.java`)
- **DTO View**: `{대상}View.java` (예: `LectureCardView.java`)
- **Repository**: `{도메인}Repository.java` + `{도메인}RepositoryImpl.java`
- **Service**: `{도메인}Service.java` (예: `LectureService.java`)
- **Controller**: `{화면}Controller.java` (예: `LectureController.java`)

---

## 💡 확장 시나리오

### 나중에 프로젝트가 커지면:

**결제 로직이 복잡해질 때**
```
LectureService → LectureService + PaymentService 분리
- 환불 정책 복잡
- 할인 쿠폰 시스템
- 정기 결제
```

**수강 관리 기능이 많아질 때**
```
LectureService → LectureService + EnrollmentService 분리
- 출석 체크
- 진도율 관리
- 학습 분석
```

**현재는 통합이 더 효율적입니다!**

---

## ✅ 최종 체크리스트

- [x] Model 6개 정의
- [x] 필수 DTO만 선별 (15개)
- [x] Service 통합 (4개)
- [x] Repository 인터페이스/구현체 분리
- [x] Controller 화면별 정리
- [x] 총 파일 수 51개로 관리 가능한 규모 유지

**Toy Project에 최적화된 구조 완성!** 🎉
