import {Discount} from "../enums/discount";
import {VoucherDto} from "./voucherDto";
import {PaymentDto} from "./paymentDto";

export interface PayoffDto {
    totalCost?: number
    discountValue?: number
    discounts?: Discount[]
    voucher?: VoucherDto
    payments?: PaymentDto[]
}
