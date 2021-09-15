package com.endava.movieApiServicesChallenge.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor   
public class MovieConsult {

    private int page;
    private boolean  adult;
    private String genres;
    private String title;
    private int limit;

    public boolean allMovies(){
        if( this.genres==null  && this.title==null){
            return true;
        }
        return false;
    }

}
