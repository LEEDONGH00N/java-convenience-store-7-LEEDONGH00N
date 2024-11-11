package store.utils;

import store.properties.MembershipProperties;

public class MembershipUtils {

    public static int discount(int amountWithoutPromotion){
        int discountAmount = amountWithoutPromotion * (MembershipProperties.MEMBERSHIP_DISCOUNT_PERCENTAGE) / 100;
        return validateDiscountAmountMax(discountAmount);
    }

    private static int validateDiscountAmountMax(int discountAmount){
        if(discountAmount >= MembershipProperties.MEMBERSHIP_DISCOUNT_MAXIMUM_AMOUNT){
            return MembershipProperties.MEMBERSHIP_DISCOUNT_MAXIMUM_AMOUNT;
        }
        return discountAmount;
    }
}
