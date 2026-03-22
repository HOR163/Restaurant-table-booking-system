import { Table } from "../dto";
import { INewBookingState } from "../interface/INewBookingState";

export class TableAdvisor {

    /**
     * Calculate the score for given table and current booking state
     * @param table table for which the score is calculated
     * @param bookingState current state of the booking (selected attributes, number of people)
     * @returns a score between -0.5 to 1
     */
    static getScore(table: Table, bookingState: INewBookingState): number {
        let score = 0;
        let attributeScore = 0;
        let seatScore = 0;

        if (bookingState.attributes.length !== 0) {
            for (let selectedAttribute of bookingState.attributes) {
                if (table.attributes.includes(selectedAttribute)) {
                    attributeScore++;
                } else {
                    attributeScore--;
                }
            }
            attributeScore /= (bookingState.attributes.length + table.attributes.length);
        } else {
            attributeScore = table.attributes.length === 1 ? 1 : 0;
        }

        if (table.seatsAmount < bookingState.seatsAmount) {
            seatScore = 0;
        } else if (table.seatsAmount === bookingState.seatsAmount) {
            seatScore = 1;
        } else {
            seatScore = table.seatsAmount / (table.seatsAmount + bookingState.seatsAmount);
        }

        score = 0.5 * attributeScore + 0.5 * seatScore;

        return score;
    }
}