import { Restaurant, Table } from "../dto";

export interface INewBookingState {
    restaurant: Restaurant | null,
    table: Table | null,
    date: string | null,
    time: string | null,
    seatsAmount: number,
    attributes: string[]
}
