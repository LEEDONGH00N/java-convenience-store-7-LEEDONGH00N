package store.model.products;


public class PromotionStock {

    private int quantity;
    private String promotionType;

    public PromotionStock(int quantity, String promotionType) {
        this.quantity = quantity;
        this.promotionType = promotionType;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPromotionType() {
        return promotionType;
    }

}
