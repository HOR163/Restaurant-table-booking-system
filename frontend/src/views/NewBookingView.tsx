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
import { TableAdvisor, TimeFinder } from "../utils";
import { TableButton, AttributeCard } from "../component";

import { useNewBooking } from "../hook/useNewBooking";

export default function NewBookingView() {
  const {
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
  } = useNewBooking();

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
              value={bookingState.restaurant?.id ?? ""}
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
              value={bookingState.date ?? ""}
              onChange={(e) => handleDateChange(e.target.value)}
            />

            <TextField
              type="number"
              label="Number of people"
              fullWidth
              size="small"
              value={bookingState.seatsAmount ?? ""}
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
                      checked={bookingState.attributes.includes(a.id)}
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
          {bookingState.restaurant === null || bookingState.date === null ? (
            <div>Select a restaurant first</div>
          ) : (
            <div className="flex gap-4">
              {Array.from(tables.values()).map((t) => (
                <TableButton
                  key={t.id}
                  table={t}
                  isSelected={bookingState.table?.id === t.id}
                  available={Array.from(bookingSlots.keys()).includes(t.id)}
                  color={
                    TableAdvisor.getScore(t, bookingState) >= 0.5
                      ? "success"
                      : "error"
                  }
                  onSelect={(tableId) => handleTableChange(tableId)}
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

          {bookingState.table ? (
            <div className="flex flex-col h-full">
              <Box>
                <Typography>Table: {bookingState.table.tableNumber}</Typography>
                <Typography>
                  Number of seats: {bookingState.table.seatsAmount}
                </Typography>

                <Box className="flex gap-1 pt-2 flex-wrap">
                  {bookingState.table.attributes.map((attributeId) => (
                    <AttributeCard
                      key={attributeId}
                      name={attributes.get(attributeId)?.name ?? "ERROR"}
                      isSelected={bookingState.attributes.includes(attributeId)}
                      isMissing={!bookingState.attributes.includes(attributeId)}
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
                    bookingSlots.get(bookingState.table.id) ?? [],
                  ).map((time) => (
                    <Button
                      key={time}
                      variant={
                        bookingState.time === time ? "contained" : "outlined"
                      }
                      size="small"
                      onClick={() => handleTimeChange(time)}
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
                  disabled={!validateState(bookingState)}
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
