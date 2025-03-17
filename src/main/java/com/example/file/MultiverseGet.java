package com.example.file;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MultiverseGet {
    private String multiverseCsvPath;
    private String multiverseName;
    private String datingSystem;
    private List<String> eras = new ArrayList<>();

    public MultiverseGet(String multiverseCsvPath) {
        this.multiverseCsvPath = multiverseCsvPath;
        loadMultiverseData();
    }

    private void loadMultiverseData() {
        try (CSVReader reader = new CSVReader(new FileReader(multiverseCsvPath))) {
            String[] line;
            try {
                while ((line = reader.readNext()) != null) {
                    if (line.length >= 2) {
                        if ("Multiverse Name".equals(line[0])) {
                            multiverseName = line[1];
                        } else if ("Dating System".equals(line[0])) {
                            datingSystem = line[1];
                        } else if ("Before Era".equals(line[0]) ||
                                "During Era".equals(line[0]) ||
                                "After Era".equals(line[0])) {
                            eras.add(line[1]);
                        }
                    }
                }
            } catch (CsvValidationException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMultiverseName() {
        return multiverseName;
    }

    public String getDatingSystem() {
        return datingSystem;
    }

    public List<String> getEras() {
        return eras;
    }

    public boolean isRelativeDating() {
        return "Relative".equals(datingSystem);
    }

    public boolean isNumeralDating() {
        return "Numeral".equals(datingSystem);
    }
}