package com.project.app.service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 로그인 비즈니스 로직을 처리하는 서비스 클래스
 *
 * 기능:
 * - users.txt 파일에서 사용자 정보를 로드
 * - 로그인 검증 로직 수행 (ID/PW 확인)
 *
 * 주요 내용:
 * 1. 생성자에서 users.txt 파일을 읽어 HashMap에 저장
 * 2. login 메서드에서 입력값 검증 및 사용자 인증 수행
 */
public class SignInService {

    private Map<String, String> users = new HashMap<>();

    public SignInService() {
        loadUsers();
    }

    private void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String id = parts[0].trim();
                    String pw = parts[1].trim();
                    users.put(id, pw);
                }
            }
        } catch (IOException e) {
            System.out.println("users.txt 파일을 읽을 수 없습니다: " + e.getMessage());
        }
    }

    public String login(String id, String password) {
        if (id.isEmpty() || password.isEmpty()) {
            return "EMPTY";
        }
        
        if (!users.containsKey(id)) {
            return "USER_NOT_FOUND";
        }
        
        if (!users.get(id).equals(password)) {
            return "WRONG_PASSWORD";
        }
        
        return "SUCCESS";
    }
}