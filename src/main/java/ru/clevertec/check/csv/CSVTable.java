package main.java.ru.clevertec.check.csv;

import java.io.IOException;
import java.util.List;

import main.java.ru.clevertec.check.csv.CSVReader;

public class CSVTable {
    List<List<String>> records;

    private CSVTable(String filename) throws IOException{

        try {
            this.records = CSVReader.readFile(filename);
        } catch (IOException exc) {
            throw exc;
        }
    }

    public static CSVTable getTable(String filename) throws IOException{
        return new CSVTable(filename);
    }

    public String[] getRow(int i){
        if (i < this.records.size() && i >= 0){
            List<String> line = this.records.get(i);
            return line.toArray(new String[line.size()]);
        }

        return null;
    }

    public int getRowCount(){
        return this.records.size();
    }

    public void printTable(){
        for (int i = 0; i < this.getRowCount(); i++){
            String[] line = this.getRow(i);

            for (int j = 0; j < line.length; j++){
                System.out.print(line[j] + "\t");
            }
            System.out.println();
        }
    }


}
