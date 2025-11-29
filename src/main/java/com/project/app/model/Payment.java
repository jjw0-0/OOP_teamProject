// src/main/java/com/project/app/model/Payment.java
package com.project.app.model;

/**
 * 결제 정보 엔티티 (마이페이지/결제내역용)
 *
 * PaymentData.txt 형식:
 * 결제ID/사용자ID/강의ID/결제금액/교재구매여부/결제수단/결제일시/결제상태
 */
public class Payment {

    private final String paymentId;   // 결제ID
    private final String userId;      // 사용자ID
    private final String lectureId;   // 강의ID (예: L027)
    private final int amount;         // 결제금액 (양수: 지출 금액)
    private final String textbookFlag; // 교재구매여부 (예: Y/N, O/X 등)
    private final String method;      // 결제수단 (카드, 현금 등)
    private final String paymentDate; // 결제일시
    private final String status;      // 결제상태 (완료, 취소 등)

    public Payment(String paymentId,
                   String userId,
                   String lectureId,
                   int amount,
                   String textbookFlag,
                   String method,
                   String paymentDate,
                   String status) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.lectureId = lectureId;
        this.amount = amount;
        this.textbookFlag = textbookFlag;
        this.method = method;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getUserId() {
        return userId;
    }

    public String getLectureId() {
        return lectureId;
    }

    public int getAmount() {
        return amount;
    }

    public String getTextbookFlag() {
        return textbookFlag;
    }

    public String getMethod() {
        return method;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getStatus() {
        return status;
    }
}
