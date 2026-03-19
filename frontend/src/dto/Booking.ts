import { z } from "zod";

export const BookingSchema = z.object({
    id: z.uuid(),
    tableId: z.uuid(),
    userId: z.uuid(),
    restaurantId: z.uuid(),
    startTime: z.iso.datetime(),
    pending: z.boolean(),
    pendingEndTime: z.iso.datetime()
})

export type Booking = z.infer<typeof BookingSchema>