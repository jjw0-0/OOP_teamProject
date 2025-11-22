# MVC 패턴 실전 가이드

이 문서는 MVC 패턴을 처음 접하는 팀원을 위한 실전 중심 가이드입니다.

## 목차

1. [MVC 패턴 이해하기](#1-mvc-패턴-이해하기)
2. [로그인 기능 구현 예제](#2-로그인-기능-구현-예제)
3. [자주 하는 실수 및 해결법](#3-자주-하는-실수-및-해결법)

---

## 1. MVC 패턴 이해하기

### 기본 개념

MVC는 애플리케이션을 Model, View, Controller 세 가지 역할로 분리하는 패턴입니다.

**왜 분리하는가?**

```
분리하지 않으면:
- 모든 코드가 한 곳에 몰려서 수정이 어려움
- 버그 발생 시 원인 파악이 어려움
- 여러 명이 동시에 작업하기 어려움

분리하면:
- 각자 맡은 부분만 작업 가능
- 문제 발생 시 해당 계층만 확인
- 코드 재사용이 쉬움
```

### 전체 구조

```
┌──────────────────────────────────────────────┐
│  View (화면)                                  │
│  - 사용자가 보는 UI                            │
│  - 입력 받기, 결과 표시                         │
└──────────────────────────────────────────────┘
                    ↕ (DTO)
┌──────────────────────────────────────────────┐
│  Controller (중재자)                          │
│  - View와 Service 연결                        │
│  - 이벤트 처리                                 │
└──────────────────────────────────────────────┘
                    ↕ (DTO)
┌──────────────────────────────────────────────┐
│  Service (비즈니스 로직)                       │
│  - 데이터 검증                                 │
│  - 핵심 로직 처리                              │
└──────────────────────────────────────────────┘
                    ↕ (Entity)
┌──────────────────────────────────────────────┐
│  Repository (데이터 접근)                      │
│  - DB 읽기/쓰기                               │
└──────────────────────────────────────────────┘
                    ↕
┌──────────────────────────────────────────────┐
│  Database (데이터 저장소)                      │
└──────────────────────────────────────────────┘
```

### 데이터 흐름 (로그인 예시)

```
1. 사용자가 ID/PW 입력 후 로그인 버튼 클릭
   ↓
2. View: 버튼 클릭 이벤트 발생
   ↓
3. Controller: View에서 입력값 가져와서 DTO 생성
   LoginRequest request = new LoginRequest(id, pw);
   ↓
4. Controller: Service 호출
   LoginResponse response = authService.login(request);
   ↓
5. Service: 비즈니스 로직 수행
   - Repository에서 사용자 조회
   - 비밀번호 확인
   - Response DTO 생성하여 반환
   ↓
6. Controller: 결과에 따라 View 업데이트 또는 메시지 표시
   if (response.isSuccess()) { 화면 전환 }
   else { 에러 메시지 표시 }
```

### 계층별 책임

| 계층 | 역할 | 할 수 있는 것 | 하면 안 되는 것 |
|------|------|---------------|----------------|
| View | 화면 표시 | UI 구성, 입력 받기 | 비즈니스 로직, DB 접근 |
| Controller | 이벤트 처리 | View-Service 연결, 화면 전환 | 비즈니스 로직, DB 접근 |
| Service | 비즈니스 로직 | 데이터 검증, Entity-DTO 변환 | UI 코드, 직접 DB 접근 |
| Repository | 데이터 접근 | CRUD 작업 | 비즈니스 로직, UI 코드 |

### DTO의 역할

DTO는 계층 간 데이터를 전달하는 객체입니다.

```java
// DTO 사용 안 하면:
login(String id, String password, String deviceId, String ip, ...)
// 매개변수가 많아지면 관리 어려움

// DTO 사용하면:
login(LoginRequest request)
// request 안에 필요한 데이터가 모두 포함됨
```

---

## 2. 로그인 기능 구현 예제

### 구현 순서

```
Model → DTO → Repository → Service → Controller → View
```

---

### Step 1: Model 작성

파일: `src/model/User.java`

```java
package model;

public class User {
    private String id;
    private String password;
    private String name;

    public User(String id, String password, String name) {
        this.id = id;
        this.password = password;
        this.name = name;
    }

    public String getId() { return id; }
    public String getPassword() { return password; }
    public String getName() { return name; }
}
```

---

### Step 2: DTO 작성

파일: `src/dto/LoginRequest.java`

```java
package dto;

public class LoginRequest {
    private final String userId;
    private final String password;

    public LoginRequest(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() { return userId; }
    public String getPassword() { return password; }
}
```

파일: `src/dto/LoginResponse.java`

```java
package dto;

public class LoginResponse {
    private final boolean success;
    private final String message;
    private final String userName;

    private LoginResponse(boolean success, String message, String userName) {
        this.success = success;
        this.message = message;
        this.userName = userName;
    }

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

---

### Step 3: Repository 작성

파일: `src/repository/UserRepository.java`

```java
package repository;

import model.User;

public interface UserRepository {
    User findById(String userId);
    void save(User user);
}
```

파일: `src/repository/UserRepositoryImpl.java`

```java
package repository;

import model.User;
import java.util.HashMap;
import java.util.Map;

public class UserRepositoryImpl implements UserRepository {
    private final Map<String, User> database = new HashMap<>();

    @Override
    public User findById(String userId) {
        return database.get(userId);
    }

    @Override
    public void save(User user) {
        database.put(user.getId(), user);
    }
}
```

---

### Step 4: Service 작성

파일: `src/service/AuthService.java`

```java
package service;

import dto.LoginRequest;
import dto.LoginResponse;
import model.User;
import repository.UserRepository;

public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginResponse login(LoginRequest request) {
        // 1. 검증: 사용자 존재 여부
        User user = userRepository.findById(request.getUserId());
        if (user == null) {
            return LoginResponse.failure("존재하지 않는 사용자입니다.");
        }

        // 2. 검증: 비밀번호 일치 여부
        if (!user.getPassword().equals(request.getPassword())) {
            return LoginResponse.failure("비밀번호가 일치하지 않습니다.");
        }

        // 3. 성공
        return LoginResponse.success(user.getName());
    }
}
```

---

### Step 5: Controller 작성

파일: `src/controller/SignInController.java`

```java
package controller;

import dto.LoginRequest;
import dto.LoginResponse;
import service.AuthService;
import view.SignInView;
import javax.swing.JOptionPane;

public class SignInController {
    private final SignInView view;
    private final AuthService service;

    public SignInController(SignInView view, AuthService service) {
        this.view = view;
        this.service = service;
        initListeners();
    }

    private void initListeners() {
        view.addLoginListener(e -> handleLogin());
    }

    private void handleLogin() {
        // View에서 입력값 가져오기
        String userId = view.getUserId();
        String password = view.getPassword();

        // DTO로 변환
        LoginRequest request = new LoginRequest(userId, password);

        // Service 호출
        LoginResponse response = service.login(request);

        // 결과 처리
        if (response.isSuccess()) {
            JOptionPane.showMessageDialog(null,
                response.getUserName() + "님 환영합니다.");
        } else {
            JOptionPane.showMessageDialog(null,
                response.getMessage(), "로그인 실패", JOptionPane.ERROR_MESSAGE);
        }
    }
}
```

---

### Step 6: View 작성

파일: `src/view/SignInView.java`

```java
package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SignInView extends JPanel {
    private final JTextField userIdField;
    private final JPasswordField passwordField;
    private final JButton loginButton;

    public SignInView() {
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("아이디:"));
        userIdField = new JTextField();
        add(userIdField);

        add(new JLabel("비밀번호:"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel());
        loginButton = new JButton("로그인");
        add(loginButton);
    }

    public String getUserId() {
        return userIdField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }
}
```

---

### 실행 코드

파일: `src/Main.java`

```java
import controller.SignInController;
import repository.UserRepository;
import repository.UserRepositoryImpl;
import service.AuthService;
import view.SignInView;
import model.User;
import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepositoryImpl();
        userRepository.save(new User("test", "1234", "홍길동"));

        AuthService authService = new AuthService(userRepository);
        SignInView view = new SignInView();
        new SignInController(view, authService);

        JFrame frame = new JFrame("로그인");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(view);
        frame.setSize(400, 200);
        frame.setVisible(true);
    }
}
```

---

## 3. 자주 하는 실수 및 해결법

### 실수 1: Controller에서 Repository 직접 호출

```java
// 잘못된 코드
public class SignInController {
    private final UserRepository userRepository;

    private void handleLogin() {
        User user = userRepository.findById(userId);  // X
        if (user.getPassword().equals(password)) { /* ... */ }
    }
}
```

**문제점**: Controller에 비즈니스 로직이 들어감, 역할 혼재

```java
// 올바른 코드
public class SignInController {
    private final AuthService authService;

    private void handleLogin() {
        LoginRequest request = new LoginRequest(userId, password);
        LoginResponse response = authService.login(request);  // O
    }
}
```

**해결**: Controller는 Service만 호출

---

### 실수 2: View에 비즈니스 로직 작성

```java
// 잘못된 코드
public class SignInView extends JPanel {
    private void loginButtonClicked() {
        String userId = userIdField.getText();
        User user = database.get(userId);  // X
        if (user != null && user.getPassword().equals(password)) { /* ... */ }
    }
}
```

**문제점**: View가 DB 접근 및 검증 수행

```java
// 올바른 코드
public class SignInView extends JPanel {
    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);  // O
    }

    public String getUserId() {
        return userIdField.getText();
    }
}
```

**해결**: View는 이벤트를 Controller에 전달만

---

### 실수 3: Service에서 UI 코드 사용

```java
// 잘못된 코드
public class AuthService {
    public void login(LoginRequest request) {
        User user = userRepository.findById(request.getUserId());
        if (user == null) {
            JOptionPane.showMessageDialog(null, "사용자 없음");  // X
        }
    }
}
```

**문제점**: Service가 UI에 의존

```java
// 올바른 코드
public class AuthService {
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findById(request.getUserId());
        if (user == null) {
            return LoginResponse.failure("사용자 없음");  // O
        }
    }
}
```

**해결**: DTO로 결과 반환, UI는 Controller에서

---

### 실수 4: DTO에 Setter 남발

```java
// 좋지 않은 코드
public class LoginRequest {
    private String userId;

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
```

**문제점**: 값이 중간에 변경될 수 있음

```java
// 더 좋은 코드
public class LoginRequest {
    private final String userId;

    public LoginRequest(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() { return userId; }
}
```

**해결**: final 사용, 생성자로만 값 설정

---

### 실수 5: 여러 매개변수 대신 DTO 미사용

```java
// 좋지 않은 코드
public LoginResponse login(String userId, String password,
                          String deviceId, String ipAddress) {
    // 매개변수 많음
}
```

**문제점**: 매개변수 추가/변경 시 메서드 시그니처 변경 필요

```java
// 더 좋은 코드
public LoginResponse login(LoginRequest request) {
    // request 안에 필요한 정보 포함
}
```

**해결**: 매개변수 3개 이상이면 DTO 사용

---

### 실수 6: Entity를 View에 직접 전달

```java
// 좋지 않은 코드
public class LectureService {
    public Lecture getLecture(int id) {
        return lectureRepository.findById(id);  // Entity 직접 반환
    }
}
```

**문제점**: Entity 구조 변경 시 View도 영향받음

```java
// 더 좋은 코드
public class LectureService {
    public LectureDetailResponse getLecture(int id) {
        Lecture lecture = lectureRepository.findById(id);
        return new LectureDetailResponse(
            lecture.getName(),
            lecture.getPrice()
        );
    }
}
```

**해결**: Entity를 DTO로 변환 후 반환

---

### 실수 7: ID 필드에 Setter 제공

```java
// 잘못된 코드
public class User {
    private int id;

    public void setId(int id) {  // X
        this.id = id;
    }
}
```

**문제점**: ID는 변경되면 안 됨

```java
// 올바른 코드
public class User {
    private final int id;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    // Setter 없음
}
```

**해결**: 불변 필드는 final + Setter 제공 안 함

---

### 실수 8: Repository에 비즈니스 로직

```java
// 잘못된 코드
public class LectureRepositoryImpl {
    public boolean enrollLecture(int lectureId, String userId) {
        Lecture lecture = findById(lectureId);
        if (lecture.isFull()) {  // X (비즈니스 로직)
            return false;
        }
        lecture.increaseEnrollment();
        save(lecture);
        return true;
    }
}
```

**문제점**: Repository가 비즈니스 로직 처리

```java
// 올바른 코드
// Repository: CRUD만
public class LectureRepositoryImpl {
    public Lecture findById(int id) { /* ... */ }
    public void save(Lecture lecture) { /* ... */ }
}

// Service: 비즈니스 로직
public class LectureService {
    public EnrollResponse enrollLecture(EnrollRequest request) {
        Lecture lecture = lectureRepository.findById(request.getLectureId());

        if (lecture.isFull()) {  // O
            return EnrollResponse.failure("정원 초과");
        }

        lecture.increaseEnrollment();
        lectureRepository.save(lecture);
        return EnrollResponse.success();
    }
}
```

**해결**: Repository는 CRUD만, 로직은 Service에

---

## 권장 사항

### 네이밍 컨벤션

- Controller 메서드: `handleLogin()`, `handleEnroll()`
- Service 메서드: `login()`, `enrollLecture()`
- View 메서드: `addLoginListener()`, `getUserId()`
- DTO: `LoginRequest`, `EnrollResponse`

### 작성 순서 준수

Model → DTO → Repository → Service → Controller → View 순서로 작성하면 의존성 문제가 없습니다.

### null 체크 습관화

```java
User user = userRepository.findById(userId);
if (user == null) {
    return Response.failure("사용자 없음");
}
```

### 의미 있는 메서드명

```java
// 나쁜 예
setEnrollment(n);

// 좋은 예
increaseEnrollment();
decreaseEnrollment();
```

---

## 체크리스트

코드 작성 후 확인:

**Model**
- 모든 필드가 private인가?
- 불변 필드는 final인가?
- 의미 있는 메서드명을 사용했는가?

**DTO**
- 필드가 private final인가?
- Setter 없이 Getter만 제공하는가?
- Response DTO는 정적 팩토리 메서드가 있는가?

**Service**
- UI 코드가 없는가?
- Request DTO를 받고 Response DTO를 반환하는가?
- 검증 → 처리 → 반환 순서인가?

**Controller**
- 비즈니스 로직이 없는가?
- Repository를 직접 호출하지 않는가?
- initListeners 메서드가 있는가?

**View**
- 비즈니스 로직이 없는가?
- addXxxListener 메서드를 제공하는가?
- getXxx 메서드로 입력값을 제공하는가?

---

## 참고 문서

- [CONTROLLER_GUIDE.md](./CONTROLLER_GUIDE.md)
- [DTO_GUIDE.md](./DTO_GUIDE.md)
- [MODEL_GUIDE.md](./MODEL_GUIDE.md)
- [SERVICE_GUIDE.md](./SERVICE_GUIDE.md)
- [PROJECT_FILES_STRUCTURE.md](./PROJECT_FILES_STRUCTURE.md)
