package store.model.products;


import store.model.promotions.Promotion;

import java.time.LocalDateTime;

public class PromotionProduct {

    private int stocks;
    private Promotion promotion;

    public PromotionProduct(int stocks, Promotion promotion) {
        this.stocks = stocks;
        this.promotion = promotion;
    }

    public int getStocks() {
        return stocks;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public boolean isPromotionAvailable(LocalDateTime now){
        return promotion.compareDate(now);
    }

    public boolean canPurchase(int purchaseQuantity){
        return purchaseQuantity <= stocks;
    }

    public boolean canGetAdditional(int purchaseQuantity){
        return purchaseQuantity % promotion.getAvailableQuantityPerPromotion() == promotion.getBuy();
    }

    public int getGiveaway(){
        return promotion.getGet();
    }

    public int quantityCannotGetPromotion(int purchaseQuantity){
        return (purchaseQuantity - stocks) + (stocks % promotion.getAvailableQuantityPerPromotion());
    }

    public int purchase(int purchaseQuantity){
        stocks -= purchaseQuantity;
        return purchaseQuantity;
    }
}
