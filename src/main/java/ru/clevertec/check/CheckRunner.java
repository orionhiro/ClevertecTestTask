package main.java.ru.clevertec.check;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import main.java.ru.clevertec.check.card.DebitCard;
import main.java.ru.clevertec.check.card.DiscountCard;
import main.java.ru.clevertec.check.csv.CSVTable;
import main.java.ru.clevertec.check.csv.CSVWriter;
import main.java.ru.clevertec.check.product.Product;


public class CheckRunner {
    private static final String products_path = "./src/main/resources/products.csv";
    private static final String discount_path = "./src/main/resources/discountCards.csv";

    public static void main(String[] args) {
        CSVTable productTable;
        CSVTable cardTable;

        DiscountCard discountCard;
        DebitCard debitCard;

        Map<Product, Integer> productCounts = new HashMap<>();

        // loading card list
        System.out.print("Loading card list... ");

        try {
            cardTable = CSVTable.getTable(discount_path);
        } catch (FileNotFoundException exc) {
            System.out.println("File not found...");
            CheckWriter.writeCheck("result.csv", "INTERNAL SERVER ERROR");
            return;
        } catch (IOException exc){
            System.out.println("Unknown error...");
            CheckWriter.writeCheck("result.csv", "INTERNAL SERVER ERROR");
            return;
        }

        System.out.println("Loading successfull!");
        System.out.println("\n=============================\n");

        // print card table
        cardTable.printTable();

        System.out.println("\n=============================\n");

        // parsing args

        int debitCardValue, shift;
        String pathToFile;
        String saveToFile;

        if(
                args.length >= 5 &&
                        checkProductArgs(args, 0, args.length-5, "[0-9]+-[0-9]+") &&
                        args[args.length-2].matches("pathToFile=\\..+") &&
                        args[args.length-1].matches("saveToFile=\\..+") &&
                        args[args.length-4].matches("discountCard=[0-9]{4}") &&
                        args[args.length-3].matches("balanceDebitCard=[0-9]+")){
            discountCard = getCard(cardTable, args[args.length - 4]);
            debitCardValue = Integer.parseInt(args[args.length-3].split("=")[1]);
            debitCard = DebitCard.getDebitCard(debitCardValue);

            pathToFile = args[args.length-2];
            saveToFile = args[args.length-1];

            shift = 4;

        } else if (
                args.length >= 3 &&
                        checkProductArgs(args, 0, args.length-5, "[0-9]+-[0-9]+") &&
                        args[args.length-2].matches("discountCard=[0-9]{4}") &&
                        args[args.length-1].matches("balanceDebitCard=[0-9]+")
        ) {
            discountCard = getCard(cardTable, args[args.length-2]);
            debitCardValue = Integer.parseInt(args[args.length-1].split("=")[1]);
            debitCard = DebitCard.getDebitCard(debitCardValue);

            pathToFile = products_path;
            saveToFile = "result.csv";

            shift = 2;

        } else {
            System.out.println("BAD REQUEST");
            CheckWriter.writeCheck("result.csv", "BAD REQUEST");
            return;
        }

        System.out.print("\nLoading product table... ");

        try {
            productTable = CSVTable.getTable(products_path);
        } catch (FileNotFoundException exc) {
            System.out.println("File not found...");
            CheckWriter.writeCheck("result.csv", "INTERNAL SERVER ERROR");
            return;
        } catch (IOException exc){
            System.out.println("Unknown error...");
            CheckWriter.writeCheck("result.csv", "INTERNAL SERVER ERROR");
            return;
        }

        System.out.println("Loading successfull!");
        System.out.println("\n=============================\n");

        // print product table
        productTable.printTable();

        System.out.println("\n=============================\n");



        productCounts = formShoppingList(productTable, Arrays.copyOfRange(args, 0, args.length), shift);
        CheckWriter.writeCheck("result.csv", productCounts, discountCard, debitCard);

    }

    private static Map<Product, Integer> formShoppingList(CSVTable productTable, String[] args, int shift){

        Map<Product, Integer> shoppingList = new HashMap<>();

        for(int i = 0; i < args.length - shift; i++){
            int productId = Integer.parseInt(args[i].split("-")[0]);
            int productCount = Integer.parseInt(args[i].split("-")[1]);
            String[] productLine = productTable.getRow(productId);
            if (productLine == null){
                continue;
            }

            Product product = Product.getProduct(
                    Integer.parseInt(productLine[0]),
                    productLine[1],
                    Float.parseFloat(productLine[2]),
                    Integer.parseInt(productLine[3]),
                    Boolean.parseBoolean(productLine[4])
            );

            if(shoppingList.containsKey(product)){
                shoppingList.put(product, shoppingList.get(product) + productCount);
            } else {
                shoppingList.put(product, productCount);
            }

        }

        return shoppingList;
    }

    private static boolean checkProductArgs(String[] args, int start, int end, String regexp){

        for(int i = start; i <= end; i++){
            if (!args[i].matches(regexp)){
                return false;
            }
        }

        return true;
    }

    private static DiscountCard getCard(CSVTable cardTable, String arg){
        String cardNumber = arg.split("=")[1];
        String[] cardLine = cardTable.getRow(Integer.parseInt(cardNumber) - 1110);

        DiscountCard discountCard;

        if (cardLine != null){
            discountCard = DiscountCard.getCard(
                    Integer.parseInt(cardLine[0]),
                    Integer.parseInt(cardLine[1]),
                    Integer.parseInt(cardLine[2]));
        } else {
            discountCard = DiscountCard.getCard(
                    0,
                    Integer.parseInt(cardNumber),
                    2
            );
        }

        return discountCard;
    }

}
