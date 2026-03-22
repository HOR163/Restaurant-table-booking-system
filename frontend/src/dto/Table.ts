import { z } from "zod";
import { Identifiable } from "./Identifiable";

export const TableSchema = Identifiable.extend({
    restaurantId: z.uuid(),
    seatsAmount: z.int(),
    tableNumber: z.int(),
    attributes: z.array(z.uuid())
})

export type Table = z.infer<typeof TableSchema>