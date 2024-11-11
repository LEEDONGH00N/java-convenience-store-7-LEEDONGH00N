package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import store.exception.ConvenienceExceptionStatus;
import store.model.products.NormalProduct;
import store.model.products.ProductStock;
import store.model.products.PromotionProduct;
import store.model.promotions.Promotion;
import store.model.receipt.Purchase;
import store.model.receipt.Receipt;
import store.utils.InputValidator;
import store.utils.MembershipUtils;
import store.utils.files.ProductsFileReader;
import store.utils.files.PromotionsFileReaders;
import store.views.InputView;
import store.views.OutputView;

import java.io.IOException;
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
        while(true){
            receipt = new Receipt();
            try{
                String purchase = InputValidator.validateInput(InputView.inputPurchaseProduct());
                extractOrders(Arrays.stream(purchase.split(",")).toList());
                return;
            }catch (IllegalArgumentException e){
                OutputView.outputErrorMessage(e.getMessage());
            }
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
        purchases.forEach(this::processSingleOrder);
    }

    private void processSingleOrder(String order){
        List<String> orders = getOrders(order);
        ProductStock productStock = findProduct(orders.getFirst().trim());
        int orderQuantity = Integer.parseInt(orders.getLast().trim());
        validateStock(productStock, orderQuantity);
        int promotionPurchaseCount = purchaseFromPromotion(productStock, orderQuantity);
        orderQuantity = hasOrderQuantityIncreased(orderQuantity, promotionPurchaseCount);
        int normalPurchaseCount = orderQuantity - promotionPurchaseCount;
        purchaseFromNormal(productStock, orderQuantity - promotionPurchaseCount);
        Purchase purchase = new Purchase(
                productStock.getProduct(),
                promotionPurchaseCount + normalPurchaseCount,
                findPromotionCount(promotionPurchaseCount, productStock),
                findPromotionQuantityPerPromotion(productStock));
        handleReceipt(purchase);
    }

    private void handleReceipt(Purchase purchase) {
        receipt.addPurchase(purchase);
        receipt.adjust();
    }

    private void validateStock(ProductStock productStock, int orderQuantity) {
        if(!isStockEnough(productStock, orderQuantity)){
            throw new IllegalArgumentException(ConvenienceExceptionStatus.STOCK_OUT_OF_RANGE.getMessage());
        }
    }

    private List<String> getOrders(String order) {
        order = order.replace("[", "").replace("]", "");
        return Arrays.stream(order.split("-")).toList();
    }

    private boolean isStockEnough(ProductStock productStock, int orderQuantity) {
        int totalStock = productStock.getNormalProduct().getStocks();
        if(productStock.hasPromotionStock()){
            return totalStock + productStock.getPromotionProduct().getStocks() >= orderQuantity;
        }
        return totalStock >= orderQuantity;
    }

    private int hasOrderQuantityIncreased(int orderQuantity, int promotionPurchaseCount) {
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

    private int purchaseFromPromotion(ProductStock productStock, int orderQuantity) {
        if (!productStock.hasPromotionStock() || !isPromotionActive(productStock)) {
            return 0;
        }

        PromotionProduct promotionProduct = productStock.getPromotionProduct();

        if (promotionProduct.canPurchase(orderQuantity)) {
            return handlePromotionPurchase(productStock, promotionProduct, orderQuantity);
        }

        return confirmAndPurchaseWithoutPromotion(productStock, orderQuantity, promotionProduct);
    }

    private boolean isPromotionActive(ProductStock productStock) {
        return productStock.getPromotionProduct().getPromotion().compareDate(DateTimes.now());
    }

    private int handlePromotionPurchase(ProductStock productStock, PromotionProduct promotionProduct, int orderQuantity) {
        if (promotionProduct.canGetAdditional(orderQuantity) && willGetAdditional(productStock, promotionProduct)) {
            return purchaseWithGiveaway(promotionProduct, orderQuantity);
        }
        promotionProduct.purchase(orderQuantity);
        return orderQuantity;
    }

    private int purchaseWithGiveaway(PromotionProduct promotionProduct, int orderQuantity) {
        isPromotionProductStockEnoughWithAdditional(orderQuantity, promotionProduct);
        orderQuantity += promotionProduct.getGiveaway();
        promotionProduct.purchase(orderQuantity);
        return orderQuantity;
    }

    private int confirmAndPurchaseWithoutPromotion(ProductStock productStock, int orderQuantity, PromotionProduct promotionProduct) {
        int noPromotionBenefitQuantity = promotionProduct.quantityCannotGetPromotion(orderQuantity);
        boolean agree = InputView.inputExistingNoPromotionBenefit(productStock.getProduct().getName(), noPromotionBenefitQuantity);
        if(agree){
            int purchased = promotionProduct.purchase(productStock.getPromotionProduct().getStocks());
            orderQuantity -= purchased;
            return orderQuantity;
        }
        promotionProduct.purchase(orderQuantity);
        return orderQuantity;
    }

    private boolean willGetAdditional(ProductStock productStock, PromotionProduct promotionProduct) {
        return InputView.inputCanGetAdditional(productStock.getProduct().getName(), promotionProduct.getGiveaway());
    }

    private void isPromotionProductStockEnoughWithAdditional(int orderQuantity, PromotionProduct promotionProduct) {
        if(!promotionProduct.canPurchase(orderQuantity + promotionProduct.getGiveaway())){
            OutputView.outputPromotionCannotGiveGiveaway();
        }
    }

    private void purchaseFromNormal(ProductStock productStock, int orderQuantity){
        if(orderQuantity == 0){
            return;
        }
        NormalProduct normalProduct = productStock.getNormalProduct();
        normalProduct.purchase(orderQuantity);
    }

    private ProductStock findProduct(String name){
        return productStocks.stream()
                .filter(productStock -> productStock.getProduct().getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ConvenienceExceptionStatus.NO_PRODUCT_FOUND.getMessage()));

    }

    public static ConvenienceController loadFiles() throws IOException {
        return new ConvenienceController();
    }
}
