import { createContext } from "react";
import {
    RestaurantService,
    AttributeService,
    TableService,
    BookingSlotService,
    BookingService,
} from "../service";
import { IServiceContext } from "../interface";

export const serviceContextData: IServiceContext = {
    attribute: new AttributeService(),
    booking: new BookingService(),
    bookingSlot: new BookingSlotService(),
    restaurant: new RestaurantService(),
    table: new TableService(),
};

export const ServiceContext = createContext<IServiceContext>(serviceContextData);
