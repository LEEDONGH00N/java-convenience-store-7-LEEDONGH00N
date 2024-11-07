package store.controller;


import store.model.products.ProductStock;
import store.utils.files.FileReaders;
import store.views.OutputView;

import java.io.IOException;
import java.util.List;

public class ConvenienceController {

    private final List<ProductStock> productStocks;

    public ConvenienceController() throws IOException {
        this.productStocks = FileReaders.loadProducts();
    }

    public void printStock(){
        OutputView.outputWelcomeMessage();
        OutputView.outputCurrentStock(productStocks);
    }
}
