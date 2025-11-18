package com.project.app.service;

import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * 회원가입 비즈니스 로직을 처리하는 서비스 클래스
 *
 * 기능:
 * - 회원가입 입력값 검증 (필수 입력, 비밀번호 규칙, ID 중복 체크 등)
 * - users.txt 파일에 사용자 정보 저장
 * - 날짜 계산 유틸리티 제공 (윤년 처리 등)
 *
 * 주요 내용:
 * 1. handleSignUp: 모든 입력값 검증 후 파일에 저장
 * 2. isDuplicateId: users.txt를 읽어 ID 중복 확인
 * 3. getDaysInMonth: 월별 날짜 수 계산 (윤년 고려)
 */
public class SignUpService {

    /**
     * 회원가입 처리 메서드
     *
     * @param parent 부모 컴포넌트 (메시지 다이얼로그 표시용, JPanel 또는 JFrame 모두 가능)
     */
    public void handleSignUp(JTextField tfId, JPasswordField pfPw, JTextField tfName,
                             JComboBox<Integer> cbYear, JComboBox<Integer> cbMonth, JComboBox<Integer> cbDay,
                             Component parent) {
        String name = tfName.getText().trim();
        String id = tfId.getText().trim();
        String password = new String(pfPw.getPassword()).trim();

        if (name.isEmpty() || id.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "모든 정보를 입력하세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String forbidden = "\"'\\/:;,=?*";
        for (char c : password.toCharArray()) {
            if (forbidden.indexOf(c) != -1) {
                JOptionPane.showMessageDialog(parent,
                        "비밀번호에 사용할 수 없는 특수문자가 포함되어 있습니다.\n" +
                                "사용 불가 문자: \" ' \\ / : ; , = ? *",
                        "비밀번호 오류",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(parent, "비밀번호는 6자리 이상이어야 합니다.", "비밀번호 오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int year = (int) cbYear.getSelectedItem();
        int month = (int) cbMonth.getSelectedItem();
        int day = (int) cbDay.getSelectedItem();
        String birth = String.format("%04d-%02d-%02d", year, month, day);

        if (isDuplicateId(id)) {
            JOptionPane.showMessageDialog(parent, "이미 존재하는 아이디입니다.", "중복 오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
            writer.write(id + "," + password + "," + name + "," + birth);
            writer.newLine();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parent, "회원정보 저장 중 오류가 발생했습니다.", "파일 오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(parent, "회원가입이 완료되었습니다!");
        clearFields(tfId, pfPw, tfName, cbYear, cbMonth, cbDay);
    }

    public boolean isDuplicateId(String id) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(id)) return true;
            }
        } catch (IOException e) {}
        return false;
    }

    /**
     * 월별 날짜 수 계산 (윤년 고려)
     *
     * @param year 연도
     * @param month 월 (1~12)
     * @return 해당 월의 날짜 수
     */
    public int getDaysInMonth(int year, int month) {
        switch (month) {
            case 2: return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0) ? 29 : 28;
            case 4: case 6: case 9: case 11: return 30;
            default: return 31;
        }
    }

    /**
     * 취소 버튼 처리 메서드
     *
     * 주의: JPanel로 변환 후에는 dispose() 대신 화면 전환 로직이 필요합니다.
     * 현재는 사용하지 않으며, 향후 SidePanel 전환 로직으로 대체될 예정입니다.
     *
     * @param component 부모 컴포넌트 (사용 안 함)
     */
    @Deprecated
    public void handleCancel(Component component) {
        // JPanel에서는 dispose() 불가능
        // TODO: SidePanel로 이전 화면 전환 로직 구현 필요
        // 예: SidePanel.getInstance().showContent(new SignInView());
    }

    private void clearFields(JTextField tfId, JPasswordField pfPw, JTextField tfName,
                             JComboBox<Integer> cbYear, JComboBox<Integer> cbMonth, JComboBox<Integer> cbDay) {
        tfName.setText("");
        tfId.setText("");
        pfPw.setText("");
        cbYear.setSelectedIndex(0);
        cbMonth.setSelectedIndex(0);
        cbDay.setSelectedIndex(0);
    }
}
