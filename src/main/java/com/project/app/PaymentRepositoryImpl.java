package com.project.app.repository;

import com.project.app.model.Payment;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * PaymentRepository 구현체 (파일 기반 버전)
 *
 * - PaymentData.txt 파일에서 결제 정보를 읽어 메모리에 보관
 * - 형식 (현재 기준):
 *   결제ID/사용자ID/강의ID/결제금액/교재구매여부/결제수단/결제일시/결제상태
 */
public class PaymentRepositoryImpl implements PaymentRepository {

    private static final PaymentRepositoryImpl instance = new PaymentRepositoryImpl();

    public static PaymentRepositoryImpl getInstance() {
        return instance;
    }

    private static final String PAYMENT_DATA_FILE = "PaymentData.txt";

    private final List<Payment> payments = new ArrayList<>();

    private PaymentRepositoryImpl() {
        loadFromFile();
    }

    /**
     * PaymentData.txt 를 읽어서 payments 리스트에 적재
     *
     * 형식:
     *  결제ID/사용자ID/강의ID/결제금액/교재구매여부/결제수단/결제일시/결제상태
     */
    private void loadFromFile() {
        payments.clear();

        File file = new File(PAYMENT_DATA_FILE);
        if (!file.exists()) {
            System.err.println("[PaymentRepositoryImpl] 데이터 파일 없음: " + file.getAbsolutePath());
            return;
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // 헤더 스킵
                if (firstLine) {
                    firstLine = false;
                    if (line.startsWith("결제ID")) {
                        continue;
                    }
                }

                String[] parts = line.split("/");
                if (parts.length < 8) {
                    System.err.println("[PaymentRepositoryImpl] 잘못된 형식의 라인: " + line);
                    continue;
                }

                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim();
                }

                String paymentId    = parts[0]; // 예: PAY001_1
                String userId       = parts[1]; // 예: user001
                String lectureId    = parts[2]; // 예: L027
                int amount          = parseInt(parts[3], 0); // 양수 금액
                String textbookFlag = parts[4]; // 예: "Y" / "N"
                String method       = parts[5]; // 카드, 계좌이체 등
                String paymentDate  = parts[6]; // "2025-10-01 18:30" 등
                String status       = parts[7]; // 결제완료, 취소 등

                // ✅ 현재 Payment 모델에 맞는 생성자 호출
                Payment payment = new Payment(
                        paymentId,
                        userId,
                        lectureId,
                        amount,
                        textbookFlag,
                        method,
                        paymentDate,
                        status
                );

                payments.add(payment);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int parseInt(String s, int defaultValue) {
        if (s == null) return defaultValue;
        try {
            return Integer.parseInt(s.replace(",", "").trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    public synchronized List<Payment> findByUserId(String userId) {
        List<Payment> result = new ArrayList<>();
        if (userId == null) return result;

        for (Payment p : payments) {
            if (userId.equals(p.getUserId())) {
                result.add(p);
            }
        }
        // 시간 순서대로 기록되어 있다고 가정 → 그대로 반환
        return Collections.unmodifiableList(result);
    }

    @Override
    public synchronized void save(Payment payment) {
        if (payment == null) return;
        payments.add(payment);
        // TODO: 필요하다면 PaymentData.txt에 append 하는 로직 추가
    }
}
