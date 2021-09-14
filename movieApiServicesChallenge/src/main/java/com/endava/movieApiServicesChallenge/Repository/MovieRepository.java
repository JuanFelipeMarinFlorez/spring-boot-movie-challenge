package com.endava.movieApiServicesChallenge.Repository;

import com.endava.movieApiServicesChallenge.Model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends MongoRepository<Movie, Integer> {

     Page<Movie> findAll(Pageable pageable);

     @Query(value= "{ genres : ?0 }")
     Page<Movie> findByGenre(String genres, Pageable pageable);
}
