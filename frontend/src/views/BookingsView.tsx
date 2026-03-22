import { useCallback, useContext, useEffect, useState } from "react";
import { ServiceContext, UserContext } from "../context";
import { Booking, Restaurant, Table } from "../dto";
import { Converter } from "../utils";
import { Typography } from "@mui/material";

export default function BookingsView() {
  const services = useContext(ServiceContext);
  const user = useContext(UserContext);

  const [bookings, setBookings] = useState<Booking[]>([]);
  const [restaurants, setRestaurants] = useState<Map<string, Restaurant>>(
    new Map(),
  );
  const [tables, setTables] = useState<Map<string, Table>>(new Map());

  const getBookings = useCallback(async () => {
    const currentTime = new Date();
    const viewEndTime = new Date(currentTime);
    viewEndTime.setMonth(viewEndTime.getMonth() + 3);

    try {
      const loadedBookings = await services.booking.getUserBookings(
        user.userId,
        currentTime.toISOString(),
        viewEndTime.toISOString(),
      );

      loadedBookings.sort((a, b) => dateComparator(new Date(a.startTime), new Date(b.startTime)));

      setBookings(loadedBookings);

      const loadedRestaurants = await services.restaurant.getAll();
      setRestaurants(Converter.toMap(loadedRestaurants));

      const restaurantIds = new Set<string>();
      const loadedTables = new Map<string, Table>();

      for (const booking of loadedBookings) {
        const restaurantId = booking.restaurantId;
        if (restaurantIds.has(restaurantId)) {
          continue;
        }
        restaurantIds.add(restaurantId);
        try {
          const newTables =
            await services.table.getRestaurantTables(restaurantId);
          for (const table of newTables) {
            loadedTables.set(table.id, table);
          }
        } catch (error) {
          console.error(
            `Error getting tables from restaurant ${restaurantId} ${error}`,
          );
        }
      }
      setTables(loadedTables);
    } catch (error) {
      console.error("Error retreiving user bookings:", error);
    }
  }, [services, user]);

  const dateComparator = (date1: Date, date2: Date): number => {
    if (date1 < date2) {
      return -1;
    } else if (date1 > date2) {
      return 1;
    }
    return 0;
  }


  const getStartTimeAndDate = (
    dateTime: string,
  ): { date: string; time: string } => {
    const dateTimeObject = new Date(dateTime);

    return {
      date: `${dateTimeObject.getUTCFullYear()}.${(dateTimeObject.getUTCMonth() + 1).toString().padStart(2, "0")}.${dateTimeObject.getUTCDate().toString().padStart(2, "0")}`,
      time: `${dateTimeObject.getUTCHours().toString().padStart(2, "0")}.${dateTimeObject.getUTCMinutes().toString().padStart(2, "0")}`,
    };
  };

  useEffect(() => {
    getBookings();
  }, [getBookings]);

  return (
    <div className="flex justify-center overflow-y-scroll py-5">
      <div className="flex flex-col gap-2 min-w-[600px]">
        {bookings.map((b) => (
          <div className="flex w-full  bg-gray-200 p-2 rounded-lg items-center">
            <div className="flex-shrink">
              <Typography variant="subtitle2">
                {getStartTimeAndDate(b.startTime)["date"]}
              </Typography>
              <Typography variant="subtitle2">
                {getStartTimeAndDate(b.startTime)["time"]}
              </Typography>
            </div>
            <div className="flex-1 flex justify-center">
              <Typography className="px-4"  variant="subtitle1">{restaurants.get(b.restaurantId)?.name}</Typography>
            </div>
            <div>
              <Typography className="px-4" variant="subtitle2">Seats: {tables.get(b.tableId)?.seatsAmount}</Typography>
              <Typography className="px-4" variant="subtitle2">Table: {tables.get(b.tableId)?.tableNumber}</Typography>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
