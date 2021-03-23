package com.navneet.executor.utils;

import com.navneet.executor.models.CustomerInfo;
import com.univocity.parsers.common.IterableResult;
import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvRoutines;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * @author navneetprabhakar
 */
@Service
@Log4j2
public class FileUtils {

    /**
     * This method reads data from csv file and output the Iterable collection
     * Iterable is used to optimise memory usage
     * @param filePath
     * @param className
     * @param <T>
     * @return
     */
    public <T> IterableResult<T, ParsingContext> readDataFromCsv(String filePath, Class<T> className) throws IOException {
        InputStreamReader stream =null;
        try {
            CsvParserSettings settings = new CsvParserSettings();
            settings.getFormat().setLineSeparator(System.lineSeparator());
            stream = new InputStreamReader(new FileInputStream(filePath));
            return new CsvRoutines(settings).iterate(className, stream);
        }catch(Exception e){
            log.error("An error occurred while reading the data from csv file:",e);
        }
        return null;
    }

    /**
     * This method is used to count the number of lines in the file
     * @param path
     * @return
     * @throws IOException
     */
    public int numberOfLines(String path) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(path));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }
}
