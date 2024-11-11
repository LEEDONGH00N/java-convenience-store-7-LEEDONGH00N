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
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PromotionTest {

    private ProductStock productStock;
    private PromotionProduct promotionProduct;

    @BeforeEach
    void setup(){
        Product product = new Product("콜라", 1000);
        Promotion promotion = new Promotion("2+1", 2, 1, LocalDate.parse("2024-01-01"), LocalDate.parse("2024-12-31"));
        productStock = new ProductStock(product);
        productStock.setPromotionStock(new PromotionProduct(5, promotion));
        productStock.setNormalStock(new NormalProduct(5));
        promotionProduct = productStock.getPromotionProduct();
    }

    @Test
    @DisplayName("프로모션 기간이 아님")
    void 현재_프로모션_기간이_아님() {
        LocalDateTime past = LocalDateTime.parse("2023-12-31T15:34:50.188975");
        assertFalse(promotionProduct.isPromotionAvailable(past));
    }

    @Test
    @DisplayName("요청 수량이 제고보다 많다")
    void 재고_부족(){
        int purchaseQuantity = 8;
        System.out.println(promotionProduct.quantityCannotGetPromotion(purchaseQuantity));
        assertFalse(promotionProduct.canPurchase(purchaseQuantity));
    }

    @Test
    @DisplayName("추가 증정을 받을 수 있는 경우")
    void 추가_증정_가능(){
        int purchaseQuantity = 5;
        assertTrue(promotionProduct.canGetAdditional(purchaseQuantity));
    }

    @Test
    @DisplayName("추가 증정을 받을 수 있는 경우")
    void 추가_증정_가능_Y(){
        int purchaseQuantity = 2;
        assertTrue(promotionProduct.canGetAdditional(purchaseQuantity));

        // 더 받겠다고 함
        purchaseQuantity += promotionProduct.getGiveaway();

        assertTrue(promotionProduct.canPurchase(purchaseQuantity));
        promotionProduct.purchase(purchaseQuantity);
        assertEquals(2, promotionProduct.getStocks());
    }

    @Test
    @DisplayName("추가 증정을 받을 수 있는 경우")
    void 추가_증정_가능_N(){
        int purchaseQuantity = 2;
        assertTrue(promotionProduct.canGetAdditional(purchaseQuantity));

        // 더 받겠다고 안함
        promotionProduct.purchase(purchaseQuantity);
        assertEquals(3, promotionProduct.getStocks());
    }
}
