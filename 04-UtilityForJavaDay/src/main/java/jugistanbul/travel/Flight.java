package jugistanbul.travel;

import java.time.LocalDate;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 13.02.2020
 **/

public class Flight {

    private LocalDate date;
    private Long price;

    public Flight(LocalDate date, Long price) {
        this.date = date;
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
