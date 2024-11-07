package store.model.products;

public class ProductStock {

    private final Product product;
    private NormalStock normalStock;
    private PromotionStock promotionStock;

    public ProductStock(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public NormalStock getNormalStock() {
        return normalStock;
    }

    public PromotionStock getPromotionStock() {
        return promotionStock;
    }

    public boolean hasPromotionStock() {
        return promotionStock != null;
    }

    public boolean hasNormalStock() {
        return normalStock != null;
    }

    public void setNormalStock(NormalStock normalStock) {
        this.normalStock = normalStock;
    }

    public void setPromotionStock(PromotionStock promotionStock) {
        this.promotionStock = promotionStock;
    }
}
