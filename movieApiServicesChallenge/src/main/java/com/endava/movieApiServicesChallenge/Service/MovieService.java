package com.endava.movieApiServicesChallenge.Service;


import com.endava.movieApiServicesChallenge.Model.Movie;
import com.endava.movieApiServicesChallenge.Repository.MovieRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class MovieService {

    static final List<Movie> movies = new ArrayList<Movie>();
    static final List<String[]> csvData= new ArrayList<String[]>();
    static boolean csvReading =true;
    private MovieRepository movieRepository;

    @Autowired
    public MovieService( MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public int loadMovieInDataBase() {
        try {
            ExecutorService executor= Executors.newSingleThreadExecutor();
            Callable<Integer> callable=new MovieSaverMovieFromString(this.movieRepository);
            Future<Integer> future = executor.submit(callable);
            new Thread(new MovieSaverStringFromCSV()).start();
            return future.get();
        }catch(Exception e){
            log.info("Error loading movie in dataBase , Exception: {}", e.toString());
            return 0;
        }
    }

    public Optional<Movie> getMovieById(int id){
        return this.movieRepository.findById(id);
    }

    public ResponseEntity<Map<String,Object>> getMovies(int page,boolean  adult, String genres, String title, int limit)
    {
        try {
            List<Movie> movies = new ArrayList<Movie>();
            Pageable paging = PageRequest.of(page, 10);

            Page<Movie> pageTuts;

            if(genres!=null){
                pageTuts = this.movieRepository.findByGenre(genres,paging);

            }
            else{
                pageTuts = this.movieRepository.findAll(paging);
            }


            movies = pageTuts.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("Movies", movies);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalMovies", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Cannot get movies "+e.toString());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
