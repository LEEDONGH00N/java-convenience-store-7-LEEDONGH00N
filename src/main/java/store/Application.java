package store;

import store.controller.ConvenienceController;

import java.io.IOException;

public class Application {
    public static void main(String[] args){
        // TODO: 프로그램 구현
        try {
            ConvenienceController controller = new ConvenienceController();
            controller.printStock();
        }catch (IOException e){
            e.getMessage();
        }

    }
}
