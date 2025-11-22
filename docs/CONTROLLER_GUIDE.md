# Controller 작성 가이드

## Controller란?

Controller는 View와 Service를 연결하는 중간 계층입니다.

## Controller의 역할

1. View의 버튼 및 이벤트 리스너 등록
2. 사용자 입력을 DTO로 변환
3. Service 호출
4. Service 결과를 받아 View 업데이트
5. 화면 전환 처리

## Controller 작성 시 포함해야 할 요소

### 1. 필드 선언
- View 인스턴스: Controller가 제어할 화면
- Service 인스턴스: 비즈니스 로직을 처리할 서비스

### 2. 생성자
- View와 Service를 매개변수로 받아서 필드 초기화
- 생성자 내에서 initListeners 메서드 호출

### 3. initListeners 메서드
- 모든 이벤트 리스너를 한 곳에서 등록
- View의 버튼이나 컴포넌트에 이벤트 핸들러 연결
- private 메서드로 작성

### 4. 이벤트 처리 메서드 (handleXxx 형식)
각 이벤트 처리 메서드는 다음 순서로 작성:
- View에서 입력값 가져오기
- 입력값을 DTO로 변환
- Service 메서드 호출
- Service의 반환 결과 처리
- View 업데이트 또는 화면 전환

### 5. 화면 전환 메서드 (navigateToXxx 형식)
- 다른 화면으로 전환하는 로직
- SidePanel을 통한 화면 전환 처리

## Controller 작성 순서

1. 필요한 View와 Service를 필드로 선언
2. 생성자에서 View와 Service를 받아 초기화하고 initListeners 호출
3. initListeners 메서드에서 모든 이벤트 리스너 등록
4. 각 이벤트를 처리할 handleXxx 메서드 작성
5. 필요한 경우 navigateToXxx 메서드로 화면 전환 로직 작성

### Controller 코드 뼈대 (참고용)

View의 이벤트를 감지하고 Service를 호출하는 표준 구조입니다.

```java
public class LoginController {
    // 1. View와 Service를 필드로 가짐
    private final LoginView view;
    private final AuthService service;

    // 2. 생성자 주입
    public LoginController(LoginView view, AuthService service) {
        this.view = view;
        this.service = service;
        initListeners(); // 리스너 등록 시작
    }

    // 3. 리스너 등록 (모든 버튼 이벤트를 여기서 연결)
    private void initListeners() {
        // View에 있는 '로그인 버튼'에 기능을 붙임
        view.addLoginListener(e -> handleLogin());
        
        // View에 있는 '회원가입 버튼'에 기능을 붙임
        view.addRegisterListener(e -> navigateToRegister());
    }

    // 4. 실제 처리 로직 (Handle Methods)
    private void handleLogin() {
        try {
            // Step 1: View에서 데이터 가져오기
            LoginRequest request = view.getLoginInput(); // DTO로 받아오기

            // Step 2: Service 호출
            LoginResponse response = service.login(request);

            // Step 3: 결과에 따라 View 업데이트
            if (response.isSuccess()) {
                JOptionPane.showMessageDialog(null, "환영합니다 " + response.getUserName());
                // 화면 전환 로직...
            } else {
                JOptionPane.showMessageDialog(null, response.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace(); // 에러 로그
            JOptionPane.showMessageDialog(null, "오류 발생: " + e.getMessage());
        }
    }
}
```

## View에 필요한 메서드

Controller가 View를 제어하려면 View에 다음 메서드들이 필요합니다:

### 리스너 등록 메서드
- 버튼이나 컴포넌트에 ActionListener를 등록하는 메서드
- 메서드명: addXxxListener 형식
- 매개변수: ActionListener

### 데이터 가져오기 메서드
- View의 입력 필드에서 값을 가져오는 메서드
- 메서드명: getXxx 형식
- 반환 타입: 해당 데이터의 타입

### 화면 조작 메서드
- View의 필드를 초기화하거나 업데이트하는 메서드
- 데이터를 화면에 표시하는 메서드

## 주의사항

- Controller는 UI 로직(예: JOptionPane)을 가질 수 있지만, 비즈니스 로직은 포함하면 안 됩니다
- 비즈니스 로직은 Service에, 데이터 접근은 Repository에 위치해야 합니다
- Controller는 중재자 역할만 수행합니다

Controller가 View를 조종하려면 View는 아래 두 가지를 반드시 가지고 있어야 합니다.

### 1. 데이터 Getter (Controller가 입력값을 가져갈 수 있게)
```java
// 텍스트 필드의 값을 DTO로 포장해서 줍니다
public LoginRequest getLoginInput() {
    String id = idField.getText();
    String pw = new String(pwField.getPassword());
    return new LoginRequest(id, pw);
}
```
### 2. 리스너 부착 메서드 (Controller가 버튼을 누를 수 있게)
```java
// Controller가 이 메서드를 호출하여 이벤트를 연결합니다.
public void addLoginListener(ActionListener listener) {
    loginButton.addActionListener(listener);
}
```