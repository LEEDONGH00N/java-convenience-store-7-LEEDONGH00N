package store.views;

import store.model.products.ProductStock;

import java.util.List;

public class OutputView {

    private static final String OUTPUT_WELCOME_MESSAGE = "안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n";
    private static final String OUTPUT_ASK_PURCHASE_MESSAGE = "\n구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String OUTPUT_NORMAL_PRODUCT_DISPLAY_FORMAT = "- %s %,d원 %d개";
    private static final String OUTPUT_NORMAL_PRODUCT_NO_QUANTITY_DISPLAY_FORMAT = "- %s %,d원 재고 없음";
    private static final String OUTPUT_PROMOTION_PRODUCT_DISPLAY_FORMAT = "- %s %,d원 %d개 %s";
    private static final String OUTPUT_PROMOTION_PRODUCT_NO_QUANTITY_DISPLAY_FORMAT = "- %s %,d원 재고 없음 %s";

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
        System.out.println(OUTPUT_ASK_PURCHASE_MESSAGE);
    }

    private static String formatPromotionProductDisplay(ProductStock productStock) {
        if (productStock.getPromotionStock().getQuantity() > 0) {
            return String.format(OUTPUT_PROMOTION_PRODUCT_DISPLAY_FORMAT,
                    productStock.getProduct().getName(),
                    productStock.getProduct().getPrice(),
                    productStock.getPromotionStock().getQuantity(),
                    productStock.getPromotionStock().getPromotionType());
        } else {
            return String.format(OUTPUT_PROMOTION_PRODUCT_NO_QUANTITY_DISPLAY_FORMAT,
                    productStock.getProduct().getName(),
                    productStock.getProduct().getPrice(),
                    productStock.getPromotionStock().getPromotionType());
        }
    }

    private static String formatNormalProductDisplay(ProductStock productStock) {
        if (productStock.getNormalStock().getQuantity() > 0) {
            return String.format(OUTPUT_NORMAL_PRODUCT_DISPLAY_FORMAT,
                    productStock.getProduct().getName(),
                    productStock.getProduct().getPrice(),
                    productStock.getNormalStock().getQuantity());
        } else {
            return String.format(OUTPUT_NORMAL_PRODUCT_NO_QUANTITY_DISPLAY_FORMAT,
                    productStock.getProduct().getName(),
                    productStock.getProduct().getPrice());
        }
    }
}
