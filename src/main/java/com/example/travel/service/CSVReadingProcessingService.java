package com.example.travel.service;


import com.example.travel.config.InputFileConfig;
import com.example.travel.dto.CSVDataRow;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service("csv")
public class CSVReadingProcessingService implements FileProcessingService<List<String>, List<CSVDataRow>>, FileReadingService<List<String>> {

    private final InputFileConfig inputFileConfig;
    private final DateTimeFormatter dateTimeFormatter;

    public CSVReadingProcessingService(InputFileConfig inputFileConfig) {
        this.inputFileConfig = inputFileConfig;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(inputFileConfig.getDateTimeFormat());
    }

    @Override
    public List<CSVDataRow> process(List<String> rows) throws IOException {
        return rows.stream().map(line -> line.split(inputFileConfig.getDelimiter()))
                .map(values -> new CSVDataRow(
                        values[0].trim(),
                        ZonedDateTime.of(LocalDateTime.parse(values[1].trim(), dateTimeFormatter), ZoneId.of("UTC")),
                        values[2].trim(),
                        values[3].trim(),
                        values[4].trim(),
                        values[5].trim(),
                        values[6].trim()
                )).collect(Collectors.toList());
    }

    @Override
    public List<String> read(String filePath) throws IOException {
        List<String> data;
        File file = new File(filePath);
        InputStream is = new FileInputStream(file);
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(is))){
            data = reader.lines().skip(1)
                    .collect(Collectors.toList());
        }
        return data;
    }
}
