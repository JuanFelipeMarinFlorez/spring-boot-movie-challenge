package com.endava.movieApiServicesChallenge.Repository;
import com.endava.movieApiServicesChallenge.Model.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends MongoRepository<Rating,String> {


    List<Rating> findAllByMovieId(int id);
}
