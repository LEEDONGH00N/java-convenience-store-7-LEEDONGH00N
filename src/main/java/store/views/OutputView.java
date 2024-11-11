package store.views;

import store.model.products.Product;
import store.model.products.ProductStock;
import store.model.receipt.Purchase;
import store.model.receipt.Receipt;

import java.util.List;

public class OutputView {

    private static final String OUTPUT_WELCOME_MESSAGE = "안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n";
    private static final String OUTPUT_NORMAL_PRODUCT_DISPLAY_FORMAT = "- %s %,d원 %d개";
    private static final String OUTPUT_NORMAL_PRODUCT_NO_QUANTITY_DISPLAY_FORMAT = "- %s %,d원 재고 없음";
    private static final String OUTPUT_PROMOTION_PRODUCT_DISPLAY_FORMAT = "- %s %,d원 %d개 %s";
    private static final String OUTPUT_PROMOTION_PRODUCT_NO_QUANTITY_DISPLAY_FORMAT = "- %s %,d원 재고 없음 %s";
    private static final String OUTPUT_PROMOTION_PRODUCT_CANNOT_GIVE_GIVEAWAY = "프로모션 재고가 부족하여 기존 주문량으로 주문 도와드리겠습니다.";
    private static final String OUTPUT_ERROR_PREFIX = "[ERROR] ";
    private static final String OUTPUT_RECEIPT_CONV = "==============W 편의점================";
    private static final String OUTPUT_RECEIPT_GIVEAWAY_DIVIDER = "=============증\t\t정===============";
    private static final String OUTPUT_RECEIPT_AMOUNT_DIVIDER = "====================================";
    private static final String OUTPUT_RECEIPT_PROPERTIES = "%-10s %10s %10s";
    private static final String OUTPUT_RECEIPT_PURCHASES = "%-10s %10s \t %10s";
    private static final String OUTPUT_RECEIPT_GIVEAWAY = "%-10s %10s";
    private static final String OUTPUT_RECEIPT_TOTAL_AMOUNT = "%-10s %10s %10s";
    private static final String OUTPUT_RECEIPT_PROMOTION_DISCOUNT = "%-10s \t\t\t %10s";
    private static final String OUTPUT_RECEIPT_MEMBERSHIP_DISCOUNT = "%-10s \t\t\t %10s";
    private static final String OUTPUT_RECEIPT_PAYMENT = "%-10s \t\t\t %10s";

    public static void outputWelcomeMessage(){
        System.out.println(OUTPUT_WELCOME_MESSAGE);
    }

    public static void outputCurrentStock(List<ProductStock> productStocks){
        for (ProductStock productStock : productStocks){
            if(productStock.hasPromotionStock()) {
                System.out.println(formatPromotionProductDisplay(productStock));
            }
            System.out.println(formatNormalProductDisplay(productStock));
        }
    }

    public static void outputPromotionCannotGiveGiveaway(){
        System.out.println(OUTPUT_PROMOTION_PRODUCT_CANNOT_GIVE_GIVEAWAY);
    }

    private static String formatPromotionProductDisplay(ProductStock productStock) {
        if (productStock.getPromotionProduct().getStocks() > 0) {
            return String.format(OUTPUT_PROMOTION_PRODUCT_DISPLAY_FORMAT,
                    productStock.getProduct().getName(),
                    productStock.getProduct().getPrice(),
                    productStock.getPromotionProduct().getStocks(),
                    productStock.getPromotionProduct().getPromotion().getName());
        } else {
            return String.format(OUTPUT_PROMOTION_PRODUCT_NO_QUANTITY_DISPLAY_FORMAT,
                    productStock.getProduct().getName(),
                    productStock.getProduct().getPrice(),
                    productStock.getPromotionProduct().getPromotion().getName());
        }
    }

    private static String formatNormalProductDisplay(ProductStock productStock) {
        if (productStock.getNormalProduct().getStocks() > 0) {
            return String.format(OUTPUT_NORMAL_PRODUCT_DISPLAY_FORMAT,
                    productStock.getProduct().getName(),
                    productStock.getProduct().getPrice(),
                    productStock.getNormalProduct().getStocks());
        } else {
            return String.format(OUTPUT_NORMAL_PRODUCT_NO_QUANTITY_DISPLAY_FORMAT,
                    productStock.getProduct().getName(),
                    productStock.getProduct().getPrice());
        }
    }

    public static void outputErrorMessage(String message){
        System.out.println(OUTPUT_ERROR_PREFIX + message);
    }

    public static void outputReceipt(Receipt receipt){
        System.out.println(OUTPUT_RECEIPT_CONV);
        System.out.println(String.format(OUTPUT_RECEIPT_PROPERTIES, "상품명", "수량", "금액"));
        for (Purchase purchase : receipt.getPurchases()){
            System.out.println(String.format(OUTPUT_RECEIPT_PURCHASES, purchase.getProduct().getName(), purchase.getTotalQuantity(), purchase.getTotalAmount()));
        }
        System.out.println(OUTPUT_RECEIPT_GIVEAWAY_DIVIDER);
        for (Purchase purchase : receipt.getPurchases()){
            if(purchase.getGiveAwayCount() != 0) {
                System.out.println(String.format(OUTPUT_RECEIPT_GIVEAWAY, purchase.getProduct().getName(), purchase.getGiveAwayCount()));
            }
        }
        System.out.println(OUTPUT_RECEIPT_AMOUNT_DIVIDER);
        System.out.println(String.format(OUTPUT_RECEIPT_TOTAL_AMOUNT, "총구매액", receipt.getTotalCount(), receipt.getTotalPrice()));
        System.out.println(String.format(OUTPUT_RECEIPT_PROMOTION_DISCOUNT, "행사할인", receipt.getPromotionDiscountAmount()));
        System.out.println(String.format(OUTPUT_RECEIPT_MEMBERSHIP_DISCOUNT, "멤버십할인", receipt.getMembershipDiscountAmount()));
        System.out.println(String.format(OUTPUT_RECEIPT_PAYMENT, "내실돈", receipt.getPaymentAmount()));
    }
}
