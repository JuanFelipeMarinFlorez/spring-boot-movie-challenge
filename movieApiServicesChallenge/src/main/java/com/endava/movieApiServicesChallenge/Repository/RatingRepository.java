package com.endava.movieApiServicesChallenge.Repository;

import com.endava.movieApiServicesChallenge.Model.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends MongoRepository<Rating,String> {
}
