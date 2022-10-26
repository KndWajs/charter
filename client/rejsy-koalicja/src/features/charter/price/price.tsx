import {Card, Elevation} from "@blueprintjs/core"

import React from "react"
import {useSelector} from "react-redux";
import {RootState} from "../../../app/store";
import "./price.scss"
import {formatDate} from "../../../utils/general";


export default function Price() {

    const payoff = useSelector((state: RootState) => state.charter.charter.payoff)

    return (
        <Card id="main-card" interactive={false} elevation={Elevation.TWO}>
            <h3 className="bp4-heading">Price</h3>
            {!payoff ?
                <p>Create charter to see price...</p> :
                <>

                    <p><b>Total cost: </b>
                        {Intl.NumberFormat('pl-PL', {style: 'currency', currency: 'PLN'})
                            .format(payoff.totalCost!)}
                    </p>
                    <p>
                        {payoff.days2Price ? Array.from(payoff.days2Price!).map(function (day2price, i) {
                            // @ts-ignore
                            const days = day2price.first
                            const price = Intl.NumberFormat('pl-PL', {
                                style: 'currency',
                                currency: 'PLN'
                                // @ts-ignore
                            }).format(day2price.second)
                            return (
                                <div>
                                    <p>Price per day: {price} | Days: {days}</p>
                                </div>
                            )
                        }) : null}
                    </p>
                    <p><b>Total discount:</b> {payoff.discountValue || 0}%</p>
                    <p><b>Voucher code:</b> {payoff.voucher?.code}
                        {payoff.voucher?.code ?
                            <React.Fragment> | discount: {payoff.voucher?.amount || 0}%</React.Fragment> : null}
                    </p>
                    <p><b>Discounts:</b>
                        {payoff.discounts?.map(function (discount, i) {
                            return " " + discount;
                        })}
                    </p>
                    <p><b>Payments:</b>
                        {payoff.payments?.map(function (payment, i) {
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
                </>
            }
        </Card>

    )
}
