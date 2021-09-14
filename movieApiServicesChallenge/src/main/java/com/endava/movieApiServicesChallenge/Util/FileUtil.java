package com.endava.movieApiServicesChallenge.Util;

import com.endava.movieApiServicesChallenge.Properties.Config;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
public class FileUtil {

    private static final CsvMapper mapper = new CsvMapper();

    public static List<String[]> loadFromCsv(String filePath){
        try( CSVReader reader= new CSVReaderBuilder(new FileReader(filePath)).withSkipLines(1).build())
        {
            log.info("the number is: "+reader.getMultilineLimit());
            return reader.readAll();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }




}
