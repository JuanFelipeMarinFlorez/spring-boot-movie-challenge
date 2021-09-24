package com.endava.movieApiServicesChallenge.Service.MovieServices;

import com.endava.movieApiServicesChallenge.Model.Movie;
import com.endava.movieApiServicesChallenge.Model.Rating;
import com.endava.movieApiServicesChallenge.Repository.MovieRepository;
import com.endava.movieApiServicesChallenge.Repository.RatingRepository;
import com.endava.movieApiServicesChallenge.Service.RatingServices.RatingService;
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
public class MovieSaverMovieFromString implements Callable<Integer> {
    boolean reading=true;
    MovieRepository movieRepository;

    @Autowired
    public MovieSaverMovieFromString(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Integer call() {
        int dataLoaded=0;
        while (reading) {
            synchronized (MovieService.csvData) {
                try {
                    while (MovieService.csvData.isEmpty()){
                        Thread.sleep(700);
                        MovieService.csvData.wait();
                    }

                    while ( !MovieService.csvData.isEmpty()) {
                        Movie movie = new Movie(new ArrayList<String>());
                        String[] dataLine = MovieService.csvData.get(0);
                        try{
                            movie.setAdult(Boolean.getBoolean(dataLine[0]));
                            movie.setId(Integer.parseInt(dataLine[5]));
                            movie.setOriginalTitle(dataLine[8]);
                            movie.setOverView(dataLine[9]);
                            movie.setOriginalLanguage(dataLine[7]);
                            movie.setGenresFromString(dataLine[3]);
                            movie.setYear(Integer.parseInt(dataLine[14].split("-")[0]));
                            movie.setPopularity(Float.parseFloat(dataLine[10]));
                            movie.setRuntime(Float.parseFloat(dataLine[16]));
                            MovieService.movies.add(movie);
                            MovieService.csvData.remove(0);
                            dataLoaded++;
                        }
                        catch(Exception e){
                            MovieService.csvData.remove(0);
                            log.error("Problem saving {}, with exception:{} ", dataLine, e.toString());
                        }
                    }
                    List<Movie> newRatings=new ArrayList<>();
                    newRatings.addAll(MovieService.movies);
                    ExecutorService es =Executors.newSingleThreadExecutor();
                    es.submit(() -> this.movieRepository.saveAll(newRatings));
                    MovieService.movies.clear();
                    MovieService.csvData.notify();
                    if(!MovieService.csvReading){
                        reading=false;
                        return dataLoaded;
                    }
                } catch (Exception e) {
                    log.error("Error loading movie data to movie list , Exception: {}", e.toString());
                }
            }
        }
        return 0;
    }
}
