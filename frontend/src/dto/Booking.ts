import { z } from "zod";
import { Identifiable } from "./Identifiable";

export const BookingSchema = Identifiable.extend({
    tableId: z.uuid(),
    userId: z.uuid(),
    restaurantId: z.uuid(),
    startTime: z.iso.datetime(),
    pending: z.optional(z.boolean()),
    pendingEndTime: z.optional(z.iso.datetime())
})

export type Booking = z.infer<typeof BookingSchema>