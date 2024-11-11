package store.model.receipt;

import store.model.products.Product;

public class Purchase {

    private Product product;
    private int totalAmount; // 총 금액
    private int totalQuantity; // 총 개수
    private int giveAwayQuantity; // 증정품 개수
    private int promotionAmount; // 프로모션 적용 금액

    public Purchase(Product product, int totalQuantity, int giveAwayQuantity, int promotionQuantityPerPromotion) {
        this.product = product;
        this.totalQuantity = totalQuantity;
        this.giveAwayQuantity = giveAwayQuantity;
        this.totalAmount = product.getPrice() * totalQuantity;
        this.promotionAmount = product.getPrice() * giveAwayQuantity * promotionQuantityPerPromotion;
    }

    public Product getProduct() {
        return product;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getGiveAwayCount() {
        return giveAwayQuantity;
    }

    public int getPriceWithoutPromotion(){
        return totalAmount - promotionAmount;
    }

    public int getPromotionDiscounted() {
        return product.getPrice() * giveAwayQuantity;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "product=" + product.getName() +
                ", totalAmount=" + totalAmount +
                ", totalQuantity=" + totalQuantity +
                ", giveAwayQuantity=" + giveAwayQuantity +
                ", promotionAmount=" + promotionAmount +
                '}';
    }
}
