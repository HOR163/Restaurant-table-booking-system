import { z } from "zod";
import { Identifiable } from "./Identifiable";

export const RestaurantSchema = Identifiable.extend({
    ownerId: z.uuid(),
    name: z.string(),
    tables: z.array(z.uuid()),
})

export type Restaurant = z.infer<typeof RestaurantSchema>;
