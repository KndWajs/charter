import moment from "moment";

export function addDays(date: Date, days: number): Date {
    var result = new Date(date);
    result.setDate(result.getDate() + days);
    return result;
}

// eslint-disable-next-line import/prefer-default-export
export const formatDate = (date: any) => {
    return moment(date).format("YYYY-MM-DD")
}
