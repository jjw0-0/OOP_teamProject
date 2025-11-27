package com.project.app.repository;

import java.util.List;

import com.project.app.model.Payment;

/**
 * PaymentRepository
 *
 * - Payment 엔티티에 대한 데이터 접근 인터페이스
 * - 현재는 인메모리 구현(PaymentRepositoryImpl)을 사용
 */
public interface PaymentRepository {

    /**
     * 특정 사용자 ID의 결제 내역 조회
     */
    List<Payment> findByUserId(String userId);

    /**
     * 결제 내역 저장
     */
    void save(Payment payment);
}
