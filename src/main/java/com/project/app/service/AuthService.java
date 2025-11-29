package com.project.app.service;

import com.project.app.model.User;

/**
 * 전역 로그인 상태를 관리하는 AuthService
 *
 * - 로그인 성공 시 현재 사용자(User)를 보관
 * - 어디서나 로그인 여부 / 현재 사용자 정보 조회 가능
 */
public class AuthService {

    // 현재 로그인한 사용자 (없으면 null)
    private static User currentUser;

    // 로그인 성공 시 호출: 현재 사용자 저장
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    // 현재 로그인한 사용자 반환 (없으면 null)
    public static User getCurrentUser() {
        return currentUser;
    }

    // 로그인 되어 있는지 여부
    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    // 로그아웃
    public static void logout() {
        currentUser = null;
    }
}