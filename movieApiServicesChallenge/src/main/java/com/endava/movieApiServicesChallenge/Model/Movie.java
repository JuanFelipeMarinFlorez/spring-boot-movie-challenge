package com.endava.movieApiServicesChallenge.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Document(collection = "Movies")
public class Movie {


    @Id
    private int id;
    private String originalTitle;
    private String overView;
    private String originalLanguage;
    private boolean adult;
    private List<String> genres;


    public Movie(List<String> genres) {
        this.genres = genres;
    }


    public void setGenresFromString(String genres){
        if(genres.length()>5){
            String[] genreArray=genres.replaceAll("[^a-zA-Z:,0-9]", "").split("[:,]");
            List<String> genreList= Arrays.stream(genreArray)
                    .filter(x-> !x.equals("id") && !x.equals("name"))
                    .collect(Collectors.toList());
            Iterator<String> iterator = genreList.iterator();
            while (iterator.hasNext()) {
                iterator.next();
                this.genres.add(iterator.next());
            }
        }
    }
}
