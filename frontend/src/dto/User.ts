import { z } from "zod";

const UserSchema = z.object({
    id: z.uuid(),
    email: z.email()
})

export type User = z.infer<typeof UserSchema>