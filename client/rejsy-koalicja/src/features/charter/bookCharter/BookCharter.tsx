import {Button, Card, Classes, Elevation} from "@blueprintjs/core"

import React, {useEffect} from "react"
import "./BookCharter.scss"
import {DateInput2} from "@blueprintjs/datetime2";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../app/store";
import {setCharter, setFrom, setTo, setVoucherCode} from "../charterSlice";
import {useCreateCharterMutation} from "../../../services/charter";
import {addDays, formatDate} from "../../../utils/general";
import CharterDto from "../../../shared/models/charterDto";


export default function BookCharter() {

    const from = useSelector((state: RootState) => state.charter.charter.from)
    const to = useSelector((state: RootState) => state.charter.charter.to)
    const voucherCode = useSelector((state: RootState) => state.charter.charter.payoff?.voucher?.code)
    const charter = useSelector((state: RootState) => state.charter.charter)
    const dispatch = useDispatch()

    const [updatePost, result] = useCreateCharterMutation()

    useEffect(() => {
        if (!result.isUninitialized && result.isSuccess) {
            dispatch(setCharter(result.data as CharterDto))
        }
    }, [result])

    const handleVoucherData = (e: React.FormEvent<HTMLInputElement>) => {
        dispatch(setVoucherCode(e.currentTarget.value))
    }

    const addSampleVoucher = () => {
        dispatch(setVoucherCode("SCD-001"))
    };

    return (
        <Card id="main-card" interactive={false} elevation={Elevation.TWO}>
            <h3 className="bp4-heading">Book a charter</h3>

            <div className="double-input margin">
                <div className="titles">
                    <div className="left-title">
                        Starting date
                    </div>
                    <div className="right-title">
                        Ending date
                    </div>
                </div>
                <div className="content">
                    <div className="left-content">
                        <DateInput2
                            className="input"
                            formatDate={(date: Date) => formatDate(date)}
                            minDate={new Date()}
                            maxDate={addDays(new Date(), 365)}
                            onChange={(date: string | null) => {
                                dispatch(setFrom(date ? new Date(date) : new Date()))
                            }}
                            parseDate={(str) => new Date(str)}
                            shortcuts={false}
                            value={from.toString()}
                        />
                    </div>
                    <div className="right-content">
                        <DateInput2
                            className="input"
                            formatDate={(date: Date) => formatDate(date)}
                            minDate={new Date(from)}
                            maxDate={addDays(new Date(), 365)}
                            onChange={(date: string | null) => {
                                dispatch(setTo(date ? new Date(date) : addDays(new Date(), 7)),
                                )
                            }}
                            parseDate={(str) => new Date(str)}
                            shortcuts={false}
                            value={to.toString()}
                        />
                    </div>
                </div>
            </div>

            <div className="double-input margin">
                <div className="titles">
                    <div className="left-title">
                        Voucher code
                    </div>
                </div>
                <div className="content">
                    <div className="left-content">
                        <input
                            id="code"
                            value={voucherCode || ""}
                            className={Classes.INPUT}
                            placeholder="eg. SCD-001 or SCD-002"
                            onChange={handleVoucherData}
                        />
                    </div>
                    <div className="right-content">
                        <Button
                            text="add sample voucher"
                            onClick={addSampleVoucher}
                        />
                    </div>

                </div>
            </div>

            <Button onClick={() => updatePost(charter)}>Submit</Button>
        </Card>
    )
}
