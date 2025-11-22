# 프로젝트 파일 구조 가이드

## 프로젝트 개요
- 주제: 고등학생 수능 강의 오프라인 신청 프로그램
- 주요 기능: 강의 검색/조회, 강의 신청, 결제, 리뷰
- 아키텍처: MVC 패턴 기반 계층형 구조

## 전체 디렉토리 구조

프로젝트는 다음과 같은 계층으로 구성됩니다:
- model: 도메인 객체(엔티티)
- dto: 데이터 전송 객체
- repository: 데이터 접근 계층
- service: 비즈니스 로직 계층
- controller: 이벤트 처리 계층
- view: 사용자 인터페이스

## 각 디렉토리의 역할

### 1. model 디렉토리
도메인 객체(엔티티)를 정의하는 곳입니다.

#### 역할
- 데이터베이스 테이블과 매핑되는 객체 정의
- 비즈니스 도메인의 핵심 개념 표현
- 엔티티 간의 관계 정의

#### 포함 파일
- User: 사용자 정보
- Lecture: 강의 정보
- Instructor: 강사 정보
- Textbook: 교재 정보
- Review: 리뷰 정보
- Payment: 결제 내역 정보

#### 상호작용
- Repository에서 데이터를 읽고 쓸 때 사용
- Service 내부에서 비즈니스 로직 처리 시 사용
- DTO로 변환되어 상위 계층으로 전달

### 2. dto 디렉토리
계층 간 데이터 전송 객체를 정의하는 곳입니다.

#### 역할
- View와 Service 간 데이터 전달
- 필요한 데이터만 선택적으로 포함
- Entity와 View 간의 결합도 감소

#### 포함 파일 분류
- Request DTO: View에서 Service로 전달되는 요청 데이터
- Response DTO: Service에서 View로 반환되는 응답 데이터
- View DTO: UI 표시 전용 데이터

#### 상호작용
- Controller가 View로부터 입력을 받아 Request DTO 생성
- Service가 비즈니스 로직을 처리하고 Response DTO 반환
- Controller가 Response DTO를 받아 View 업데이트

### 3. repository 디렉토리
데이터 접근 계층을 정의하는 곳입니다.

#### 역할
- 데이터 소스(파일, DB 등)와의 직접적인 상호작용
- CRUD 작업 수행
- 데이터 조회 및 저장 로직 캡슐화

#### 구조
- 인터페이스: 데이터 접근 메서드 정의
- 구현체(Impl): 실제 데이터 접근 로직 구현

#### 포함 파일
인터페이스:
- UserRepository
- LectureRepository
- InstructorRepository
- TextbookRepository
- ReviewRepository
- PaymentRepository

구현체:
- UserRepositoryImpl
- LectureRepositoryImpl
- InstructorRepositoryImpl
- TextbookRepositoryImpl
- ReviewRepositoryImpl
- PaymentRepositoryImpl

#### 상호작용
- Service가 Repository 인터페이스를 통해 데이터 접근
- Entity 객체를 반환하거나 매개변수로 받음
- 데이터 소스의 변경이 Service에 영향을 주지 않도록 추상화

### 4. service 디렉토리
비즈니스 로직을 처리하는 계층입니다.

#### 역할
- 핵심 비즈니스 로직 구현
- 데이터 검증 및 처리
- 여러 Repository를 조합하여 복잡한 작업 수행
- Entity를 DTO로 변환

#### 포함 파일
- AuthService: 인증(로그인, 회원가입)
- LectureService: 강의 조회, 검색, 신청, 결제
- ReviewService: 리뷰 관리
- UserService: 사용자 정보 관리

#### 상호작용
- Controller로부터 Request DTO를 받음
- Repository를 통해 데이터 조회/저장
- Entity를 Response DTO로 변환하여 반환
- 비즈니스 규칙 검증 및 예외 처리

### 5. controller 디렉토리
이벤트 처리 및 View-Service 연결 계층입니다.

#### 역할
- View의 이벤트 리스너 등록
- 사용자 입력을 DTO로 변환
- Service 메서드 호출
- Service 결과를 받아 View 업데이트
- 화면 전환 처리

#### 포함 파일
- SignInController: 로그인 화면 제어
- SignUpController: 회원가입 화면 제어
- LectureController: 강의 목록 화면 제어
- LectureDetailController: 강의 상세 화면 제어
- MyPageController: 마이페이지 제어
- InstructorController: 강사 페이지 제어

#### 상호작용
- View로부터 사용자 입력 수신
- 입력 데이터를 Request DTO로 변환
- Service 호출하여 비즈니스 로직 실행
- Response DTO를 받아 View에 전달
- UI 관련 예외 처리(JOptionPane 등)

### 6. view 디렉토리
사용자 인터페이스를 정의하는 곳입니다.

#### 역할
- 화면 UI 구성 및 렌더링
- 사용자 입력 받기
- 데이터 화면에 표시
- 이벤트 리스너 등록 메서드 제공

#### 포함 파일
- SignInView: 로그인 화면
- SignUpView: 회원가입 화면
- HomePageView: 홈 화면
- LecturePageView: 강의 목록 화면
- LectureDetailView: 강의 상세 화면
- InstructorsPageView: 강사 목록 화면
- MyPageView: 마이페이지
- SidePanel: 네비게이션 패널

#### 상호작용
- Controller에 이벤트 리스너 등록 메서드 제공
- Controller에 입력 데이터를 반환하는 getter 메서드 제공
- Controller로부터 데이터를 받아 화면에 표시
- 비즈니스 로직을 포함하지 않음

## 계층 간 데이터 흐름

### 데이터 조회 흐름
1. View: 사용자가 데이터 조회 요청
2. Controller: View로부터 입력을 받아 Request DTO 생성
3. Service: Request DTO를 받아 Repository 호출
4. Repository: 데이터 소스에서 Entity 조회
5. Service: Entity를 Response DTO로 변환
6. Controller: Response DTO를 받아 View에 전달
7. View: 데이터를 화면에 표시

### 데이터 생성/수정 흐름
1. View: 사용자가 데이터 입력
2. Controller: 입력 데이터를 Request DTO로 변환
3. Service: Request DTO 검증 및 Entity 생성/수정
4. Repository: Entity를 데이터 소스에 저장
5. Service: 결과를 Response DTO로 반환
6. Controller: Response DTO를 받아 View 업데이트
7. View: 결과 메시지 표시 또는 화면 전환

## 계층별 책임

### View
- UI 구성 및 렌더링만 담당
- 비즈니스 로직 포함 금지
- Controller에 필요한 메서드만 제공

### Controller
- View와 Service 연결만 담당
- 간단한 UI 로직(JOptionPane) 포함 가능
- 비즈니스 로직 포함 금지

### Service
- 비즈니스 로직만 담당
- 데이터 검증 및 처리
- Entity와 DTO 변환
- UI 관련 코드 포함 금지

### Repository
- 데이터 접근만 담당
- CRUD 작업 수행
- 비즈니스 로직 포함 금지

### Model
- 데이터 구조 정의만 담당
- 간단한 도메인 로직 포함 가능
- UI나 데이터 접근 로직 포함 금지

### DTO
- 데이터 전송만 담당
- 로직 포함 금지
- 불변 객체로 설계

## 디렉토리 간 의존성 규칙

### 의존 방향
- View → Controller → Service → Repository → Model
- 역방향 의존 금지(하위 계층이 상위 계층 참조 금지)

### DTO 사용 위치
- View ↔ Controller: DTO 사용
- Controller ↔ Service: DTO 사용
- Service ↔ Repository: Entity 사용

### 주의사항
- View는 Service를 직접 호출하면 안 됩니다
- Service는 View를 알아서는 안 됩니다
- Repository는 DTO를 알아서는 안 됩니다
- 각 계층은 바로 아래 계층만 의존해야 합니다
