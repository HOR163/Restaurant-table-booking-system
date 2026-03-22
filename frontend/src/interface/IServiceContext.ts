import {
    AttributeService,
    BookingService,
    BookingSlotService,
    RestaurantService,
    TableService
} from "../service";

export interface IServiceContext {
    attribute: AttributeService,
    booking: BookingService,
    bookingSlot: BookingSlotService,
    restaurant: RestaurantService,
    table: TableService,
};