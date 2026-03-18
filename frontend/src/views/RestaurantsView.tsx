import { List, ListItemButton, ListItemText } from "@mui/material";
import { RestaurantService } from "../service/RestaurantService";
import { useState, useEffect } from "react";
import { Restaurant } from "../dto/Restaurant";

export default function RestaurantView() {
    const [restaurants, setRestaurants] = useState<Restaurant[]>([]);

    useEffect(() => {
        const restaurantService = new RestaurantService();
        restaurantService.getAll().then(restaurants => setRestaurants(restaurants));
    }, []);

    return <>
        <List>
            {restaurants.map(restaurant =>
                <ListItemButton key={restaurant.id} href={`/newBooking/${restaurant.id}`} onClick={() => console.log(restaurant)}>
                    <ListItemText primary={restaurant.name} />
                </ListItemButton>)
            }
        </List>
    </>
} 