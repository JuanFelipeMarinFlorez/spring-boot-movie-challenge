package com.endava.movieApiServicesChallenge.Controller;

import com.endava.movieApiServicesChallenge.Model.MovieConsult;
import com.endava.movieApiServicesChallenge.Service.MovieServices.MovieService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Data
@Slf4j
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController( MovieService movieService) {
        this.movieService = movieService;
    }


    @ApiOperation(value = "Load all the data from a .csv into the mongoDataBase", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully loaded the data"),
            @ApiResponse(code = 400, message = "data could not be loaded"),
    })
    @PostMapping("/load/movies")
    public ResponseEntity<String> loadMovies(){
        try{

            return new ResponseEntity<String>(
                    "Loaded the movies in the data base: "+ movieService.loadMovieInDataBase(),
                    HttpStatus.CREATED);

        }catch(Exception e){
            return  new ResponseEntity<String>("Could not load the movies in the database"+e.toString(),
                    HttpStatus.BAD_REQUEST);
        }

    }

    @ApiOperation(value = "Find a movie with an id passed by parameter  ", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully found the Movie"),
            @ApiResponse(code = 204, message = "Could not find the movie"),
    })@GetMapping("/movie/{id}")
    public ResponseEntity<String> getMovieById(@PathVariable("id") int id){
    try{
        return new ResponseEntity<String>(this.movieService.getMovieById(id).get().toString(), HttpStatus.OK);

    }catch(Exception e){
        return new ResponseEntity<String>("Cannot find the movie with id:"+id,HttpStatus.NO_CONTENT);
    }
    }

    @GetMapping("/movies/")
    public ResponseEntity<Map<String, Object>> getAllMovies(
            @RequestParam() int page,
            @RequestParam(required = false, defaultValue = "false") boolean adult,
            @RequestParam(required = false) String genres,
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "100") int limit
            ){

        try{
            MovieConsult movieConsult= new MovieConsult(page,adult,genres,title,limit);
            return this.movieService.getMovies(movieConsult);
        }catch(Exception e){
            log.info("Error getting movies, with exception:"+e.toString());
            e.printStackTrace();
            return new ResponseEntity<Map<String,Object>>((Map<String, Object>) null,HttpStatus.NO_CONTENT);

        }


    }



}
