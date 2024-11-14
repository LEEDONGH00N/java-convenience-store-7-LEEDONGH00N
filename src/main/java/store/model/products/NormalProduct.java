package store.model.products;

public class NormalProduct{

    private int stocks;

    public NormalProduct(int stock) {
        this.stocks = stock;
    }

    public int getStocks() {
        return stocks;
    }

    public int purchase(int purchaseQuantity){
        stocks -= purchaseQuantity;
        return purchaseQuantity;
    }

    public boolean canPurchase(int purchaseQuantity){
        return purchaseQuantity <= stocks;
    }
}
