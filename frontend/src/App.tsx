import "./App.css";
import { BrowserRouter, Routes, Route, Link, Navigate } from "react-router-dom";
import { AppBar, Box, Toolbar, Typography } from "@mui/material";
import BookingsView from "./views/BookingsView";
import NewBookingView from "./views/NewBookingView";

import {
  UserContext,
  ServiceContext,
  userContextData,
  serviceContextData,
} from "./context";

function App() {
  return (
    <div className="h-screen flex flex-col">
      <BrowserRouter>
        <Box>
          <AppBar position="static">
            <Toolbar className="gap-5">
              <Typography variant="h6" component="div">
                <Link to="/bookings">My bookings</Link>
              </Typography>
              <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                <Link to="/newBooking">New Booking</Link>
              </Typography>
            </Toolbar>
          </AppBar>
        </Box>

        <UserContext value={userContextData}>
          <ServiceContext value={serviceContextData}>
            <Routes>
              <Route index element={<Navigate to="/newBooking" replace />} />
              <Route path="/bookings" element={<BookingsView />} />
              <Route path="/newBooking" element={<NewBookingView />} />
            </Routes>
          </ServiceContext>
        </UserContext>
      </BrowserRouter>
    </div>
  );
}

export default App;
