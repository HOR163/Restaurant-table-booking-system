import './App.css';
import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import RestaurantView from './views/RestaurantsView';
import BookingsView from './views/BookingsView';
import NewBookingView from './views/NewBookingView';
import { AppBar, Box, Toolbar, Typography } from '@mui/material';

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

        <Routes>
          <Route path="/" element={<RestaurantView />} />
          <Route path="/bookings" element={<BookingsView />} />
          <Route path="/newBooking" element={<NewBookingView />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
