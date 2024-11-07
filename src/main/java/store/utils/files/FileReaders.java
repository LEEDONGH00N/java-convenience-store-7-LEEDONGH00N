package store.utils.files;


import store.model.products.NormalStock;
import store.model.products.Product;
import store.model.products.ProductStock;
import store.model.products.PromotionStock;
import store.properties.FileProperties;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileReaders {

    public static List<ProductStock> loadProducts() throws IOException {

        try (BufferedReader reader = new BufferedReader(new FileReader(FileProperties.PRODUCT_FILE))) {

            List<ProductStock> productStocks = new ArrayList<>();
            List<String> headers = parseHeaders(reader);
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> data = parseData(line);
                ProductStock productStock = createProduct(productStocks, headers, data);
                updateProductStock(headers, data, productStock);
            }
            isNormalStockAvailable(productStocks);
            return productStocks;
        }
    }

    private static void isNormalStockAvailable(List<ProductStock> productStocks) {
        for (ProductStock productStock : productStocks){
            if(!productStock.hasNormalStock() && productStock.hasPromotionStock()){
                productStock.setNormalStock(new NormalStock(0));
            }
        }
    }

    private static List<String> parseHeaders(BufferedReader reader) throws IOException {
        return Arrays.stream(reader.readLine().split(",")).toList();
    }

    private static List<String> parseData(String line) {
        return Arrays.stream(line.split(",")).toList();
    }

    private static ProductStock createProduct(List<ProductStock> productStocks, List<String> headers, List<String> data) {
        String name = data.get(headers.indexOf("name"));
        int price = Integer.parseInt(data.get(headers.indexOf("price")));
        return findOrCreateProduct(productStocks, name, price);
    }

    private static void updateProductStock(List<String> headers, List<String> data, ProductStock productStock) {
        int quantity = Integer.parseInt(data.get(headers.indexOf("quantity")));
        String promotion = data.get(headers.indexOf("promotion"));
        isProductNormalOrNot(promotion, productStock, quantity);
    }

    private static void isProductNormalOrNot(String promotion, ProductStock productStock, int quantity) {
        if(promotion.equals("null")){
            productStock.setNormalStock(new NormalStock(quantity));
            return;
        }
        productStock.setPromotionStock(new PromotionStock(quantity, promotion));
    }

    private static ProductStock findOrCreateProduct(List<ProductStock> productStocks, String name, int price) {
        return productStocks.stream()
                .filter(p -> p.getProduct().getName().equals(name))
                .findFirst()
                .orElseGet(() -> {
                    ProductStock newProductStock = new ProductStock(new Product(name, price));
                    productStocks.add(newProductStock); // 리스트에 추가
                    return newProductStock;
                });
    }

}
