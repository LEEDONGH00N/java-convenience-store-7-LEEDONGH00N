package store;

import store.controller.ConvenienceController;
import store.views.OutputView;

import java.io.IOException;

public class Application {
    public static void main(String[] args) {
        try {
            final ConvenienceController controller = ConvenienceController.loadFiles();
            controller.start();
        }catch (IllegalArgumentException | IOException e){
            OutputView.outputErrorMessage(e.getMessage());
        }
    }
}
