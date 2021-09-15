package com.endava.movieApiServicesChallenge.Controller;


import com.endava.movieApiServicesChallenge.Service.RatingServices.RatingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Data
@RestController
public class RatingController {


    RatingService ratingService;

    @Autowired
    public RatingController( RatingService ratingService) {
        this.ratingService = ratingService;
    }


    @ApiOperation(value = "Load all the data from a .csv into the mongoDataBase", response = Resource.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully loaded the data"),
            @ApiResponse(code = 400, message = "data could not be loaded"),
    })
    @PostMapping("/load/ratings")
    public ResponseEntity<String> loadRatings(){
        try{
            int ratingDataLoaded= this.ratingService.loadRatingInDataBase();
            return new ResponseEntity<String>("Loaded ratings in the data base: "
                    +ratingDataLoaded, HttpStatus.CREATED);
        }catch(Exception e){
            return  new ResponseEntity<String>("Could not load the ratings in the database"+e.toString(),
                    HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/movie/{id}/rating")
    public ResponseEntity<String>findMovieRating(@PathVariable("id") int id){
        try{
            HttpStatus httpStatus=null;
            Float rating= ratingService.findMovieRating(id);
            if(rating==-1){
                httpStatus= HttpStatus.NO_CONTENT;
            }
            else{
                httpStatus=HttpStatus.OK;
            }
            return new ResponseEntity<String>("rating of the movie is : "+rating,httpStatus);
        }catch(Exception e){
            return  new ResponseEntity<String>("Could not find the ratings of this movie from database"+e.toString(),
                    HttpStatus.NOT_FOUND);
        }

    }
}
