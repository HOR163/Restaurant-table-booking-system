import { BookingSlot, BookingSlotSchema } from "../dto/BookingSlot";
import { BaseApiService } from "./BaseApiService";


export class BookingSlotService extends BaseApiService<BookingSlot> {
    constructor() {
        super(BookingSlotSchema);
    }

    async getForRestaurant(restaurantId: string, date: string): Promise<BookingSlot[]> {
        return super.getArray(`/restaurants/${restaurantId}/slots?date=${date}`);
    }
}