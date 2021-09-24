package com.endava.movieApiServicesChallenge.Repository;

import com.endava.movieApiServicesChallenge.Model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface MovieRepository extends MongoRepository<Movie, Integer> {

     Page<Movie> findAll(Pageable pageable);

     @Query(value= "{ genres : { $all :?0 } }")
     Page<Movie> findByGenre(String[] genres, Pageable pageable);

     @Query(value= "{OriginalTitle : {$regex : ?0 }}")
     Page<Movie> findMovieByOriginalTitle(String name, Pageable pageable);

     Page<Movie> findMovieByAdult(boolean adult, Pageable pageable);

     @Query("{ '$and' :[{'genres' : { '$all' :?0 }} , {'originalTitle' : ?1 }, {'adult': ?2}] }")
     Page<Movie> findByGenreAndAdultAndOriginalLanguage(String[] genres,String name, boolean adult, Pageable pageable);
}
