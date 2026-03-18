import { z } from "zod";

const TableSchema = z.object({
    id: z.uuid(),
    restaurantId: z.email(),
    seatsAmount: z.int(),
    attributes: z.array(z.uuid())
})

export type Table = z.infer<typeof TableSchema>