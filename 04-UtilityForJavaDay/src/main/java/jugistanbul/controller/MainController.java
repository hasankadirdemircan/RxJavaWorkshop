package jugistanbul.controller;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import jugistanbul.entity.Passengers;
import jugistanbul.entity.Speaker;
import jugistanbul.entity.SpeakerDTO;
import jugistanbul.service.DAOService;
import jugistanbul.smtp.SendEmail;
import jugistanbul.travel.CheapTravel;
import jugistanbul.travel.Flight;
import jugistanbul.travel.Hotel;
import jugistanbul.travel.Weather;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 8.02.2020
 **/

@RestController
public class MainController
{

    private final Random random = new Random();
    private final DAOService daoService;
    private final SendEmail sendEmail;
    private final ModelMapper mapper;
    private final ExecutorService pool = Executors.newFixedThreadPool(10);
    private final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    public MainController(final DAOService daoService, final SendEmail sendEmail,
                          final ModelMapper mapper){
        this.daoService = daoService;
        this.sendEmail = sendEmail;
        this.mapper = mapper;
    }

    @GetMapping(path = "/allSpeakers")
    public List<Speaker> allSpeakers(){
        return daoService.findAllSpeakers();
    }

    @PostMapping(path = "/addSpeaker")
    public Speaker addSpeaker(@RequestBody SpeakerDTO speakerDTO){
        final Speaker speaker = mapper.map(speakerDTO, Speaker.class);
        return daoService.addSpeaker(speaker);
    }

    @DeleteMapping(path = "/deleteSpeaker/{id}")
    public Integer deleteSpeaker(@PathVariable("id") Integer id){
        return daoService.deleteSpeaker(id);
    }

    @PostMapping(path = "/mail")
    public List<Speaker> sendEmailToNonApprovedSpeakers(){

        final List<Speaker> speakers = daoService.findAllSpeakers();

        return Flowable.fromIterable(speakers)
                .parallel(10)
                .runOn(Schedulers.io())
                .flatMap(speaker -> sendEmailAsync(speaker)
                        .flatMap(s -> Flowable.<Speaker>empty()
                        .doOnError(e -> logger.warn("Failed to send {}", speaker.getMail(), e)))
                        .onErrorReturn(e -> speaker)
                ).sequential().toList().blockingGet();

    }


    @GetMapping("/travel")
        public List<CheapTravel> getCheapTravel(){

            return Observable.fromIterable(daoService.findAllPassengers())
                    .flatMap(passenger -> {

                        Long flightCostLimit = passenger.getFlightCostLimit();
                        Long hotelCostLimit = passenger.getHotelCostLimit();
                        String[] certainDays = passenger.getCertainDays().split(",");
                        return Observable.fromArray(certainDays)
                                .map(LocalDate::parse)
                        .flatMap(date ->
                                Observable.zip(
                                       weatherObservable().filter(w -> w.isSunny() && w.getDate().equals(date)),
                                        flightObservable().filter(f -> f.getPrice() <= flightCostLimit
                                        && f.getDate().equals(date)),
                                        hotelObservable().filter(h -> h.getPrice() <= hotelCostLimit && h.getNight().equals(date)),
                                        (w, f , h) -> new CheapTravel(passenger.getSpeaker(), w, f, h)
                                )
                                );
                    }).toList().blockingGet();
        }

    public Flowable<String> sendEmailAsync(final Speaker speaker){
        return Flowable.fromCallable(() -> sendEmail.sendEmail(speaker));
    }

    public Observable<Hotel> hotelObservable(){
        return nextThirtyDays()
                .map(date -> new Hotel(RandomStringUtils.random(5),
                        getRandomPrice(250l, 650l), date));
    }
    public Observable<Flight> flightObservable(){
        return nextThirtyDays()
                .map(date -> new Flight(date, getRandomPrice(1400l, 2600l)));
    }
    public Observable<Weather> weatherObservable(){
        return nextThirtyDays()
                .map(date -> new Weather(date, random.nextBoolean()));
    }

    public Observable<LocalDate> nextThirtyDays(){
        return Observable.range(1, 30)
                .map(day -> LocalDate.now().plusDays(day));
    }

    public Long getRandomPrice(final Long origin, final Long bound) {
        return ThreadLocalRandom.current().nextLong(origin, bound);
    }
}
