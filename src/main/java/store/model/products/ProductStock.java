package store.model.products;

public class ProductStock {

    private final Product product;
    private NormalProduct normalProduct;
    private PromotionProduct promotionProduct;

    public ProductStock(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public NormalProduct getNormalProduct() {
        return normalProduct;
    }

    public PromotionProduct getPromotionProduct() {
        return promotionProduct;
    }

    public boolean hasPromotionStock() {
        return promotionProduct != null;
    }

    public boolean hasNormalStock() {
        return normalProduct != null;
    }

    public void setNormalStock(NormalProduct normalStock) {
        this.normalProduct = normalStock;
    }

    public void setPromotionStock(PromotionProduct promotionStock) {
        this.promotionProduct = promotionStock;
    }
}
