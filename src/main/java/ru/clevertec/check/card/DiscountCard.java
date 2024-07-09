package main.java.ru.clevertec.check.card;

public class DiscountCard {
    private int id;
    private int number;
    private float discount;

    private DiscountCard(
            int id,
            int number,
            int discount){
        this.id = id;
        this.number = number;
        this.discount = discount / 100f;
    }

    public static DiscountCard getCard(
            int id,
            int number,
            int discount){
        return new DiscountCard(
                id,
                number,
                discount
        );
    }

    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public float getDiscount() {
        return discount;
    }
}
