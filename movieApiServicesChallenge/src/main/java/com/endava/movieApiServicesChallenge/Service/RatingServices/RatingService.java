package com.endava.movieApiServicesChallenge.Service.RatingServices;


import com.endava.movieApiServicesChallenge.Model.Rating;
import com.endava.movieApiServicesChallenge.Repository.RatingRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@NoArgsConstructor
@Data
@Slf4j
@Service
public class RatingService {

    static final List<Rating> ratings = new ArrayList<>();
    static final List<String[]> csvData= new ArrayList<String[]>();
    static boolean csvReading =true;
    private RatingRepository ratingRepository;

    @Autowired
    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public int loadRatingInDataBase() {

        try {
            ExecutorService executor= Executors.newSingleThreadExecutor();
            Callable<Integer> callable=new RatingSaverRatingFromString(this.ratingRepository);
            Future<Integer> future = executor.submit(callable);
            new Thread(new RatingSaverStringFromCSV()).start();
            return future.get();
        }catch(Exception e){
            log.error("Error loading rating in dataBase , Exception: {}", e.getMessage());
            return 0;
        }
    }

    public Float findMovieRating(int id) {
        try {
            List<Rating> ratingListOfMovie= ratingRepository.findAllByMovieId(id);
            Float total=ratingListOfMovie.stream().map(Rating::getRating).reduce((float) 0, Float::sum);
            return total/ratingListOfMovie.size();
        }catch(Exception e){
            log.error("Error searching id: {}, exception: {}",id,e.toString());
            return (float) -1;
        }

    }
}
