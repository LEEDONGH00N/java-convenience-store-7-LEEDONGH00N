package store.utils.files;

import store.model.promotions.Promotion;
import store.properties.FileProperties;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PromotionsFileReaders {

    public static List<Promotion> loadPromotions() throws IOException{
        try (BufferedReader reader = new BufferedReader(new FileReader(FileProperties.PROMOTION_FILE))) {
            List<Promotion> promotions = new ArrayList<>();
            List<String> headers = parseHeaders(reader);
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> data = parseData(line);
                String name = data.get(headers.indexOf("name"));
                int buy = Integer.parseInt(data.get(headers.indexOf("buy")));
                int get = Integer.parseInt(data.get(headers.indexOf("get")));
                LocalDate startDate = LocalDate.parse(data.get(headers.indexOf("start_date")));
                LocalDate endDate = LocalDate.parse(data.get(headers.indexOf("end_date")));
                promotions.add(new Promotion(name, buy, get, startDate, endDate));
            }
            return promotions;
        }
    }

    private static List<String> parseHeaders(BufferedReader reader) throws IOException {
        return Arrays.stream(reader.readLine().split(",")).toList();
    }

    private static List<String> parseData(String line) {
        return Arrays.stream(line.split(",")).toList();
    }
}
