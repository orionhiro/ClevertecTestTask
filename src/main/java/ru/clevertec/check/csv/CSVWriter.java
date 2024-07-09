package main.java.ru.clevertec.check.csv;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter {

    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;

    public CSVWriter(String filename) throws IOException {
        this.fileWriter = new FileWriter(filename);
        this.bufferedWriter = new BufferedWriter(fileWriter);
    }

    public void write(String line) throws IOException{
        this.bufferedWriter.write(line);
    }

    public void close() throws IOException{
        this.bufferedWriter.close();
        this.fileWriter.close();
    }
}
