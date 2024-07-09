package main.java.ru.clevertec.check.card;

public class DebitCard {
    private int value;

    private DebitCard(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static DebitCard getDebitCard(int value){
        return new DebitCard(value);
    }
}
