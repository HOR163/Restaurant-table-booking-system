import { z } from "zod";

export const TableSchema = z.object({
    id: z.uuid(),
    restaurantId: z.uuid(),
    seatsAmount: z.int(),
    attributes: z.array(z.uuid())
})

export type Table = z.infer<typeof TableSchema>