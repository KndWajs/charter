import {Card, Elevation, Icon, IconSize} from "@blueprintjs/core"

import React from "react"
import {useSelector} from "react-redux";
import {RootState} from "../../../app/store";
import "./discounts.scss"
import {Discount, DiscountProperties, DiscountType} from "../../../shared/enums/discount";


export default function Discounts() {

    const payoff = useSelector((state: RootState) => state.charter.charter.payoff)

    return (
        <Card id="main-card" interactive={false} elevation={Elevation.TWO}>
            <div style={{display: "flex", flexDirection: "row", alignItems: "flex-end"}}>
                <h3 className="bp4-heading">Discounts </h3><p style={{marginLeft: "10px"}}>(Maximum 20%)</p>
            </div>
            {!payoff ?
                <>
                    <h5 className="bp4-heading">Available discounts:</h5>
                    {Array.from(DiscountProperties.entries()).map((disc: [Discount, DiscountType]) =>
                        <span className="bp4-tag">
                                    {disc[1].name} ({disc[1].discountSize} %)
                        </span>
                    )}
                </>
                :
                <>
                    <p><b>Total discount:</b> {payoff.discountValue || 0}%</p>
                    {payoff.voucher?.code ?
                        <p><b>Voucher code:</b> {payoff.voucher?.code}
                            <React.Fragment> | discount: {payoff.voucher?.amount || 0}%</React.Fragment>
                        </p> : null
                    }
                    {Array.from(DiscountProperties.entries()).map((disc: [Discount, DiscountType]) =>
                        <p>
                            {payoff.discounts?.find(d => d === disc[0]) ?
                                <span className="bp4-tag bp4-large bp4-intent-success">
                                    {disc[1].name} ({disc[1].discountSize} %)
                                </span>
                                :
                                <span className="bp4-tag  ">
                                    {disc[1].name} ({disc[1].discountSize} %)
                                </span>
                            }
                        </p>
                    )}
                </>
            }
        </Card>

    )
}
