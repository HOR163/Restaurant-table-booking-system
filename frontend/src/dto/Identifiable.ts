import { z } from "zod";

export const Identifiable = z.object({
    id: z.uuid()
});