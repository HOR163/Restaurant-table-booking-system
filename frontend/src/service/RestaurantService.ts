import { type Restaurant, RestaurantSchema } from "../dto/Restaurant";
import { BaseApiService } from "./BaseApiService";

export class RestaurantService extends BaseApiService<Restaurant> {
    constructor() {
        super("/restaurants", RestaurantSchema);
    }
}