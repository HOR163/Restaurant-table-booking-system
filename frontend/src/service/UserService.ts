import { User, UserSchema } from "../dto/User";
import { BaseApiService } from "./BaseApiService";

export class UserService extends BaseApiService<User> {
    constructor() {
        super(UserSchema);
    }

    async get(userId: string): Promise<User | null> {
        return super.getSingle(`/users/${userId}`);
    }

    async delete(userId: string): Promise<number> {
        return super.delete(`/users/${userId}`);
    }
}