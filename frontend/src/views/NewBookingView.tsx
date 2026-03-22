import { useState, useEffect, useMemo, useCallback } from "react";
import {
  Box,
  Typography,
  TextField,
  MenuItem,
  Button,
  Checkbox,
  FormGroup,
  FormControlLabel,
  Divider,
} from "@mui/material";
import { Restaurant, Attribute, BookingSlot, Table, Booking } from "../dto";
import {
  RestaurantService,
  AttributeService,
  BookingSlotService,
  TableService,
  BookingService,
} from "../service";
import { INewBookingState } from "../interface/INewBookingState";
import { Converter, TableAdvisor, TimeFinder } from "../utils";
import { TableButton, AttributeCard } from "../component";

export default function NewBookingView() {
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

  const [selectionState, setSelectionState] = useState<INewBookingState>({
    restaurant: null,
    table: null,
    date: null,
    time: null,
    seatsAmount: 2,
    attributes: [],
  });

  const [readyToSubmit, setReadyToSubmit] = useState<boolean>(false);

  const services = useMemo(
    () => ({
      restaurant: new RestaurantService(),
      attribute: new AttributeService(),
      table: new TableService(),
      bookingSlot: new BookingSlotService(),
      booking: new BookingService(),
    }),
    [],
  );

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

      setSelectionState((prev) => ({
        ...prev,
        restaurant,
        date,
      }));
    } catch (error) {
      console.error("Failed to load booking data", error);
    }
  }, [services]);

  useEffect(() => {
    loadBaseData();
  }, [loadBaseData]);

  const validateState = (state: INewBookingState): boolean => {
    return state.restaurant &&
      state.date &&
      state.seatsAmount &&
      state.table &&
      state.time
      ? true
      : false;
  };

  useEffect(() => {
    if (validateState(selectionState)) {
      setReadyToSubmit(true);
    } else {
      setReadyToSubmit(false);
    }

    for (let table of Array.from(tables.values())) {
      console.log(
        "Table",
        table.tableNumber,
        "Score",
        TableAdvisor.getScore(table, selectionState),
      );
    }
  }, [selectionState, tables]);

  const updateBookingSlots = async (
    restaurantId: string | null,
    date: string | null,
  ) => {
    if (!restaurantId || !date) {
      return;
    }

    const bookingSlots = await services.bookingSlot.getForRestaurant(
      restaurantId,
      date,
    );
    setBookingSlots(bookingSlots);
  };

  const updateRestaurantAttributes = (tables: Table[]) => {
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
  };

  const handleRestaurantChange = async (id: string) => {
    const selected = restaurants.get(id) ?? null;

    const tables = await services.table.getRestaurantTables(id);
    setTables(Converter.toMap(tables));

    updateBookingSlots(id, selectionState.date);
    updateRestaurantAttributes(tables);

    setSelectionState((prev) => ({
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

    updateBookingSlots(selectionState.restaurant?.id ?? null, stringDate);

    setSelectionState((prev) => ({
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

    setSelectionState((prev) => ({
      ...prev,
      seatsAmount: seatsAmountNumber,
    }));
  };

  const handleAttributesChange = (attributeId: string): void => {
    const attributes = selectionState.attributes;

    if (attributes.includes(attributeId)) {
      setSelectionState((prev) => ({
        ...prev,
        attributes: attributes.filter((a) => a !== attributeId),
      }));
    } else {
      setSelectionState((prev) => ({
        ...prev,
        attributes: [...attributes, attributeId],
      }));
    }
  };

  const handleSubmit = async () => {
    if (!validateState(selectionState)) {
      return;
    }

    const booking: Omit<Booking, "id"> = {
      tableId: selectionState.table?.id || "",
      userId: "f04b862d-eb29-4fd6-8ada-b882e71b2b26",
      restaurantId: selectionState.restaurant?.id || "",
      startTime: `${selectionState.date}T${selectionState.time}:00.000Z`,
    };

    const response = await services.booking.create(booking);
    console.log(response);

    setSelectionState((prev) => ({ ...prev, table: null, time: null }));
    loadBaseData();
  };

  return (
    <>
      {/* 2. MAIN BODY */}
      <main className="flex flex-1 overflow-hidden">
        {/* LEFT SIDEBAR */}
        <aside className="w-1/4 p-6 border-r bg-white overflow-y-hidden hidden lg:block">
          <Typography variant="subtitle2" className="text-gray-500 pb-4">
            Filters
          </Typography>

          <div className="space-y-4">
            <TextField
              select
              label="Restaurant"
              fullWidth
              size="small"
              value={selectionState.restaurant?.id ?? ""}
              onChange={(e) => handleRestaurantChange(e.target.value)}
            >
              {Array.from(restaurants.values()).map((r) => (
                <MenuItem key={r.id} value={r.id}>
                  {r.name}
                </MenuItem>
              ))}
            </TextField>

            <TextField
              type="date"
              label="Date"
              fullWidth
              size="small"
              value={selectionState.date ?? ""}
              onChange={(e) => handleDateChange(e.target.value)}
            />

            <TextField
              type="number"
              label="Number of people"
              fullWidth
              size="small"
              value={selectionState.seatsAmount ?? ""}
              onChange={(e) => handleSeatsAmountChange(e.target.value)}
            />
          </div>
          <Divider className="py-2" />
          <Typography variant="subtitle2" className="text-gray-500 pt-3">
            Attributes
          </Typography>
          <Box className="overflow-y-scroll">
            <FormGroup>
              {Array.from(restaurantAttributes.values()).map((a) => (
                <FormControlLabel
                  key={a.id}
                  control={
                    <Checkbox
                      checked={selectionState.attributes.includes(a.id)}
                      onChange={() => handleAttributesChange(a.id)}
                    />
                  }
                  label={a.name}
                />
              ))}
            </FormGroup>
          </Box>
        </aside>

        {/* CENTER COLUMN: Table Selection */}
        <section className="flex-1 p-6 overflow-hidden flex-wrap">
          <div className="flex justify-between items-center mb-6">
            <Typography variant="h5" className="font-bold text-slate-800">
              Available Tables
            </Typography>
          </div>

          {/* Responsive Grid of Cards */}
          {selectionState.restaurant === null ||
          selectionState.date === null ? (
            <div>Select a restaurant first</div>
          ) : (
            <div className="flex gap-4">
              {Array.from(tables.values()).map((t) => (
                <TableButton
                  key={t.id}
                  table={t}
                  isSelected={selectionState.table?.id === t.id}
                  available={Array.from(bookingSlots.keys()).includes(t.id)}
                  color={
                    TableAdvisor.getScore(t, selectionState) >= 0.5
                      ? "success"
                      : "error"
                  }
                  onSelect={(tableId) =>
                    setSelectionState((prev) => ({
                      ...prev,
                      table: tables.get(tableId) ?? null,
                      time: null,
                    }))
                  }
                />
              ))}
            </div>
          )}
        </section>

        {/* RIGHT SIDEBAR: Action Zone */}
        <aside className="w-1/4 p-6 border-l bg-white hidden xl:block">
          <Typography variant="h6" className="font-bold mb-4">
            Booking details
          </Typography>

          {selectionState.table ? (
            <div className="flex flex-col h-full">
              <Box>
                <Typography>
                  Table: {selectionState.table.tableNumber}
                </Typography>
                <Typography>
                  Number of seats: {selectionState.table.seatsAmount}
                </Typography>

                <Box className="flex gap-1 pt-2 flex-wrap">
                  {selectionState.table.attributes.map((attributeId) => (
                    <AttributeCard
                      key={attributeId}
                      name={attributes.get(attributeId)?.name ?? "ERROR"}
                      isSelected={selectionState.attributes.includes(
                        attributeId,
                      )}
                      isMissing={
                        !selectionState.attributes.includes(attributeId)
                      }
                    />
                  ))}
                </Box>
              </Box>
              <div className="flex flex-col min-h-0 mt-4 flex-1">
                <Typography variant="subtitle2" className="text-gray-500">
                  Select Time
                </Typography>
                <div className="grid grid-cols-2 gap-2 overflow-y-auto flex-1 auto-rows-min">
                  {TimeFinder.getStartTimes(
                    bookingSlots.get(selectionState.table.id) ?? [],
                  ).map((time) => (
                    <Button
                      key={time}
                      variant={
                        selectionState.time === time ? "contained" : "outlined"
                      }
                      size="small"
                      onClick={() =>
                        setSelectionState((prev) => ({
                          ...prev,
                          time: prev.time === time ? null : time,
                        }))
                      }
                    >
                      {time}
                    </Button>
                  ))}
                </div>
              </div>
              <div className="pt-2 pb-5">
                <Button
                  variant="contained"
                  fullWidth
                  size="large"
                  className="m-4 py-20"
                  disabled={!readyToSubmit}
                  onClick={() => handleSubmit()}
                >
                  Confirm Booking
                </Button>
              </div>
            </div>
          ) : (
            <div className="text-center mt-20 text-gray-400">
              <Typography>Select a table to see available times</Typography>
            </div>
          )}
        </aside>
      </main>
    </>
  );
}
