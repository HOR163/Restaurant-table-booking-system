import { z } from "zod";
import { Identifiable } from "./Identifiable";

export const UserSchema = Identifiable.extend({
    email: z.email()
})

export type User = z.infer<typeof UserSchema>