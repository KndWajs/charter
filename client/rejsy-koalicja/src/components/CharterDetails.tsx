import {Callout} from "@blueprintjs/core"
import moment from "moment";

import React from "react"
import CharterDto from "../shared/models/charterDto";

export interface CharterDetailsProps {
    charter: CharterDto
}

export default function CharterDetails({charter}: CharterDetailsProps) {

    const formatDate = (date: any) => {
        return moment(date).format("YYYY-MM-DD")
    }
    return (
        <Callout
            style={{
                marginBottom: "10px",
                display: "flex",
                justifyContent: "flex-start",
                flexDirection: "column",
                alignItems: "flex-start"
            }}
        >
            <p><b>Charter from</b> {formatDate(charter.from)} <b>to</b> {formatDate(charter.to)} </p>
            <p><b>Total cost: </b>
                {Intl.NumberFormat('pl-PL', {style: 'currency', currency: 'PLN'})
                    .format(charter.payoff!.totalCost!)}
            </p>
            <p><b>Total discount:</b> {charter.payoff?.discountValue || 0}%</p>
            <p><b>Voucher code:</b> {charter.payoff?.voucher?.code}
                {charter.payoff?.voucher?.code ?
                    <React.Fragment> | discount: {charter.payoff?.voucher?.amount || 0}%</React.Fragment> : null}
            </p>
            <p><b>Discounts:</b>
                {charter.payoff?.discounts?.map(function (discount, i) {
                    return " " + discount;
                })}
            </p>
            <p><b>Payments:</b>
                {charter.payoff?.payments?.map(function (payment, i) {
                    return (
                        <div>
                            <p>{i + 1}. Payment date: {formatDate(payment.paymentDate)} |
                                Amount:
                                {" " + Intl.NumberFormat('pl-PL', {style: 'currency', currency: 'PLN'})
                                    .format(payment.amount)}</p>
                        </div>
                    )
                })}
            </p>

        </Callout>
    )
}
