import { type Booking, BookingSchema } from "../dto/Booking";
import { BaseApiService } from "./BaseApiService";

export class BookingService extends BaseApiService<Booking> {
    constructor() {
        super(BookingSchema);
    }

    async get(bookingId: string): Promise<Booking | null> {
        return super.getSingle(`/bookings/${bookingId}`);
    }

    async getUserBookings(userId: string, spanStart: string, spanEnd: string): Promise<Booking[]> {
        return super.getArray(`/users/${userId}/bookings?spanStart=${spanStart}&spanEnd=${spanEnd}`);
    }

    async create(data: Omit<Booking, "id">): Promise<Booking | null> {
        return super.post("/bookings", data);
    }

    async delete(bookingId: string): Promise<number> {
        return super.delete(`/bookings/${bookingId}`);
    }
}