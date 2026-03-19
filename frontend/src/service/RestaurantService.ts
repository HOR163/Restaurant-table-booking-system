import { type Restaurant, RestaurantSchema } from "../dto/Restaurant";
import { BaseApiService } from "./BaseApiService";

export class RestaurantService extends BaseApiService<Restaurant> {
    constructor() {
        super(RestaurantSchema);
    }

    async get(restaurantId: string): Promise<Restaurant | null> {
        return super.getSingle(`/restaurants/${restaurantId}`);
    }

    async getAll(): Promise<Restaurant[]> {
        return super.getArray(`/restaurants`);
    }

    async create(data: Omit<Restaurant, "id" | "tables">): Promise<Restaurant | null> {
        return super.post("/restaurants", {
            ...data,
            tables: []
        })
    }

    async delete(restaurantId: string): Promise<number> {
        return super.delete(`/restaurants/${restaurantId}`);
    }
}