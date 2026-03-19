import { z } from "zod";

export const UserSchema = z.object({
    id: z.uuid(),
    email: z.email()
})

export type User = z.infer<typeof UserSchema>