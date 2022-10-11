import {Button, Callout, Classes,} from "@blueprintjs/core"
import {DateRange, DateRangeInput2} from "@blueprintjs/datetime2"
import moment from "moment";

import React, {useState} from "react"
import CharterDto from "../shared/models/charterDto";
import axios from "axios";
import CharterDetails from "./CharterDetails";


export default function CharterForm() {

    const getEmptyCharter = (): CharterDto => {
        return {
            from: new Date(),
            to: new Date(),
            payoff: {
                voucher: {code: ""}
            }
        }
    }

    const [charter, setCharter] = useState(getEmptyCharter())

    // useEffect(() => {
    //     setCharter()
    // }, [])


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
            console.log(response);
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

    function clear(clearFormular: boolean) {
        if (clearFormular) {
            setCharter(getEmptyCharter())
        }
        sendCharterDto({} as CharterDto)
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

    return (
        <div style={{width: 500}}>
            <Callout
                style={{
                    marginBottom: "10px", display: "flex",
                    justifyContent: "flex-start",
                    flexDirection: "column"
                }}

            >

                    <label>Voucher code:</label>
                    <input
                        id="code"
                        value={charter?.payoff?.voucher?.code || ""}
                        className={Classes.INPUT}
                        placeholder="eg. SCD-001 or SCD-002"
                        onChange={handleVoucherData}
                        onKeyPress={handleKeypress}
                    />

                    <label>Charter: </label>
                    <DateRangeInput2
                        formatDate={(date: any) => formatDate(date)}
                        onChange={(range: DateRange) => {

                            console.log(range[0])
                            console.log(formatDate(range[0]))
                            setCharter({
                                ...charter,
                                from: range[0] ? range[0] : new Date(),
                                to: range[1] ? range[1] : new Date(),
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
                            clear(true)
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
