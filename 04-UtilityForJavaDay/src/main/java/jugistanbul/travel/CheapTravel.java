package jugistanbul.travel;

import jugistanbul.entity.Speaker;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 13.02.2020
 **/

public class CheapTravel {

    private String speaker;
    private Weather weather;
    private Flight flight;
    private Hotel hotel;

    public CheapTravel(String speaker, Weather weather, Flight flight, Hotel hotel) {
        this.speaker = speaker;
        this.weather = weather;
        this.flight = flight;
        this.hotel = hotel;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
}
