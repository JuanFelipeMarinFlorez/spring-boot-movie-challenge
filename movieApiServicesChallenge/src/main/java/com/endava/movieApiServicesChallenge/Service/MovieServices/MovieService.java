package com.endava.movieApiServicesChallenge.Service.MovieServices;

import com.endava.movieApiServicesChallenge.Model.Movie;
import com.endava.movieApiServicesChallenge.Model.MovieConsult;
import com.endava.movieApiServicesChallenge.Repository.MovieRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.gson.Gson;

@Service
@Slf4j
@Data
public class MovieService {

    static List<Movie> movies = new ArrayList<>();
    static  List<String[]> csvData= new ArrayList<String[]>();
    static boolean csvReading =true;
    final
    MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }


    public int loadMovieInDataBase(){
        try {
            ExecutorService executor= Executors.newSingleThreadExecutor();
            Callable<Integer> callable=new MovieSaverMovieFromString(this.movieRepository);
            Future<Integer> future = executor.submit(callable);
            new Thread(new MovieSaverStringFromCSV()).start();
            return future.get();
        }catch(Exception e){
            e.printStackTrace();
            log.error("Error loading movie in dataBase , Exception: {}", e.getMessage());
            return 0;
        }
    }
    public String getMovieById(int id){
        Optional<Movie> movie=this.movieRepository.findById(id);
        return (new Gson().toJson(movie.get()));
    }

    public ResponseEntity<Object> getMovies(MovieConsult movieConsult)
    {
        try {
            List<Movie> movies = new ArrayList<Movie>();
            Pageable paging = PageRequest.of(movieConsult.getPage(), movieConsult.getLimit());

            Page<Movie> pageTuts = null;

            if(movieConsult.allMovies()){
                pageTuts=this.movieRepository.findAll(paging);
            }
             else if(movieConsult.isAdult()){
                pageTuts=this.movieRepository.findMovieByAdult(true,paging);
            }
            else if(movieConsult.getGenres()!=null && movieConsult.getTitle()==null){
                pageTuts=this.movieRepository.findByGenre(movieConsult.getGenres().split(","),paging);
            }
            else if(movieConsult.getTitle()!=null && movieConsult.getGenres()==null){
                pageTuts=this.movieRepository.findMovieByOriginalTitle(movieConsult.getTitle(),paging);
            }
            else if(!movieConsult.allMovies()){
                pageTuts= this.movieRepository.findByGenreAndAdultAndOriginalLanguage(
                        movieConsult.getGenres().split(","), movieConsult.getTitle(), movieConsult.isAdult(),paging);
             }
            else{
                pageTuts=this.movieRepository.findMovieByAdult(true,paging);
            }

            movies = pageTuts.getContent();
            HttpStatus status=null;
            if(movies.isEmpty()){
                status= HttpStatus.NOT_FOUND;
            }else{
                status= HttpStatus.OK;
            }

            Map<String, Object> response = new HashMap<>();
            response.put("Movies", movies);

            return new ResponseEntity<>(movies,status);
        } catch (Exception e) {
            log.error("Cannot get movies "+e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<Object> getMoviesSortedByYear(MovieConsult movieConsult)
    {
        try {
            List<Movie> movies = new ArrayList<Movie>();
            Pageable paging = PageRequest.of(movieConsult.getPage(), movieConsult.getLimit(), Sort.by("year").descending());

            Page<Movie> pageTuts = null;


            pageTuts=this.movieRepository.findAll(paging);

            movies = pageTuts.getContent();
            HttpStatus status=null;
            if(movies.isEmpty()){
                status= HttpStatus.NOT_FOUND;
            }else{
                status= HttpStatus.OK;
            }

            Map<String, Object> response = new HashMap<>();
            response.put("Movies", movies);

            return new ResponseEntity<>(movies,status);
        } catch (Exception e) {
            log.error("Cannot get movies "+e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<Object> getMoviesSortedByPopularity(MovieConsult movieConsult)
    {
        try {
            List<Movie> movies = new ArrayList<Movie>();
            Pageable paging = PageRequest.of(movieConsult.getPage(), movieConsult.getLimit(), Sort.by("popularity").descending());

            Page<Movie> pageTuts = null;

            pageTuts=this.movieRepository.findAll(paging);

            movies = pageTuts.getContent();
            HttpStatus status=null;
            if(movies.isEmpty()){
                status= HttpStatus.NOT_FOUND;
            }else{
                status= HttpStatus.OK;
            }

            Map<String, Object> response = new HashMap<>();
            response.put("Movies", movies);

            return new ResponseEntity<>(movies,status);
        } catch (Exception e) {
            log.error("Cannot get movies "+e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
