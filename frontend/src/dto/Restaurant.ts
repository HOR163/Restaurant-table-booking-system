import { z } from "zod";

export const RestaurantSchema = z.object({
    id: z.uuid(),
    ownerId: z.uuid(),
    name: z.string(),
    tables: z.array(z.uuid()),
})

export type Restaurant = z.infer<typeof RestaurantSchema>;
