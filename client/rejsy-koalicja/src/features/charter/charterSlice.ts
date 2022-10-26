import {createSlice} from '@reduxjs/toolkit'
import type {PayloadAction} from '@reduxjs/toolkit'
import CharterDto from "../../shared/models/charterDto";
import {addDays} from "../../utils/general";

export interface CharterState {
    charter: CharterDto
}

const initialState: CharterState = {
    charter: {
        from: addDays(new Date(), 40),
        to: addDays(new Date(), 47)
    }
}

export const charterSlice = createSlice({
    name: 'charter',
    initialState,
    reducers: {
        setCharter: (state, action: PayloadAction<CharterDto>) => {
            state.charter = action.payload
        },
        setFrom: (state, action: PayloadAction<Date>) => {
            state.charter.from = action.payload
        },
        setTo: (state, action: PayloadAction<Date>) => {
            state.charter.to = action.payload
        },
        setVoucherCode: (state, action: PayloadAction<string>) => {
            state.charter.payoff = {
                ...state.charter.payoff,
                voucher: {
                    ...state.charter.payoff?.voucher,
                    code: action.payload
                }
            }
        },
    },
})

// Action creators are generated for each case reducer function
export const {setCharter, setFrom, setTo, setVoucherCode} = charterSlice.actions

export default charterSlice.reducer
