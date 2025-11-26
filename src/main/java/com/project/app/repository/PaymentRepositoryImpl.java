package com.project.app.repository;

import com.project.app.model.Payment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * PaymentRepository 구현체 (인메모리 버전)
 *
 * - 애플리케이션 실행 중 메모리에 결제 정보를 보관
 * - 데모용으로 userId "user01"에 대한 결제 데이터를 몇 개 추가
 */
public class PaymentRepositoryImpl implements PaymentRepository {

    private static final PaymentRepositoryImpl instance = new PaymentRepositoryImpl();

    public static PaymentRepositoryImpl getInstance() {
        return instance;
    }

    private final List<Payment> payments = new ArrayList<>();
    private int sequence = 1;

    private PaymentRepositoryImpl() {
        // 데모용 기본 결제 데이터
        // 강의 결제
        save(new Payment(nextId(), "user01", 1, "고등 수학 I", -150_000, "LECTURE", "2025-10-01"));
        save(new Payment(nextId(), "user01", 2, "물리 기본 개념", -140_000, "LECTURE", "2025-10-05"));
        save(new Payment(nextId(), "user01", 3, "한국사 입문", -120_000, "LECTURE", "2025-10-10"));
        save(new Payment(nextId(), "user01", 4, "문학 읽기 I", -135_000, "LECTURE", "2025-10-15"));
        save(new Payment(nextId(), "user01", 5, "수학 II", -155_000, "LECTURE", "2025-10-20"));

        // 교재 결제
        save(new Payment(nextId(), "user01", 1, "수학의 정석", -25_000, "TEXTBOOK", "2025-10-02"));
        save(new Payment(nextId(), "user01", 2, "고등 물리 개념서", -22_000, "TEXTBOOK", "2025-10-06"));
        save(new Payment(nextId(), "user01", 3, "한국사 바로알기", -15_000, "TEXTBOOK", "2025-10-11"));
        save(new Payment(nextId(), "user01", 4, "현대문학 작품 읽기", -20_000, "TEXTBOOK", "2025-10-16"));
        save(new Payment(nextId(), "user01", 5, "수학의 정석 (심화)", -28_000, "TEXTBOOK", "2025-10-21"));
    }

    private int nextId() {
        return sequence++;
    }

    @Override
    public synchronized List<Payment> findByUserId(String userId) {
        List<Payment> result = new ArrayList<>();
        if (userId == null) {
            return result;
        }
        for (Payment p : payments) {
            if (userId.equals(p.getUserId())) {
                result.add(p);
            }
        }
        // 시간순으로 넣었다고 가정 → 이미 정렬된 상태
        return Collections.unmodifiableList(result);
    }

    @Override
    public synchronized void save(Payment payment) {
        if (payment == null) return;
        payments.add(payment);
    }
}
