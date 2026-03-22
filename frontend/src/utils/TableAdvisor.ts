import { Table } from "../dto";
import { INewBookingState } from "../interface/INewBookingState";

export class TableAdvisor {
    static getScore(table: Table, selectionState: INewBookingState): number {
        let score = 0;
        let attributeScore = 0;
        let seatScore = 0;

        if (selectionState.attributes.length !== 0) {
            for (let selectedAttribute of selectionState.attributes) {
                if (table.attributes.includes(selectedAttribute)) {
                    attributeScore++;
                } else {
                    attributeScore--;
                }
            }
            attributeScore /= (selectionState.attributes.length + table.attributes.length);
        } else {
            attributeScore = table.attributes.length === 1 ? 1 : 0;
        }

        if (table.seatsAmount < selectionState.seatsAmount) {
            seatScore = 0;
        } else if (table.seatsAmount === selectionState.seatsAmount) {
            seatScore = 1;
        } else {
            seatScore = table.seatsAmount / (table.seatsAmount + selectionState.seatsAmount);
        }

        score = 0.3 * attributeScore + 0.8 * seatScore;

        return score;
    }
}