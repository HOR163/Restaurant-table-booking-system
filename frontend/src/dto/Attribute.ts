import { z } from "zod";

const AttributeSchema = z.object({
    id: z.uuid(),
    name: z.string()
})

export type Attribute = z.infer<typeof AttributeSchema>