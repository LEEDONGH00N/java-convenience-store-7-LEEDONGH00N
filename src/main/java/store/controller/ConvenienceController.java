package store.controller;

import store.model.products.NormalProduct;
import store.model.products.ProductStock;
import store.model.products.PromotionProduct;
import store.model.promotions.Promotion;
import store.model.receipt.Purchase;
import store.model.receipt.Receipt;
import store.utils.MembershipUtils;
import store.utils.files.ProductsFileReader;
import store.utils.files.PromotionsFileReaders;
import store.views.InputView;
import store.views.OutputView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class ConvenienceController {


    private final List<Promotion> promotions;
    private final List<ProductStock> productStocks;
    private Receipt receipt;

    private ConvenienceController() throws IOException {
        this.promotions = PromotionsFileReaders.loadPromotions();
        this.productStocks = ProductsFileReader.loadProducts(promotions);
    }

    public void start(){
        boolean getExtraOrders = true;
        while(getExtraOrders){
            try{
                welcome();
                printCurrentStock();
                askPurchase();
                askDiscount();
                printReceipt();
                getExtraOrders = InputView.inputExtraOrders();
            }catch (IllegalArgumentException e){
                OutputView.outputErrorMessage(e.getMessage());
            }
        }
    }

    private void welcome(){
        OutputView.outputWelcomeMessage();
    }

    private void printCurrentStock(){
        OutputView.outputCurrentStock(productStocks);
    }

    private void askPurchase(){
        try{
            String purchase = InputView.inputPurchaseProduct();
            List<String> purchases = Arrays.stream(purchase.split(",")).toList();
            extractOrders(purchases);
        }catch (IllegalArgumentException e){
            OutputView.outputErrorMessage(e.getMessage());
        }
    }

    private void printReceipt(){
        OutputView.outputReceipt(receipt);
    }

    private void askDiscount(){
        if(InputView.inputMembershipDiscount()){
            receipt.setMembershipDiscountAmount(
                    MembershipUtils.discount(receipt.getTotalPriceWithoutPromotions()));
        }
    }


    private void extractOrders(List<String> purchases) {
        receipt = new Receipt();
        for(String order : purchases){
            order = order.replace("[", "").replace("]", "");
            List<String> orders = Arrays.stream(order.split("-")).toList();
            String name = orders.getFirst();
            ProductStock productStock = findProduct(name);
            int orderQuantity = Integer.parseInt(orders.getLast());
            int promotionPurchaseCount = purchaseFromPromotion(productStock, orderQuantity);
            orderQuantity = hasOrderQuantityIncreased(orderQuantity, promotionPurchaseCount);
            int normalPurchaseCount = orderQuantity - promotionPurchaseCount;
            purchaseFromNormal(productStock, normalPurchaseCount);
            Purchase purchase = new Purchase(
                    productStock.getProduct(),
                    promotionPurchaseCount + normalPurchaseCount,
                    findPromotionCount(promotionPurchaseCount, productStock),
                    findPromotionQuantityPerPromotion(productStock));
            receipt.addPurchase(purchase);
            receipt.adjust();
        }
    }

    private static int hasOrderQuantityIncreased(int orderQuantity, int promotionPurchaseCount) {
        if(orderQuantity < promotionPurchaseCount){
            orderQuantity = promotionPurchaseCount;
        }
        return orderQuantity;
    }

    private int findPromotionCount(int promotionPurchaseCount, ProductStock productStock){
        if(!productStock.hasPromotionStock()){
            return 0;
        }
        return promotionPurchaseCount / productStock.getPromotionProduct().getPromotion().getAvailableQuantityPerPromotion();
    }

    private int findPromotionQuantityPerPromotion(ProductStock productStock){
        if(!productStock.hasPromotionStock()){
            return 0;
        }
        return productStock.getPromotionProduct().getPromotion().getAvailableQuantityPerPromotion();
    }

    private int purchaseFromPromotion(ProductStock productStock, int orderQuantity){

        if(!productStock.hasPromotionStock()){
            return 0;
        }
        PromotionProduct promotionProduct = productStock.getPromotionProduct();
        if (!promotionProduct.getPromotion().compareDate(LocalDateTime.now())){  // 프로모션 기간인가?
            return 0;
        }

        // 주문한 수량만큼 주문이 가능한가?
        if(promotionProduct.canPurchase(orderQuantity)){
            // 증정품을 추가로 받을 수 있는 수량인가??
            if(promotionProduct.canGetAdditional(orderQuantity)){
                boolean willGetAdditional = InputView.inputCanGetAdditional(productStock.getProduct().getName(), promotionProduct.getGiveaway());
                if(willGetAdditional){
                    if(!promotionProduct.canPurchase(orderQuantity + promotionProduct.getGiveaway())){
                        OutputView.outputPromotionCannotGiveGiveaway();
                    }
                    orderQuantity += promotionProduct.getGiveaway();
                    promotionProduct.purchase(orderQuantity);
                    return orderQuantity;
                }
            }

            //그대로 결제 가능한 수량인 경우
            promotionProduct.purchase(orderQuantity);
            return orderQuantity;
        }

        int noPromotionBenefitQuantity = promotionProduct.quantityCannotGetPromotion(orderQuantity);
        boolean agree = InputView.inputExistingNoPromotionBenefit(productStock.getProduct().getName(), noPromotionBenefitQuantity);
        if(agree){
            orderQuantity -= noPromotionBenefitQuantity;
            promotionProduct.purchase(orderQuantity);
            return orderQuantity;
        }
        promotionProduct.purchase(orderQuantity);
        return orderQuantity;
    }

    private void purchaseFromNormal(ProductStock productStock, int orderQuantity){
        if(orderQuantity == 0){
            return;
        }
        NormalProduct normalProduct = productStock.getNormalProduct();
        if (!normalProduct.canPurchase(orderQuantity)){
            throw new IllegalArgumentException("재고가 충분하지 않습니다. 수량을 다시 입력해주세요");
        }
        normalProduct.purchase(orderQuantity);
    }

    private ProductStock findProduct(String name){
        return productStocks.stream()
                .filter(productStock -> productStock.getProduct().getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

    }

    public static ConvenienceController loadFiles() throws IOException {
        return new ConvenienceController();
    }
}
