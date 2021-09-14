package com.endava.movieApiServicesChallenge.Service;

import com.endava.movieApiServicesChallenge.Properties.Config;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;

@Slf4j
public class MovieSaverStringFromCSV implements Runnable{
    boolean reading=true;

    @Override
    public void run() {

    try ( CSVReader reader= new CSVReaderBuilder(new FileReader(Config.MOVIE_FILE_PATH)).withSkipLines(1).build()){
        while (reading) {
            synchronized (MovieService.csvData) {
                while (!MovieService.csvData.isEmpty())
                    MovieService.csvData.wait(700);
                String[] nextLine;
                int cont=0;
                while ((nextLine = reader.readNext()) != null && cont<Config.MAX_LINES_FROM_CSV) {
                    MovieService.csvData.add(nextLine);
                    cont++;
                }
                if(reader.readNext() == null){
                    reading =false;
                    MovieService.csvReading=false;
                }
                log.info("Size {}", MovieService.csvData.size());
                MovieService.csvData.notify();
            }
        }
    } catch (Exception e) {
        log.info("Error loading movie data from csv file , Exception: {}", e.toString());
    }



    }
}
