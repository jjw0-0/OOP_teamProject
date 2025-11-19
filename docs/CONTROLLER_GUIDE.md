# Controller 작성 가이드

## 📚 목차
1. [Controller란?](#controller란)
2. [기본 구조](#기본-구조)
3. [작성 예시](#작성-예시)
4. [View에 필요한 메서드](#view에-필요한-메서드)

---

## Controller란?

**Controller**는 **View와 Service를 연결**하는 중간 계층입니다.

### 역할
- View의 버튼/이벤트 리스너 등록
- 사용자 입력을 DTO로 변환
- Service 호출
- Service 결과를 받아 View 업데이트
- 화면 전환 처리

---

## 기본 구조

```java
package com.project.app.controller;

import com.project.app.view.*;
import com.project.app.service.*;
import com.project.app.dto.*;

public class XxxController {

    private final XxxView view;
    private final XxxService service;

    // 생성자: View와 Service를 받아서 연결
    public XxxController(XxxView view, XxxService service) {
        this.view = view;
        this.service = service;

        // 이벤트 리스너 등록
        initListeners();
    }

    // 모든 이벤트 리스너를 한 곳에서 등록
    private void initListeners() {
        view.addXxxButtonListener(e -> handleXxx());
    }

    // 이벤트 처리 메서드
    private void handleXxx() {
        // 1. View에서 입력값 가져오기
        // 2. DTO로 변환
        // 3. Service 호출
        // 4. 결과 처리
    }
}
```

---

## 작성 예시

### 1. SignInController (로그인)

```java
package com.project.app.controller;

import com.project.app.view.SignInView;
import com.project.app.service.AuthService;
import com.project.app.dto.*;
import javax.swing.JOptionPane;

public class SignInController {

    private final SignInView view;
    private final AuthService authService;

    public SignInController(SignInView view, AuthService authService) {
        this.view = view;
        this.authService = authService;
        initListeners();
    }

    private void initListeners() {
        // 로그인 버튼 클릭 이벤트
        view.addLoginButtonListener(e -> handleLogin());

        // 회원가입 버튼 클릭 이벤트
        view.addSignUpButtonListener(e -> navigateToSignUp());
    }

    private void handleLogin() {
        // 1. View에서 입력값 가져오기
        String userId = view.getUserId();
        String password = view.getPassword();

        // 2. DTO 생성
        LoginRequest request = new LoginRequest(userId, password);

        // 3. Service 호출
        LoginResponse response = authService.login(request);

        // 4. 결과 처리
        if (response.isSuccess()) {
            JOptionPane.showMessageDialog(view, "로그인 성공!");
            view.clearFields();
            navigateToHome();
        } else {
            JOptionPane.showMessageDialog(view, response.getMessage(),
                "로그인 실패", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void navigateToSignUp() {
        SidePanel.getInstance().showContent(SignUpView.getInstance());
    }

    private void navigateToHome() {
        SidePanel.getInstance().showContent(HomePageView.getInstance());
    }
}
```

---

### 2. LectureController (강의 목록)

```java
package com.project.app.controller;

import com.project.app.view.LecturePageView;
import com.project.app.service.LectureService;
import com.project.app.dto.*;
import java.util.List;

public class LectureController {

    private final LecturePageView view;
    private final LectureService lectureService;

    private String currentSubject = null;  // 현재 선택된 과목
    private String currentSortOrder = "최신순";

    public LectureController(LecturePageView view, LectureService lectureService) {
        this.view = view;
        this.lectureService = lectureService;
        initListeners();

        // 초기 로드
        loadLectures();
    }

    private void initListeners() {
        // 과목 버튼 이벤트
        view.addSubjectButtonListener("수학", e -> handleSubjectFilter("수학"));
        view.addSubjectButtonListener("영어", e -> handleSubjectFilter("영어"));

        // 검색 버튼 이벤트
        view.addSearchButtonListener(e -> handleSearch());

        // 정렬 변경 이벤트
        view.addSortComboListener(e -> handleSortChange());

        // 강의 카드 클릭 이벤트
        view.addLectureCardClickListener(lectureId -> navigateToDetail(lectureId));
    }

    private void handleSubjectFilter(String subject) {
        currentSubject = subject;
        loadLectures();
    }

    private void handleSearch() {
        String keyword = view.getSearchKeyword();
        if (keyword.isEmpty()) {
            loadLectures();
            return;
        }

        LectureSearchRequest request = new LectureSearchRequest(
            keyword, currentSortOrder
        );
        LectureListResponse response = lectureService.searchLectures(request);
        view.displayLectures(response.getLectures());
    }

    private void handleSortChange() {
        currentSortOrder = view.getSelectedSortOrder();
        loadLectures();
    }

    private void loadLectures() {
        LectureListResponse response;

        if (currentSubject == null) {
            response = lectureService.getAllLectures(currentSortOrder);
        } else {
            response = lectureService.getLecturesBySubject(
                currentSubject, currentSortOrder
            );
        }

        view.displayLectures(response.getLectures());
    }

    private void navigateToDetail(int lectureId) {
        LectureDetailResponse detail = lectureService.getLectureDetail(lectureId);
        LectureDetailView detailView = new LectureDetailView(detail);
        detailView.show();
    }
}
```

---

## View에 필요한 메서드

Controller가 View를 제어하려면 View에 다음 메서드들이 필요합니다.

### SignInView 예시

```java
// 리스너 등록 메서드
public void addLoginButtonListener(ActionListener listener) {
    loginBtn.addActionListener(listener);
}

public void addSignUpButtonListener(ActionListener listener) {
    signupBtn.addActionListener(listener);
}

// 데이터 가져오기
public String getUserId() {
    return idField.getText().trim();
}

public String getPassword() {
    return new String(pwField.getPassword()).trim();
}

// 화면 조작
public void clearFields() {
    idField.setText("");
    pwField.setText("");
}
```

### LecturePageView 예시

```java
// 리스너 등록
public void addSubjectButtonListener(String subject, ActionListener listener) {
    subjectButtons.get(subject).addActionListener(listener);
}

public void addSearchButtonListener(ActionListener listener) {
    searchBtn.addActionListener(listener);
}

public void addSortComboListener(ActionListener listener) {
    sortCombo.addActionListener(listener);
}

public void addLectureCardClickListener(LectureCardClickListener listener) {
    this.cardClickListener = listener;
}

// 데이터 가져오기
public String getSearchKeyword() {
    return searchField.getText().trim();
}

public String getSelectedSortOrder() {
    return (String) sortCombo.getSelectedItem();
}

// 화면 업데이트
public void displayLectures(List<LectureCardView> lectures) {
    lectureCardPanel.removeAll();
    for (LectureCardView lecture : lectures) {
        lectureCardPanel.add(createLectureCard(lecture));
    }
    lectureCardPanel.revalidate();
    lectureCardPanel.repaint();
}

// 함수형 인터페이스
@FunctionalInterface
public interface LectureCardClickListener {
    void onCardClick(int lectureId);
}
```

---

## 핵심 정리

### Controller의 3가지 핵심 작업

1. **이벤트 리스너 등록** (`initListeners()`)
   - View의 버튼/필드에 이벤트 연결

2. **이벤트 처리** (`handleXxx()`)
   - 입력값 가져오기 → DTO 변환 → Service 호출 → 결과 처리

3. **화면 전환** (`navigateToXxx()`)
   - 다른 화면으로 이동

### Controller 작성 순서

```
1. View와 Service를 필드로 선언
2. 생성자에서 초기화 및 initListeners() 호출
3. initListeners()에서 모든 이벤트 리스너 등록
4. handleXxx() 메서드로 각 이벤트 처리
5. 필요시 navigateToXxx()로 화면 전환
```

---

## 참고 사항

- Controller는 **UI 로직(JOptionPane 등)은 가져도 되지만**, **비즈니스 로직은 포함하면 안 됩니다**
- 비즈니스 로직은 **Service**에, 데이터 접근은 **Repository**에
- Controller는 **중재자** 역할만 수행
