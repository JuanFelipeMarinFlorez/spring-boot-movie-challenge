package com.endava.movieApiServicesChallenge.Service;

import com.endava.movieApiServicesChallenge.Model.Movie;
import com.endava.movieApiServicesChallenge.Repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataBaseThread implements Runnable{

    List<Movie> movieList;
    MovieRepository movieRepository;

    public DataBaseThread(List<Movie> movieList, MovieRepository movieRepository) {
        this.movieList = movieList;
        this.movieRepository = movieRepository;
    }


    public void run(){
        System.out.println("hola perros"+movieList.size());
        movieRepository.saveAll(movieList);
    }
}
