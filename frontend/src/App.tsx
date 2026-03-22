import "./App.css";
import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import { AppBar, Box, Toolbar, Typography } from "@mui/material";
import RestaurantView from "./views/RestaurantsView";
import BookingsView from "./views/BookingsView";
import NewBookingView from "./views/NewBookingView";

import { UserContext, ServiceContext, userContextData, serviceContextData } from "./context";

function App() {

  return (
    <div className="h-screen flex flex-col">
      <BrowserRouter>
        <Box>
          <AppBar position="static">
            <Toolbar className="gap-5">
              <Typography variant="h6" component="div">
                <Link to="/">Restaurants</Link>
              </Typography>
              <Typography variant="h6" component="div">
                <Link to="/bookings">Bookings</Link>
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
              <Route path="/" element={<RestaurantView />} />
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
