package store.views;

import camp.nextstep.edu.missionutils.Console;

public class InputView {

    private static final String INPUT_ASK_PURCHASE_MESSAGE = "\n구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String INPUT_ASK_MEMBERSHIP_DISCOUNT = "멤버십 할인을 받으시겠습니까? (Y/N)";
    private static final String INPUT_ASK_EXTRA_ORDERS = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";
    private static final String INPUT_ASK_EXISTING_NO_PROMOTION_BENEFIT = "현재 %s %,d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    private static final String INPUT_ASK_ADDITIONAL_GET = "현재 %s은(는) %,d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";

    public static String inputPurchaseProduct(){
        System.out.println(INPUT_ASK_PURCHASE_MESSAGE);
        return Console.readLine();
    }

    public static boolean inputCanGetAdditional(String name, int quantity){
        System.out.println(String.format(INPUT_ASK_ADDITIONAL_GET, name, quantity));
        return Console.readLine().equalsIgnoreCase("Y");
    }

    public static boolean inputMembershipDiscount(){
        System.out.println(INPUT_ASK_MEMBERSHIP_DISCOUNT);
        return Console.readLine().equalsIgnoreCase("Y");
    }

    public static boolean inputExtraOrders(){
        System.out.println(INPUT_ASK_EXTRA_ORDERS);
        return Console.readLine().equalsIgnoreCase("Y");
    }

    public static boolean inputExistingNoPromotionBenefit(String name, int quantity){
        System.out.println(String.format(INPUT_ASK_EXISTING_NO_PROMOTION_BENEFIT, name, quantity));
        return Console.readLine().equalsIgnoreCase("Y");
    }
}
