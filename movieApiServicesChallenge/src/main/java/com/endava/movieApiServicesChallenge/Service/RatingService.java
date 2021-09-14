package com.endava.movieApiServicesChallenge.Service;


import com.endava.movieApiServicesChallenge.Model.Movie;
import com.endava.movieApiServicesChallenge.Model.Rating;
import com.endava.movieApiServicesChallenge.Properties.Config;
import com.endava.movieApiServicesChallenge.Repository.RatingRepository;
import com.endava.movieApiServicesChallenge.Util.FileUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Data
public class RatingService {

    private List<Rating> ratings;
    RatingRepository ratingRepository;

    @Autowired
    public RatingService(List<Rating> ratings, RatingRepository ratingRepository) {
        this.ratings = ratings;

        this.ratingRepository = ratingRepository;
    }

    public int loadRatingInDataBase(){
        try {
            List<String[]> csvData = FileUtil.loadFromCsv(Config.RATING_FILE_PATH);

            ratings= csvData.stream().map(dataLine -> {
                Rating rating = new Rating();
                try {
                    //userId,movieId,rating,timestamp
                    rating.setMovieId(Integer.parseInt(dataLine[1]));
                    rating.setUserId(Integer.parseInt(dataLine[0]));
                    rating.setRating(Float.parseFloat(dataLine[2]));
                    rating.setTimestamp(Timestamp.valueOf(dataLine[3]));

                    return rating;
                } catch (Exception e) {

                    log.info("Structure invalid of this line:  {} , Exception: {}", dataLine, e.toString());
                    return null;
                }
            }).filter(rating -> rating != null).collect(Collectors.toList());
            ratings.stream().forEach(rating -> this.ratingRepository.save(rating));
            return 0;
        }catch(Exception e){
            log.info("Error loading ratings  in dataBase , Exception: {}", e.toString());
            return 0;
        }
    }

}
