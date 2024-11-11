package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.model.products.NormalProduct;
import store.model.products.Product;
import store.model.products.ProductStock;
import store.model.products.PromotionProduct;
import store.model.promotions.Promotion;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PromotionTest {

    private ProductStock productStock;
    private PromotionProduct promotionProduct;

    @BeforeEach
    void setup() {
        Product product = new Product("콜라", 1000);
        Promotion promotion = new Promotion("2+1", 2, 1, LocalDate.parse("2024-01-01"), LocalDate.parse("2024-12-31"));
        productStock = new ProductStock(product);
        productStock.setPromotionStock(new PromotionProduct(5, promotion));
        productStock.setNormalStock(new NormalProduct(5));
        promotionProduct = productStock.getPromotionProduct();
    }

    @Test
    @DisplayName("프로모션이 유효하지 않은 기간")
    void shouldReturnFalseWhenPromotionIsNotActive() {
        LocalDateTime pastDate = LocalDateTime.parse("2023-12-31T15:34:50.188975");
        assertFalse(promotionProduct.isPromotionAvailable(pastDate), "프로모션 기간이 아닐 경우 false 반환");
    }

    @Test
    @DisplayName("요청 수량이 재고보다 많을 때 구매 불가")
    void shouldReturnFalseWhenInsufficientStock() {
        int purchaseQuantity = 8;
        System.out.println(promotionProduct.quantityCannotGetPromotion(purchaseQuantity));
        assertFalse(promotionProduct.canPurchase(purchaseQuantity), "재고 부족으로 구매 불가");
    }

    @Test
    @DisplayName("추가 증정이 가능한 경우")
    void shouldAllowAdditionalGiveaway() {
        int purchaseQuantity = 5;
        assertTrue(promotionProduct.canGetAdditional(purchaseQuantity), "구매 수량에 따라 추가 증정 가능");
    }

    @Test
    @DisplayName("추가 증정을 받고 구매할 수 있는 경우")
    void shouldAllowPurchaseWithAdditionalGiveaway() {
        int purchaseQuantity = 2;
        assertTrue(promotionProduct.canGetAdditional(purchaseQuantity), "추가 증정 가능");

        // 추가 증정을 받음
        purchaseQuantity += promotionProduct.getGiveaway();

        assertTrue(promotionProduct.canPurchase(purchaseQuantity), "증정 포함 구매 가능");
        promotionProduct.purchase(purchaseQuantity);
        assertEquals(2, promotionProduct.getStocks(), "구매 후 남은 재고 확인");
    }

    @Test
    @DisplayName("추가 증정을 받지 않고 구매하는 경우")
    void shouldAllowPurchaseWithoutAdditionalGiveaway() {
        int purchaseQuantity = 2;
        assertTrue(promotionProduct.canGetAdditional(purchaseQuantity), "추가 증정 가능");

        // 추가 증정을 받지 않음
        promotionProduct.purchase(purchaseQuantity);
        assertEquals(3, promotionProduct.getStocks(), "추가 증정 없이 구매 후 남은 재고 확인");
    }
}