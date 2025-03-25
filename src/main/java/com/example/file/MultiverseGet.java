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
       // System.out.println("MG: Loading multiverse data from CSV file: " + multiverseCsvPath);
        try (CSVReader reader = new CSVReader(new FileReader(multiverseCsvPath))) {
            String[] line;
            try {
                while ((line = reader.readNext()) != null) {
                    if (line.length >= 2) {
                        //System.out.println("MG: Processing line: " + String.join(", ", line));
                        if ("Multiverse Name".equals(line[0])) {
                            multiverseName = line[1];
                         //   System.out.println("MG: Multiverse Name found: " + multiverseName);
                        } else if ("Dating System".equals(line[0])) {
                            datingSystem = line[1];
                           // System.out.println("MG: Dating System found: " + datingSystem);
                        } else if ("Before Era".equals(line[0]) ||
                                "During Era".equals(line[0]) ||
                                "After Era".equals(line[0])) {
                            eras.add(line[1]);
                           // System.out.println("MG: Era added: " + line[1]);
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
       // System.out.println("MG: Getting multiverse name: " + multiverseName);
        return multiverseName;
    }

    public String getDatingSystem() {
       // System.out.println("MG: Getting dating system: " + datingSystem);
        return datingSystem;
    }

    public List<String> getEras() {
      //  System.out.println("MG: Getting eras: " + eras);
        return eras;
    }

    public boolean isRelativeDating() {
      //  System.out.println("MG: Checking if dating system is Relative: " + "Relative".equals(datingSystem));
        return "Relative".equals(datingSystem);
    }

    public boolean isNumeralDating() {
       // System.out.println("MG: Checking if dating system is Numeral: " + "Numeral".equals(datingSystem));
        return "Numeral".equals(datingSystem);
    }
}