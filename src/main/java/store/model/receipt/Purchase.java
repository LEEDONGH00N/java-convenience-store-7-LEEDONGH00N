package store.model.receipt;

import store.model.products.Product;

public class Purchase {

    private final Product product;
    private final int totalAmount;
    private final int totalQuantity;
    private final int giveAwayQuantity;
    private final int promotionAmount;

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
}
