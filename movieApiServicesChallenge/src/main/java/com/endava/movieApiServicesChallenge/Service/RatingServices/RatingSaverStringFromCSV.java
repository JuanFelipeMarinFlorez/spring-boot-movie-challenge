package com.endava.movieApiServicesChallenge.Service.RatingServices;

import com.endava.movieApiServicesChallenge.Properties.Config;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;

@Slf4j
public class RatingSaverStringFromCSV implements Runnable{
    boolean reading=true;

    @Override
    public void run() {
        try ( CSVReader reader= new CSVReaderBuilder(new FileReader(Config.RATING_FILE_PATH)).withSkipLines(1).build()){
            while (reading) {
                synchronized (RatingService.csvData) {
                    while (!RatingService.csvData.isEmpty())
                        RatingService.csvData.wait(700);
                    String[] nextLine;
                    int cont=0;
                    while ((nextLine = reader.readNext()) != null && cont<Config.MAX_LINES_FROM_CSV) {
                        RatingService.csvData.add(nextLine);
                        cont++;
                    }
                    if(reader.readNext() == null){
                        reading =false;
                        RatingService.csvReading=false;
                    }
                    RatingService.csvData.notify();
                }
            }
        } catch (Exception e) {
            log.error("Error loading rating data from csv file , Exception: {}", e.getMessage());
        }
    }
}
