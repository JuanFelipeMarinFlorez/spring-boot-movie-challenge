package com.endava.movieApiServicesChallenge.Service.RatingServices;

import com.endava.movieApiServicesChallenge.Model.Rating;
import com.endava.movieApiServicesChallenge.Repository.RatingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class RatingSaverRatingFromString implements Callable<Integer> {
    boolean reading=true;
    RatingRepository ratingRepository;

    @Autowired
    public RatingSaverRatingFromString(RatingRepository movieRepository) {
        this.ratingRepository = movieRepository;
    }

    @Override
    public Integer call() {
        int dataLoaded=0;
        while (reading) {
            synchronized (RatingService.csvData) {
                try {
                    while (RatingService.csvData.isEmpty()){
                        Thread.sleep(700);
                        RatingService.csvData.wait();
                    }

                    while ( !RatingService.csvData.isEmpty()) {
                        Rating rating = new Rating();
                        String[] dataLine = RatingService.csvData.get(0);
                        try{
                            rating.setMovieId(Integer.parseInt(dataLine[1]));
                            rating.setUserId(Integer.parseInt(dataLine[0]));
                            rating.setRating(Float.parseFloat(dataLine[2]));
                            rating.setTimestamp(dataLine[3]);
                            RatingService.ratings.add(rating);
                            RatingService.csvData.remove(0);
                            dataLoaded++;
                        }
                        catch(Exception e){
                            RatingService.csvData.remove(0);
                            log.info("Problem saving {}, with exception:{} ", dataLine, e.toString());
                        }
                    }

                    List<Rating> newRatings=new ArrayList<>();
                    newRatings.addAll(RatingService.ratings);
                    ExecutorService es =Executors.newSingleThreadExecutor();
                    es.submit(() -> this.ratingRepository.saveAll(newRatings));
                    RatingService.ratings.clear();
                    RatingService.csvData.notify();

                    if(!RatingService.csvReading){
                        reading=false;
                        return dataLoaded;
                    }
                } catch (Exception e) {
                    log.info("Error loading rating data to movie list , Exception: {}", e.toString());
                }
            }
        }
        return 0;
    }
}
