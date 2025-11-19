# DTO 설계 가이드

## 📚 목차
1. [DTO란?](#dto란)
2. [DTO 종류](#dto-종류)
3. [네이밍 규칙](#네이밍-규칙)
4. [기본 구조 패턴](#기본-구조-패턴)
5. [실전 적용 예시](#실전-적용-예시)

---

## DTO란?

**DTO (Data Transfer Object)** = 계층 간 데이터를 전달하는 객체

### 핵심 개념

```
View ←→ Controller ←→ Service ←→ Repository
         (DTO 사용)    (Entity 사용)
```

### 왜 DTO를 사용하는가?

1. **계층 간 결합도 감소**
   - Entity 구조가 변경되어도 DTO만 수정하면 됨
   - View는 Entity의 내부 구조를 알 필요 없음

2. **불필요한 데이터 노출 방지**
   - View는 필요한 정보만 받음
   - 보안: 민감한 정보(비밀번호 등) 숨김

3. **명확한 인터페이스**
   - 메서드 시그니처만 봐도 어떤 데이터가 필요한지 알 수 있음

---

## DTO 종류

### 1. Request DTO
**View → Service**로 전달되는 요청 데이터

**특징:**
- 사용자 입력값을 담음
- 검증(Validation)에 필요한 정보 포함
- 불변 객체로 만드는 것이 좋음

**사용 시점:**
- 생성(Create)
- 검색(Search)
- 수정(Update)

---

### 2. Response DTO
**Service → View**로 반환되는 응답 데이터

**특징:**
- 작업 결과를 담음
- 성공/실패 여부, 메시지, 데이터 포함
- View에 표시할 정보만 선택적으로 포함

**사용 시점:**
- 조회(Read)
- 생성/수정/삭제 결과

---

### 3. View DTO
**View 내부**에서만 사용하는 표시 전용 데이터

**특징:**
- UI 렌더링에 필요한 최소 정보만 포함
- 카드, 리스트 아이템 등에 사용
- Response DTO보다 더 간결함

**사용 시점:**
- 목록 카드 표시
- 요약 정보 표시

---

## 네이밍 규칙

### 기본 패턴

| 종류 | 네이밍 패턴 | 예시 |
|------|-----------|------|
| **Request** | `{동작}{대상}Request` | `CreateUserRequest`<br>`SearchLectureRequest`<br>`UpdateProfileRequest` |
| **Response** | `{대상}{동작}Response` | `UserDetailResponse`<br>`LectureListResponse`<br>`LoginResponse` |
| **View** | `{대상}{용도}View` | `LectureCardView`<br>`UserSummaryView` |

### 네이밍 가이드

```java
// ✅ 좋은 예시
CreateLectureRequest      // 명확한 동작
LectureDetailResponse     // 무엇을 반환하는지 명확
LectureCardView          // 용도가 명확

// ❌ 나쁜 예시
LectureDTO               // 너무 포괄적
LectureData              // 용도 불분명
LectureInfo              // Request/Response 구분 안 됨
```

---

## 기본 구조 패턴

### 1. Request DTO 구조

```java
package com.project.app.dto;

/**
 * {기능} 요청 DTO
 *
 * 사용 시점: {언제 사용하는지}
 */
public class {동작}{대상}Request {

    // 필수 입력 필드
    private Type field1;
    private Type field2;

    // 선택적 입력 필드
    private Type optionalField;

    // 생성자: 필수 필드만
    public {동작}{대상}Request(Type field1, Type field2) {
        this.field1 = field1;
        this.field2 = field2;
    }

    // Getters
    public Type getField1() { return field1; }
    public Type getField2() { return field2; }

    // Setters (선택적 필드만)
    public void setOptionalField(Type optionalField) {
        this.optionalField = optionalField;
    }
}
```

**핵심 포인트:**
- 필수 필드는 생성자로 강제
- 불변성 유지 (Setter 최소화)
- 검증 로직은 Service에서 처리

---

### 2. Response DTO 구조

#### 패턴 A: 단순 데이터 반환

```java
package com.project.app.dto;

/**
 * {대상} {동작} 응답 DTO
 */
public class {대상}{동작}Response {

    // 반환할 데이터 필드들
    private Type field1;
    private Type field2;
    private Type field3;

    // 생성자
    public {대상}{동작}Response(Type field1, Type field2, Type field3) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
    }

    // Getters만
    public Type getField1() { return field1; }
    public Type getField2() { return field2; }
    public Type getField3() { return field3; }
}
```

---

#### 패턴 B: 성공/실패 포함

```java
package com.project.app.dto;

/**
 * {동작} 결과 응답 DTO
 */
public class {동작}Response {

    private boolean success;      // 성공 여부
    private String message;       // 메시지
    private ErrorType errorType;  // 에러 타입 (실패 시)
    private Type resultData;      // 결과 데이터 (성공 시)

    // 에러 타입 Enum
    public enum ErrorType {
        NONE,
        VALIDATION_ERROR,
        NOT_FOUND,
        DUPLICATE,
        PERMISSION_DENIED,
        SYSTEM_ERROR
    }

    // 정적 팩토리 메서드: 성공
    public static {동작}Response success(Type data) {
        return new {동작}Response(true, "성공 메시지", ErrorType.NONE, data);
    }

    // 정적 팩토리 메서드: 실패
    public static {동작}Response failure(String message, ErrorType errorType) {
        return new {동작}Response(false, message, errorType, null);
    }

    // private 생성자
    private {동작}Response(boolean success, String message,
                          ErrorType errorType, Type resultData) {
        this.success = success;
        this.message = message;
        this.errorType = errorType;
        this.resultData = resultData;
    }

    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public ErrorType getErrorType() { return errorType; }
    public Type getResultData() { return resultData; }
}
```

**핵심 포인트:**
- 정적 팩토리 메서드로 생성 (`success()`, `failure()`)
- Enum으로 에러 타입 명확하게 정의
- 불변 객체 (생성자 private, Getter만 제공)

---

### 3. View DTO 구조

```java
package com.project.app.dto;

/**
 * {대상} {용도} 표시용 DTO
 *
 * 사용처: {어디서 사용하는지}
 */
public class {대상}{용도}View {

    // UI 표시에 필요한 최소 필드만
    private Type essentialField1;
    private Type essentialField2;

    public {대상}{용도}View(Type field1, Type field2) {
        this.essentialField1 = field1;
        this.essentialField2 = field2;
    }

    // Getters만
    public Type getEssentialField1() { return essentialField1; }
    public Type getEssentialField2() { return essentialField2; }
}
```

**핵심 포인트:**
- 정말 필요한 필드만 포함 (3~5개 정도)
- 불변 객체
- 가볍고 간결하게

---

## 실전 적용 예시

### 시나리오: 강의 검색 및 조회

#### 1단계: 필요한 DTO 파악

```
사용자 행동 → 필요한 DTO

1. 검색어 입력 → SearchLectureRequest
2. 강의 목록 표시 → LectureListResponse, LectureCardView
3. 강의 클릭 → LectureDetailResponse
4. 강의 신청 → EnrollLectureRequest, EnrollLectureResponse
```

---

#### 2단계: DTO 정의

```java
// 1. 검색 요청
public class SearchLectureRequest {
    private String keyword;
    private String subject;      // 선택적
    private String sortOrder;
    // ...
}

// 2. 목록 응답
public class LectureListResponse {
    private List<LectureCardView> lectures;
    private int totalCount;
    // ...
}

// 3. 카드 표시용
public class LectureCardView {
    private int id;
    private String name;
    private String thumbnail;
    private double rating;
    // ...
}

// 4. 상세 응답
public class LectureDetailResponse {
    private int id;
    private String name;
    private String description;
    private boolean isEnrolled;
    // ... (더 많은 정보)
}

// 5. 신청 요청
public class EnrollLectureRequest {
    private int lectureId;
    private String userId;
    private boolean purchaseTextbook;
    // ...
}

// 6. 신청 응답
public class EnrollLectureResponse {
    private boolean success;
    private String message;
    private ErrorType errorType;
    // ...
}
```

---

#### 3단계: 흐름도

```
┌─────────────────────────────────────────────────┐
│ 1. 사용자가 검색어 입력                          │
└─────────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────────┐
│ Controller: View에서 입력값 가져오기             │
│ → SearchLectureRequest 생성                     │
└─────────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────────┐
│ Service: Request 받아서 처리                     │
│ → Repository에서 Entity 조회                     │
│ → Entity를 LectureCardView로 변환                │
│ → LectureListResponse로 감싸서 반환              │
└─────────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────────┐
│ Controller: Response 받아서                      │
│ → View에 displayLectures() 호출                 │
└─────────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────────┐
│ View: List<LectureCardView> 받아서 화면에 표시   │
└─────────────────────────────────────────────────┘
```

---

## DTO 설계 체크리스트

### ✅ Request DTO

- [ ] 필수 필드가 생성자에 있는가?
- [ ] 불필요한 Setter가 없는가?
- [ ] 필드 이름이 명확한가?
- [ ] Javadoc 주석이 있는가?

### ✅ Response DTO

- [ ] 성공/실패를 구분할 수 있는가?
- [ ] 에러 타입이 Enum으로 정의되었는가?
- [ ] 정적 팩토리 메서드를 사용했는가?
- [ ] 불필요한 정보가 노출되지 않는가?

### ✅ View DTO

- [ ] UI에 필요한 최소 정보만 있는가?
- [ ] Response DTO와 중복되지 않는가?
- [ ] 불변 객체인가?

---

## 핵심 정리

### DTO 3원칙

1. **단순함**: 로직 없이 데이터만
2. **불변성**: 생성 후 변경 불가
3. **명확함**: 이름만 봐도 용도 파악

### DTO vs Entity

| 항목 | Entity | DTO |
|------|--------|-----|
| **사용 위치** | Repository, Service 내부 | Controller, View |
| **목적** | 도메인 모델 표현 | 데이터 전송 |
| **변경 빈도** | 낮음 (도메인 안정) | 높음 (요구사항 변경) |
| **필드** | 모든 정보 | 필요한 정보만 |
| **로직** | 비즈니스 로직 가능 | 로직 없음 |

### 언제 DTO를 만드는가?

```
✅ DTO 필요한 경우:
- 여러 필드를 함께 전달할 때
- 검증이 필요한 입력 데이터
- 성공/실패를 구분해야 할 때
- View에 보여줄 데이터가 Entity와 다를 때

❌ DTO 불필요한 경우:
- 단일 값(String, int) 전달
- 내부 메서드 간 데이터 전달
- 간단한 boolean 반환
```

---

## 마무리

DTO는 **계층 간 계약(Contract)** 입니다.

- Request DTO: "이런 정보를 줄게요"
- Response DTO: "이런 정보를 돌려줄게요"

명확한 DTO 설계는 **유지보수**와 **협업**을 쉽게 만듭니다!
