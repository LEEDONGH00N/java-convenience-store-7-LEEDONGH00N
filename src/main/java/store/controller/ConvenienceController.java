package store.controller;

import store.model.products.NormalProduct;
import store.model.products.ProductStock;
import store.model.products.PromotionProduct;
import store.model.promotions.Promotion;
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

    private ConvenienceController() throws IOException {
        this.promotions = PromotionsFileReaders.loadPromotions();
        this.productStocks = ProductsFileReader.loadProducts(promotions);
    }

    public void start(){
        while(true){
            try{
                welcome();
                askPurchase();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    private void welcome(){
        OutputView.outputWelcomeMessage();
        OutputView.outputCurrentStock(productStocks);
    }

    private void askPurchase(){
        String purchase = InputView.inputPurchaseProduct();
        List<String> purchases = Arrays.stream(purchase.split(",")).toList();
        extractOrders(purchases);
    }

    private void extractOrders(List<String> purchases) {
        for(String order : purchases){
            order = order.replace("[", "").replace("]", "");
            List<String> orders = Arrays.stream(order.split("-")).toList();
            String name = orders.getFirst();
            ProductStock productStock = findProduct(name);
            int quantity = Integer.parseInt(orders.getLast());
            int restToPurchaseNormal = purchaseFromPromotion(productStock, quantity);
            purchaseFromNormal(productStock, restToPurchaseNormal);
        }
    }

    private int purchaseFromPromotion(ProductStock productStock, int quantity){

        if(!productStock.hasPromotionStock()){
            return quantity;
        }
        PromotionProduct promotionProduct = productStock.getPromotionProduct();
        if (!promotionProduct.getPromotion().compareDate(LocalDateTime.now())){  // 프로모션 기간인가?
            return quantity;
        }

        // 주문한 수량만큼 주문이 가능한가?
        if(promotionProduct.canPurchase(quantity)){
            // 증정품을 추가로 받을 수 있는 수량인가??
            if(promotionProduct.canGetAdditional(quantity)){
                boolean willGetAdditional = InputView.inputCanGetAdditional(productStock.getProduct().getName(), promotionProduct.getGiveaway());
                if(willGetAdditional){
                    if(!promotionProduct.canPurchase(quantity + promotionProduct.getGiveaway())){
                        OutputView.outputPromotionCannotGiveGiveaway();
                    }
                    quantity += promotionProduct.getGiveaway();
                    promotionProduct.purchase(quantity);
                    return 0;
                }
            }
            //그대로 결제 가능한 수량인 경우
            promotionProduct.purchase(quantity);
            return 0;
        }

        int noPromotionBenefitQuantity = promotionProduct.quantityCannotGetPromotion(quantity);
        boolean agree = InputView.inputExistingNoPromotionBenefit(productStock.getProduct().getName(), noPromotionBenefitQuantity);
        if(agree){
            quantity -= noPromotionBenefitQuantity;
            promotionProduct.purchase(quantity);
            return noPromotionBenefitQuantity;
        }
        promotionProduct.purchase(quantity);
        return 0;
    }

    private void purchaseFromNormal(ProductStock productStock, int quantity){
        if(quantity == 0){
            return;
        }
        NormalProduct normalProduct = productStock.getNormalProduct();
        if (!normalProduct.canPurchase(quantity)){
            throw new IllegalArgumentException(productStock.getProduct().getName() + "의 재고가 충분하지 않습니다. 수량을 다시 입력해주세요");
        }
        normalProduct.purchase(quantity);
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
