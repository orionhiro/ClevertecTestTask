package main.java.ru.clevertec.check;

import main.java.ru.clevertec.check.card.DiscountCard;
import main.java.ru.clevertec.check.card.DebitCard;
import main.java.ru.clevertec.check.csv.CSVWriter;
import main.java.ru.clevertec.check.product.Product;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class CheckWriter {
    public static void writeCheck(
            String filename,
            Map<Product, Integer> shoppingList,
            DiscountCard discountCard,
            DebitCard debitCard){

        float total = 0;
        float totalDiscount = 0;

        CSVWriter csvWriter;
        String checkBody = "";

        try{
            csvWriter = new CSVWriter(filename);
        } catch (IOException exc){
            System.out.println("Error while writing file...");
            return;
        }

        try{

            // write date-time

            checkBody += "Date;Time\n";
            System.out.println("Date;Time");

            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            String time = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss"));

            System.out.printf("%s;%s\n", date, time);
            checkBody += String.format("%s;%s\n\n", date, time);

            // writing check body
            System.out.println("\nQTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL");
            checkBody += "QTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL\n";

            for (Map.Entry<Product, Integer> entry: shoppingList.entrySet()){
                Product product = entry.getKey();
                int count = entry.getValue();
                double discount = 0;

                System.out.printf("%d;%s;%.2f$;", count, product.getDescription(), product.getPrice());
                checkBody += String.format("%d;%s;%.2f$;", count, product.getDescription(), product.getPrice());

                if (shoppingList.get(product) >= 5 && product.isWholesale()){
                    discount = round(0.1d * product.getPrice()*count, 2);
                } else{
                    discount = round((double) discountCard.getDiscount() * product.getPrice()*count, 2);
                }

                System.out.printf("%.2f$;%.2f$;\n", discount, product.getPrice()*count);
                checkBody += String.format("%.2f$;%.2f$;\n", discount, product.getPrice()*count);

                totalDiscount += discount;
                total += product.getPrice()*count;

            }

            // check sum
            if (debitCard.getValue() < total){
                System.out.println("ERROR. NOT ENOUGH MONEY");
                csvWriter.write("NOT ENOUGH MONEY");
                try{
                    csvWriter.close();
                } catch (IOException exc){
                    System.out.println("Error while closing file...");
                }
                return;
            }

            // write check body
            csvWriter.write(checkBody);


            // write card data
            System.out.println("\nDISCOUNT CARD;DISCOUNT PERCENTAGE");
            csvWriter.write("\nDISCOUNT CARD;DISCOUNT PERCENTAGE\n");
            System.out.printf("%d;%.0f%%", discountCard.getNumber(), discountCard.getDiscount() * 100);
            csvWriter.write(String.format("%d;%.0f%%\n", discountCard.getNumber(), discountCard.getDiscount() * 100));

            // write price data
            System.out.println("\nTOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT");
            csvWriter.write("\nTOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT\n");
            System.out.printf("%.2f$;%.2f$;%.2f$", total, totalDiscount, (total - totalDiscount));
            csvWriter.write(String.format("%.2f$;%.2f$;%.2f$", total, totalDiscount, (total - totalDiscount)));

            try{
                csvWriter.close();
            } catch (IOException exc){
                System.out.println("Error while closing file...");
            }

        } catch (IOException exc){
            System.out.println("Error while writing file...");
            return;
        }

    }

    public static void writeCheck(String filename, String line){
        CSVWriter csvWriter = null;

        try{
            csvWriter = new CSVWriter(filename);
            csvWriter.write(line);
        } catch (IOException exc){
            System.out.println("Error while writing file...");
            return;
        } finally {
            try{
                csvWriter.close();
            } catch (IOException exc){
                System.out.println("Error while closing file...");
            }
        }


    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_EVEN);
        return bd.doubleValue();
    }
}
