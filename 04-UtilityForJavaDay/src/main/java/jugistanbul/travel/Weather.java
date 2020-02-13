package jugistanbul.travel;

import java.time.LocalDate;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 13.02.2020
 **/

public class Weather {

    private LocalDate date;
    private boolean sunny;

    public Weather(LocalDate date, boolean sunny) {
        this.date = date;
        this.sunny = sunny;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isSunny() {
        return sunny;
    }

    public void setSunny(boolean sunny) {
        this.sunny = sunny;
    }
}
