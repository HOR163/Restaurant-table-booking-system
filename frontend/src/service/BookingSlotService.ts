import { BookingSlot, BookingSlotSchema } from "../dto/BookingSlot";
import { z } from "zod";


export class BookingSlotService {
    // No base url due to proxy
    private BASE_URL = "";
    private schema = BookingSlotSchema;

    async getForRestaurant(restaurantId: string, date: string): Promise<Map<string, BookingSlot[]>> {
        const requestUrl = `${this.BASE_URL}/restaurants/${restaurantId}/slots?date=${date}`;

        try {
            const response = await fetch(requestUrl);
            const json = await response.json();

            const parseResult = z.safeParse(z.record(z.uuid(), z.array(this.schema)), json);
            if (parseResult.success) {
                return new Map(Object.entries(json));
            }

            console.error(`Invalid schema gotten from get request to ${requestUrl}`, parseResult.error);

            return new Map();
        } catch (error) {
            console.error(`Error with get request to ${requestUrl} ${error}`)
            return new Map();
        }
    }
}