package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.model.products.Product;
import store.model.receipt.Purchase;
import store.model.receipt.Receipt;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ReceiptTest {

    private Receipt receipt;
    private Product cola;
    private Product soda;
    private Product water;

    @BeforeEach
    void setUp() {
        // 테스트용 상품 생성
        cola = new Product("콜라", 1000);
        soda = new Product("사이다", 1200);
        water = new Product("물", 500);

        // 영수증 객체 초기화
        receipt = new Receipt();
    }

    @Test
    @DisplayName("구매 항목 추가 및 총 금액 계산 테스트")
    void testAddPurchaseAndCalculateTotal() {
        // 구매 내역 추가
        Purchase purchase1 = new Purchase(cola, 10, 1, 1);
        Purchase purchase2 = new Purchase(soda, 5, 0, 1);
        Purchase purchase3 = new Purchase(water, 20, 2, 1);

        receipt.addPurchase(purchase1);
        receipt.addPurchase(purchase2);
        receipt.addPurchase(purchase3);
        receipt.adjust();

        // 총 금액 확인
        assertThat(receipt.getTotalPrice()).isEqualTo(1000 * 10 + 1200 * 5 + 500 * 20);
        assertThat(receipt.getTotalCount()).isEqualTo(10 + 5 + 20);
    }

    @Test
    @DisplayName("프로모션 할인 금액 계산 테스트")
    void testCalculatePromotionDiscount() {
        // 프로모션 적용된 구매 내역 추가
        Purchase purchase1 = new Purchase(cola, 10, 1, 1); // 콜라 1개 증정
        Purchase purchase2 = new Purchase(soda, 5, 1, 1); // 사이다 1개 증정
        receipt.addPurchase(purchase1);
        receipt.addPurchase(purchase2);
        receipt.adjust();

        // 프로모션 할인 금액 확인
        int expectedPromotionDiscount = cola.getPrice() * 1 + soda.getPrice() * 1;
        assertThat(receipt.getPromotionDiscountAmount()).isEqualTo(expectedPromotionDiscount);
    }

    @Test
    @DisplayName("멤버십 할인 적용 후 최종 결제 금액 테스트")
    void testCalculatePaymentAmountWithMembershipDiscount() {
        // 구매 내역 추가
        Purchase purchase1 = new Purchase(cola, 10, 1, 1);
        Purchase purchase2 = new Purchase(water, 20, 2, 1);
        receipt.addPurchase(purchase1);
        receipt.addPurchase(purchase2);

        // 멤버십 할인 적용
        receipt.setMembershipDiscountAmount(1000); // 멤버십 할인 1000원
        receipt.adjust();

        // 최종 결제 금액 확인
        int expectedPaymentAmount = receipt.getTotalPrice() - receipt.getPromotionDiscountAmount() - 1000;
        assertThat(receipt.getPaymentAmount()).isEqualTo(expectedPaymentAmount);
    }

    @Test
    @DisplayName("증정 상품 내역 확인 테스트")
    void testGiveAwayItems() {
        Purchase purchase1 = new Purchase(cola, 10, 2, 1); // 콜라 2개 증정
        receipt.addPurchase(purchase1);
        receipt.adjust();

        // 증정 상품 개수 확인
        assertThat(purchase1.getGiveAwayCount()).isEqualTo(2);
        assertThat(purchase1.getPromotionDiscounted()).isEqualTo(cola.getPrice() * 2);
    }
}
