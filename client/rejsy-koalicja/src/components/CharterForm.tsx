import {Button, Callout, Classes,} from "@blueprintjs/core"
import {DateRange, DateRangeInput2} from "@blueprintjs/datetime2"
import moment from "moment";

import React, {useEffect, useState} from "react"
import CharterDto from "../shared/models/charterDto";
import axios from "axios";
import CharterDetails from "./CharterDetails";


export default function CharterForm() {

    const getEmptyCharter = (): CharterDto => {
        return {
            from: new Date(2023, 7, 10),
            to: new Date(2023, 7, 29),
            payoff: {
                voucher: {code: ""}
            }
        }
    }

    const [charter, setCharter] = useState(getEmptyCharter())

    // useEffect(() => {
    // })


    const handleVoucherData = (e: React.FormEvent<HTMLInputElement>) => {

        setCharter({
            ...charter,
            payoff: {
                ...charter.payoff,
                voucher: {
                    ...charter.payoff?.voucher,
                    code: e.currentTarget.value
                }
            }
        })
    }


    function sendCharterDto(newCharter: CharterDto) {
        axios.put(
            "/api/charter/create-charter",
            newCharter
        ).then(function (response) {
            setCharter(response.data)
            console.log(response.data);
        })
            .catch(function (error) {
                // handle error
                console.log(error);
            })
            .finally(function () {
                // always executed
            })
    }

    function calculateAction() {
        const newCharter = {
            ...charter
        }
        sendCharterDto(newCharter)
    }

    function clear() {
        setCharter(getEmptyCharter())
    }

    const handleKeypress = (event: React.KeyboardEvent<HTMLInputElement>) => {
        if (event.key === "Enter") {
            calculateAction()
        }
    }

    // eslint-disable-next-line import/prefer-default-export
    const formatDate = (date: any) => {
        return moment(date).format("YYYY-MM-DD")
    }

    let addSampleVoucher = () => {
        setCharter({
            ...charter,
            payoff: {
                ...charter.payoff,
                voucher: {
                    ...charter.payoff?.voucher,
                    code: "SCD-001"
                }
            }
        })
    };
    return (
        <div style={{width: 600}}>

            <h1>This Page is only for testing back-end!</h1>

            <Callout
                style={{
                    marginBottom: "10px", display: "flex",
                    justifyContent: "flex-start",
                    flexDirection: "column",
                    alignItems: "flex-start"
                }}

            >

                <label>Voucher code:</label>
                <div><input
                    id="code"
                    value={charter?.payoff?.voucher?.code || ""}
                    className={Classes.INPUT}
                    placeholder="eg. SCD-001 or SCD-002"
                    onChange={handleVoucherData}
                    onKeyPress={handleKeypress}
                />
                    <Button
                        text="add sample voucher"
                        onClick={addSampleVoucher}
                    />
                </div>
                <label>Charter: </label>
                <DateRangeInput2
                    formatDate={(date: any) => formatDate(date)}
                    minDate={new Date()}
                    maxDate={new Date(new Date().getFullYear() + 2, 12)}
                    onChange={(range: DateRange) => {

                        console.log(range[0])
                        console.log(formatDate(range[0]))
                        setCharter({
                            ...charter,
                            from: range[0] ? range[0] : new Date(2023, 7, 10),
                            to: range[1] ? range[1] : new Date(2023, 7, 29),
                        })
                    }}
                    parseDate={(str) => new Date(str)}
                    shortcuts={false}
                    value={[
                        new Date(charter.from),
                        new Date(charter.to),
                    ]}
                />

                <div className="filtering-buttons">
                    <Button
                        minimal
                        text="Clear"
                        onClick={() => {
                            clear()
                        }}
                    />
                    <Button
                        text="Calculate"
                        alignText="right"
                        onClick={() => calculateAction()}
                    />
                </div>
            </Callout>

            <CharterDetails charter={charter}/>
        </div>
    )
}
