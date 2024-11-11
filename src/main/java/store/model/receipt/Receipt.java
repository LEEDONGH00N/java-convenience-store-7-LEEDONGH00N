package store.model.receipt;

import java.util.ArrayList;
import java.util.List;

public class Receipt {

    private int totalPrice; // 할인을 안한 총 금액
    private int totalPriceWithoutPromotions;
    private int membershipDiscountAmount;
    private int promotionDiscountAmount;
    private int paymentAmount;
    private int totalCount;
    private List<Purchase> purchases;

    public Receipt() {
        this.purchases = new ArrayList<>();
        this.totalPrice = 0;
        this.membershipDiscountAmount = 0;
        this.promotionDiscountAmount = 0;
        this.paymentAmount = 0;
        this.totalPriceWithoutPromotions = 0;
        this.totalCount = 0;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public int getPromotionDiscountAmount() {
        return promotionDiscountAmount;
    }

    public int getMembershipDiscountAmount() {
        return membershipDiscountAmount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getTotalPriceWithoutPromotions() {
        return totalPriceWithoutPromotions;
    }

    public void setMembershipDiscountAmount(int membershipDiscountAmount) {
        this.membershipDiscountAmount = membershipDiscountAmount;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void addPurchase(Purchase purchase) {
        purchases.add(purchase);
    }
    public void adjust(){
        adjustTotalPrice();
        adjustTotalPriceWithoutPromotions();
        adjustPromotionDiscountAmount();
        adjustTotalCount();
        adjustTotalPayment();
    }

    private void adjustTotalPrice(){
        totalPrice = purchases.stream()
                .mapToInt(Purchase::getTotalAmount)
                .sum();
    }

    private void adjustTotalPriceWithoutPromotions() {
        totalPriceWithoutPromotions = purchases.stream()
                .mapToInt(Purchase::getPriceWithoutPromotion)
                .sum();
    }

    private void adjustPromotionDiscountAmount(){
        promotionDiscountAmount = purchases.stream()
                .mapToInt(Purchase::getPromotionDiscounted)
                .sum();
    }

    private void adjustTotalCount() {
        totalCount = purchases.stream()
                .mapToInt(Purchase::getTotalQuantity)
                .sum();
    }

    private void adjustTotalPayment(){
        paymentAmount = totalPrice - membershipDiscountAmount - promotionDiscountAmount;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "totalPrice=" + totalPrice +
                ", totalPriceWithoutPromotions=" + totalPriceWithoutPromotions +
                ", membershipDiscountAmount=" + membershipDiscountAmount +
                ", promotionDiscountAmount=" + promotionDiscountAmount +
                ", paymentAmount=" + paymentAmount +
                ", purchases=" + purchases +
                '}';
    }
}