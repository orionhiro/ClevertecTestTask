package main.java.ru.clevertec.check.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CSVReader {
    public static List<List<String>> readFile(String filename) throws IOException, FileNotFoundException {
        List<List<String>> records = new ArrayList<>();

        try(Scanner scanner = new Scanner(new File(filename))){
            while(scanner.hasNext()){
                records.add(getRecordFromLine(scanner.nextLine()));
            }
        } catch (FileNotFoundException exc){
            throw exc;
        }

        return records;
    }

    private static List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<>();
        try(Scanner rowScanner = new Scanner(line)){
            rowScanner.useDelimiter(";");
            while(rowScanner.hasNext()){
                values.add(rowScanner.next());
            }
        }

        return values;
    }
}
