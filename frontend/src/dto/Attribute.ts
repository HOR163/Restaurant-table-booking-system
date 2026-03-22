import { z } from "zod";
import { Identifiable } from "./Identifiable";

export const AttributeSchema = Identifiable.extend({
    name: z.string()
});

export type Attribute = z.infer<typeof AttributeSchema>