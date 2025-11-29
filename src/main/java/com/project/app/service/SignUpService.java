package com.project.app.service;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

/**
 * 회원가입 비즈니스 로직
 *
 * - UserData.txt에 사용자 정보를 추가
 * - 형식: 사용자ID/비밀번호/이름/생년월일/학년/신청강의목록/결제내역목록
 */
public class SignUpService {

    private static final String USER_DATA_FILE = "UserData.txt";

    // 회원가입 처리 메서드
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

        // 생년월일 기준 학년 자동 계산 (정수)
        int gradeInt = calculateGradeFromBirth(year, month, day);
        String gradeLabel = gradeIntToLabel(gradeInt);  // "고1" / "고2" / ...

        // ID 중복 체크 (UserData.txt 기준)
        if (isDuplicateId(id)) {
            JOptionPane.showMessageDialog(parent, "이미 존재하는 아이디입니다.", "중복 오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ★ UserData.txt 파일에 저장
        File file = new File(USER_DATA_FILE);
        boolean needHeader = !file.exists() || file.length() == 0;

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {

            if (needHeader) {
                writer.write("사용자ID/비밀번호/이름/생년월일/학년/신청강의목록/결제내역목록");
                writer.newLine();
            }

            // 처음 회원가입 시 신청강의목록, 결제내역목록은 비워둠 ("", "")
            String enrolledLectures = "";
            String paymentIds = "";

            String line = String.join("/",
                    id,
                    password,
                    name,
                    birth,
                    gradeLabel,
                    enrolledLectures,
                    paymentIds
            );
            writer.write(line);
            writer.newLine();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parent, "회원정보 저장 중 오류가 발생했습니다.", "파일 오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(parent, "회원가입이 완료되었습니다!");
        clearFields(tfId, pfPw, tfName, cbYear, cbMonth, cbDay);
    }

    public boolean isDuplicateId(String id) {
        File file = new File(USER_DATA_FILE);
        if (!file.exists()) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // 헤더 스킵
                if (first) {
                    first = false;
                    if (line.startsWith("사용자ID")) {
                        continue;
                    }
                }

                String[] parts = line.split("/");
                if (parts.length > 0 && parts[0].trim().equals(id)) {
                    return true;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 월별 날짜 수 계산 (윤년 고려)
     */
    public int getDaysInMonth(int year, int month) {
        switch (month) {
            case 2: return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0) ? 29 : 28;
            case 4:
            case 6:
            case 9:
            case 11: return 30;
            default: return 31;
        }
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

    // 생년월일을 기준으로 현재 학년(고1~고3 / N수)을 추정 (정수)
    private int calculateGradeFromBirth(int year, int month, int day) {
        Calendar now = Calendar.getInstance();
        int currentYear = now.get(Calendar.YEAR);

        int diff = currentYear - year;

        if (diff == 16) return 1; // 고1
        if (diff == 17) return 2; // 고2
        if (diff == 18) return 3; // 고3
        if (diff >= 19) return 4; // N수
        return 0; // 아직 고등학생이 아닌 경우
    }

    private String gradeIntToLabel(int grade) {
        return switch (grade) {
            case 1 -> "고1";
            case 2 -> "고2";
            case 3 -> "고3";
            case 4 -> "N수";
            default -> "미지정";
        };
    }
}
