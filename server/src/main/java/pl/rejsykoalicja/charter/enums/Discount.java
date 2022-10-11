package pl.rejsykoalicja.charter.enums;

public enum Discount {
    JANUARY_BOOKING(20),
    FEBRUARY_BOOKING(10),
    TWO_WEEKS(5),
    THREE_WEEKS(10),
    REGULAR_CUSTOMER(5);

    public final int discountSize;

    Discount(int discountSize) {
        this.discountSize = discountSize;
    }
}
