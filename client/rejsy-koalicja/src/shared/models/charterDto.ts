import {Equipment} from "../enums/equipment";
import {CaptainDto} from "./captainDto";
import {PayoffDto} from "./payoffDto";

export default interface CharterDto {
    equipment?: Equipment[]
    from: Date
    to: Date
    captain?: CaptainDto
    crewNumber?: number
    payoff?: PayoffDto
    reservation?: Date
    thermsAndConditionVersion?: string
}
