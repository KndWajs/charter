export enum Discount {
    JANUARY_BOOKING = "JANUARY_BOOKING",
    FEBRUARY_BOOKING = "FEBRUARY_BOOKING",
    TWO_WEEKS = "TWO_WEEKS",
    THREE_WEEKS = "THREE_WEEKS",
    REGULAR_CUSTOMER = "REGULAR_CUSTOMER"
}

export interface DiscountType {
    name: string
    discountSize: number
}

export const DiscountProperties = new Map<Discount, DiscountType>([
    [Discount.JANUARY_BOOKING, {name: "January booking", discountSize: 20}],
    [Discount.FEBRUARY_BOOKING, {name: "February booking", discountSize: 10}],
    [Discount.TWO_WEEKS, {name: "Two weeks", discountSize: 5}],
    [Discount.THREE_WEEKS, {name: "Three weeks", discountSize: 10}],
    [Discount.REGULAR_CUSTOMER, {name: "Regular customer", discountSize: 5}]
])

export const getDiscountName = (discount: Discount): string | undefined =>
    DiscountProperties.get(discount)?.name

export const getDiscountColor = (discount: Discount): number | undefined =>
    DiscountProperties.get(discount)?.discountSize
