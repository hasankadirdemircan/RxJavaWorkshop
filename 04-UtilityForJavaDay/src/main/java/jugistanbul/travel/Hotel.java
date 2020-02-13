package jugistanbul.travel;

import java.time.LocalDate;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 13.02.2020
 **/

public class Hotel {

    private String name;
    private Long price;
    private LocalDate night;

    public Hotel(String name, Long price, LocalDate night) {
        this.name = name;
        this.price = price;
        this.night = night;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public LocalDate getNight() {
        return night;
    }

    public void setNight(LocalDate night) {
        this.night = night;
    }
}
