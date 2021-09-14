package com.endava.movieApiServicesChallenge.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Document(collection = "Ratings")
public class Rating {

    private int userId;
    private int movieId;
    private float rating;
    private Timestamp timestamp;

}
