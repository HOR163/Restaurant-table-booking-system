import { useContext, useState, useEffect, useCallback } from "react";
import { ServiceContext, UserContext } from "../context";
import { Attribute, BookingSlot, Table, Restaurant, Booking } from "../dto";
import { INewBookingState } from "../interface";
import { Converter } from "../utils";

export function useNewBooking() {
    // States
    const services = useContext(ServiceContext);
    const user = useContext(UserContext);

    const [restaurants, setRestaurants] = useState<Map<string, Restaurant>>(
        new Map(),
    );
    const [tables, setTables] = useState<Map<string, Table>>(new Map());
    const [bookingSlots, setBookingSlots] = useState<Map<string, BookingSlot[]>>(
        new Map(),
    );
    const [attributes, setAttributes] = useState<Map<string, Attribute>>(
        new Map(),
    );
    const [restaurantAttributes, setRestaurantAttributes] = useState<
        Map<string, Attribute>
    >(new Map());

    const [bookingState, setBookingState] = useState<INewBookingState>({
        restaurant: null,
        table: null,
        date: null,
        time: null,
        seatsAmount: 2,
        attributes: [],
    });

    // Helpers

    const loadBaseData = useCallback(async () => {
        try {
            const [r, a] = await Promise.all([
                services.restaurant.getAll(),
                services.attribute.getAll(),
            ]);

            setRestaurants(Converter.toMap(r));
            const mappedAttributes = Converter.toMap(a);
            setAttributes(mappedAttributes);

            const restaurant = r.length > 0 ? r[0] : null;
            const today = new Date();
            const tomorrow = new Date(today);
            tomorrow.setDate(today.getDate() + 1);
            const date = tomorrow.toISOString().split("T")[0];

            if (restaurant) {
                const initialTables = await services.table.getRestaurantTables(
                    restaurant.id,
                );
                setTables(Converter.toMap(initialTables));

                const availableAttributes = new Map<string, Attribute>();
                for (const table of initialTables) {
                    for (const attributeId of table.attributes) {
                        if (!availableAttributes.has(attributeId)) {
                            const attr = mappedAttributes.get(attributeId);
                            if (attr) availableAttributes.set(attributeId, attr);
                        }
                    }
                }
                setRestaurantAttributes(availableAttributes);

                const slots = await services.bookingSlot.getForRestaurant(
                    restaurant.id,
                    date,
                );
                setBookingSlots(slots);
            }

            setBookingState((prev) => ({
                ...prev,
                restaurant,
                date,
            }));
        } catch (error) {
            console.error("Failed to load booking data", error);
        }
    }, [services]);

    const validateState = (state: INewBookingState): boolean => {
        return state.restaurant &&
            state.date &&
            state.seatsAmount &&
            state.table &&
            state.time
            ? true
            : false;
    };

    const updateBookingSlots = async (
        restaurantId: string | null,
        date: string | null,
    ) => {
        if (!restaurantId || !date) {
            return;
        }

        try {
            const bookingSlots = await services.bookingSlot.getForRestaurant(
                restaurantId,
                date,
            );
            setBookingSlots(bookingSlots);
        } catch (error) {
            console.error(error);
        }
    };

    // Handlers
    const handleRestaurantChange = async (id: string) => {
        const selected = restaurants.get(id) ?? null;

        try {
            const tables = await services.table.getRestaurantTables(id);
            setTables(Converter.toMap(tables));

            await updateBookingSlots(id, bookingState.date);

            const availableAttributes = new Map<string, Attribute>();
            for (const table of tables) {
                for (const attributeId of table.attributes) {
                    if (!availableAttributes.has(attributeId)) {
                        const attribute = attributes.get(attributeId);
                        if (!attribute) {
                            continue;
                        }
                        availableAttributes.set(attributeId, attribute);
                    }
                }
            }

            setRestaurantAttributes(availableAttributes);
        } catch (error) {
            console.error(error);
        }

        setBookingState((prev) => ({
            ...prev,
            restaurant: selected,
            table: null, // Reset table if restaurant changes
            time: null, // Reset time if restaurant changes
        }));
    };

    const handleDateChange = (date: string): void => {
        const dateTime = new Date(date);

        if (dateTime.getTime() < new Date().getTime()) {
            return;
        }

        const stringDate = dateTime.toISOString().split("T")[0];

        updateBookingSlots(bookingState.restaurant?.id ?? null, stringDate);

        setBookingState((prev) => ({
            ...prev,
            date: stringDate,
        }));
    };

    const handleSeatsAmountChange = (seatsAmount: string): void => {
        const seatsAmountNumber = parseInt(seatsAmount, 10);

        if (isNaN(seatsAmountNumber)) {
            return;
        }

        if (seatsAmountNumber < 1 || seatsAmountNumber > 10) {
            return;
        }

        setBookingState((prev) => ({
            ...prev,
            seatsAmount: seatsAmountNumber,
        }));
    };

    const handleAttributesChange = (attributeId: string): void => {
        const attributes = bookingState.attributes;

        if (attributes.includes(attributeId)) {
            setBookingState((prev) => ({
                ...prev,
                attributes: attributes.filter((a) => a !== attributeId),
            }));
        } else {
            setBookingState((prev) => ({
                ...prev,
                attributes: [...attributes, attributeId],
            }));
        }
    };

    const handleTableChange = (tableId: string) => {
        setBookingState((prev) => ({
            ...prev,
            table: tables.get(tableId) ?? null,
            time: null,
        }))
    }

    const handleTimeChange = (time: string) => {
        setBookingState((prev) => ({
            ...prev,
            time: prev.time === time ? null : time,
        }))
    }


    const handleSubmit = async () => {
        if (!validateState(bookingState)) {
            return;
        }

        const booking: Omit<Booking, "id"> = {
            tableId: bookingState.table?.id || "",
            userId: user.userId,
            restaurantId: bookingState.restaurant?.id || "",
            startTime: `${bookingState.date}T${bookingState.time}:00.000Z`,
        };

        try {
            await services.booking.create(booking);
        } catch (error) {
            console.error(error);
            return;
        }

        setBookingState((prev) => ({ ...prev, table: null, time: null }));
        loadBaseData();
    };

    // State changes
    useEffect(() => {
        loadBaseData();
    }, [loadBaseData]);


    return {
        // Data
        restaurants,
        attributes,
        restaurantAttributes,
        tables,
        bookingSlots,

        // Handlers
        handleRestaurantChange,
        handleSeatsAmountChange,
        handleDateChange,
        handleAttributesChange,
        handleTableChange,
        handleTimeChange,
        handleSubmit,

        // Booking state
        bookingState,
        validateState,
    }

}