package store.model.promotions;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Promotion {

    private final String name;
    private final int buy;
    private final int get;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(String name, int buy, int get, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public int getBuy() {
        return buy;
    }

    public int getGet() {
        return get;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean compareDate(LocalDateTime now){
        return now.isBefore(endDate.atStartOfDay()) && now.isAfter(startDate.atStartOfDay());
    }

    public int getAvailableQuantityPerPromotion(){
        return buy + get;
    }
}
