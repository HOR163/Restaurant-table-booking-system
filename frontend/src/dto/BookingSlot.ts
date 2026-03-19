import { z } from "zod";

export const BookingSlotSchema = z.object({
    startTime: z.iso.time(),
    endTime: z.iso.time()
})

export type BookingSlot = z.infer<typeof BookingSlotSchema>
