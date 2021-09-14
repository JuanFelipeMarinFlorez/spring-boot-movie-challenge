package com.endava.movieApiServicesChallenge.Service;

import com.endava.movieApiServicesChallenge.Model.Movie;
import com.endava.movieApiServicesChallenge.Repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
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
                        movie.setId(Integer.parseInt(dataLine[5]));
                        movie.setOriginalTitle(dataLine[8]);
                        movie.setOverView(dataLine[9]);
                        movie.setOriginalLanguage(dataLine[7]);
                        movie.setGenresFromString(dataLine[3]);
                        MovieService.movies.add(movie);
                        MovieService.csvData.remove(0);
                        dataLoaded++;
                        }
                        catch(Exception e){
                            MovieService.csvData.remove(0);
                            log.info("Problem saving {}, with exception:{} ", dataLine, e.toString());
                        }
                    }

                    log.info("Size removed {}", MovieService.movies.size());
                    List<Movie> newMovies=new ArrayList<>();
                    newMovies.addAll(MovieService.movies);
                    ExecutorService es = Executors.newSingleThreadExecutor();
                    es.submit(new Thread(new DataBaseThread(newMovies,this.movieRepository)));
                    es.shutdown();
                    MovieService.movies.clear();
                    MovieService.csvData.notify();

                    log.info("Size removed cleared{}", MovieService.movies.size());

                    if(!MovieService.csvReading){
                        reading=false;
                        return dataLoaded;
                    }
                } catch (Exception e) {
                    log.info("Error loading movie data to movie list , Exception: {}", e.toString());
                }
            }
        }
        return 0;
    }
}
