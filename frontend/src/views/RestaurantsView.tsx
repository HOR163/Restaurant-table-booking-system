import { List, ListItemButton, ListItemText, Button, Modal, Box, Typography, FormControl, InputLabel, OutlinedInput } from "@mui/material";
import { RestaurantService } from "../service/RestaurantService";
import { useState, useEffect, useMemo, useCallback } from "react";
import { Restaurant } from "../dto/Restaurant";
import { useNavigate } from "react-router-dom";

export default function RestaurantView() {
    const restaurantService = useMemo(() => new RestaurantService(), []);

    const [restaurants, setRestaurants] = useState<Restaurant[]>([]);
    const [addRestaurantModelOpen, setAddRestaurantModelOpen] = useState(false);
    const [restaurantData, setRestaurantData] = useState({
        ownerId: "f04b862d-eb29-4fd6-8ada-b882e71b2b26",
        name: ""
    });

    const navigate = useNavigate();

    const loadData = useCallback(async () => {
        const restaurants = await restaurantService.getAll();
        setRestaurants(restaurants);
    }, [restaurantService]);

    const handleAddRestaurantModalClose = () => {
        setAddRestaurantModelOpen(false);
    }

    const handleCreateRestaurant = async () => {
        if (restaurantData.name.trim().length === 0) {
            return;
        }

        try {
            await restaurantService.create(restaurantData);
        } catch (error) {
            console.error("Error creating restaurant:", error);
        }

        // Reset the form and close the modal
        setRestaurantData({
            ...restaurantData,
            name: ""
        });
        setAddRestaurantModelOpen(false);
        loadData();
    }

    const handleDeleteRestaurant = async (restaurantId: string) => {
        const success = await restaurantService.delete(restaurantId);
        if (success) {
            loadData();
        }
    }

    useEffect(() => {
        loadData();
    }, [loadData]);

    const style = {
        position: 'absolute',
        top: '50%',
        left: '50%',
        transform: 'translate(-50%, -50%)',
        width: 400,
        bgcolor: 'background.paper',
        border: '2px solid #000',
        boxShadow: 24,
        p: 4,
    };

    return <>
        <Button onClick={() => setAddRestaurantModelOpen(true)}>
            Add restaurant
        </Button>
        <List>
            {restaurants.map(restaurant =>
                <ListItemButton key={restaurant.id} className="flex" onClick={() => navigate(`/newBooking/${restaurant.id}`)}>
                    <ListItemText sx={{ flex: 1 }}
                        primary={restaurant.name} />
                    <Button variant="contained" color="error" onClick={
                        (e) => {
                            e.stopPropagation();
                            handleDeleteRestaurant(restaurant.id);
                        }}>
                        Delete
                    </Button>
                </ListItemButton>)
            }
        </List>

        {/* Modal taken from https://mui.com/material-ui/react-modal/ */}
        <Modal
            open={addRestaurantModelOpen}
            onClose={() => handleAddRestaurantModalClose()}
            aria-labelledby="modal-modal-title"
            aria-describedby="modal-modal-description"
        >
            <Box sx={style}>
                <form onSubmit={(e) => {
                    e.preventDefault();
                    handleCreateRestaurant();
                }}>
                    <Typography id="modal-modal-title" variant="h6" component="h2">
                        Create restaurant
                    </Typography>
                    <FormControl fullWidth sx={{ mt: 2 }}>
                        <InputLabel htmlFor="my-input">Name</InputLabel>
                        <OutlinedInput
                            id="my-input"
                            label="Name"
                            value={restaurantData.name}
                            onChange={(e) => setRestaurantData({ ...restaurantData, name: e.target.value })}
                        />
                    </FormControl>
                    <Button variant="contained" sx={{ mt: 2 }} type="submit">
                        Create
                    </Button>
                </form>
            </Box>
        </Modal>
    </>
}
