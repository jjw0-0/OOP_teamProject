package user;

import javax.swing.*;
import java.io.*;

// 로직 전용
public class SignUpService {

    public void handleSignUp(JTextField tfId, JPasswordField pfPw, JTextField tfName,
                             JComboBox<Integer> cbYear, JComboBox<Integer> cbMonth, JComboBox<Integer> cbDay,
                             JFrame frame) {
        String name = tfName.getText().trim();
        String id = tfId.getText().trim();
        String password = new String(pfPw.getPassword()).trim();

        if (name.isEmpty() || id.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "모든 정보를 입력하세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String forbidden = "\"'\\/:;,=?*";
        for (char c : password.toCharArray()) {
            if (forbidden.indexOf(c) != -1) {
                JOptionPane.showMessageDialog(frame,
                        "비밀번호에 사용할 수 없는 특수문자가 포함되어 있습니다.\n" +
                                "사용 불가 문자: \" ' \\ / : ; , = ? *",
                        "비밀번호 오류",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(frame, "비밀번호는 6자리 이상이어야 합니다.", "비밀번호 오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int year = (int) cbYear.getSelectedItem();
        int month = (int) cbMonth.getSelectedItem();
        int day = (int) cbDay.getSelectedItem();
        String birth = String.format("%04d-%02d-%02d", year, month, day);

        if (isDuplicateId(id)) {
            JOptionPane.showMessageDialog(frame, "이미 존재하는 아이디입니다.", "중복 오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
            writer.write(id + "," + password + "," + name + "," + birth);
            writer.newLine();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "회원정보 저장 중 오류가 발생했습니다.", "파일 오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(frame, "회원가입이 완료되었습니다!");
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

    public int getDaysInMonth(int year, int month) {
        switch (month) {
            case 2: return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0) ? 29 : 28;
            case 4: case 6: case 9: case 11: return 30;
            default: return 31;
        }
    }

    public void handleCancel(JFrame frame) {
        frame.dispose();
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
