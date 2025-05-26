
package core.controller;

import core.controller.utils.Response;
import core.controller.utils.Status;
import core.model.Flight;
import core.model.Location;
import core.model.Plane;
import core.model.storage.Storage;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class FlightController {

    public static Response createFlight(String idTxt, String planeId, String departureLocId, String scaleLocId, String arrivalLocId, String departureYear, String departureMonth,
                                        String departureDay, String departureHour, String departureMinute, String hoursDurationArrival, String minutesDurationArrival,
                                        String hoursDurationScale, String minutesDurationScale){
        Pattern pattern = Pattern.compile("^[A-z]{3}\\d{3}$");

        Location departureLoc = null;
        Location scaleLoc = null;
        Location arrivalLoc = null;
        Plane plane = null ;
        LocalDateTime departureDateTime;

        int hoursDurArrival;
        int minutesDurArrival;
        int hoursDurScale = 0;
        int minutesDurScale = 0;
        int deparYear;
        int deparMonth;
        int deparDay;
        int deparHour;
        int deparMinute;
        
        Storage storage = Storage.getInstance();

        ArrayList<Flight> flights = storage.getFlights();
        ArrayList<Plane> planes = storage.getPlanes();
        ArrayList<Location> departureLocationIdStorages = storage.getLocations();

        try {
            if (idTxt.equals("")) {
                return new Response("Error: ID must be not empty.", Status.BAD_REQUEST);
            }

            if (planeId.equals("")) {
                return new Response("Error: select a plane ID.", Status.BAD_REQUEST);
            }

            if (departureLocId.equals("")) {
                return new Response("Error: select a departure location ID.", Status.BAD_REQUEST);
            }

            if (scaleLocId.equals("")) {
                return new Response("Error: select a scale location ID.", Status.BAD_REQUEST);
            }

            if (arrivalLocId.equals("")) {
                return new Response("Error: select an arrival location ID.", Status.BAD_REQUEST);
            }

            if (departureYear.equals("")) {
                return new Response("Error: Year must be not empty.", Status.BAD_REQUEST);
            }

            if (departureMonth.equals("")) {
                return new Response("Error: select a departure month.", Status.BAD_REQUEST);
            }

            if (departureDay.equals("")) {
                return new Response("Error: select a departure day.", Status.BAD_REQUEST);
            }

            if (departureHour.equals("")) {
                return new Response("Error: select a departure hour.", Status.BAD_REQUEST);
            }

            if (departureMinute.equals("")) {
                return new Response("Error: select a departure minute.", Status.BAD_REQUEST);
            }

            if (hoursDurationArrival.equals("")) {
                return new Response("Error: select an hour for arrival duration", Status.BAD_REQUEST);
            }

            if (minutesDurationArrival.equals("")) {
                return new Response("Error: select minute for arrival duration.", Status.BAD_REQUEST);
            }

            // Validar formato de IDs
            if (!pattern.matcher(idTxt).matches()) {
                return new Response("Error: Invalid flight ID format found", Status.BAD_REQUEST);
            }

            for (Flight flight : flights) {
                if (flight.getId().equals(idTxt)) {
                    return new Response("Error: A flight with this ID is already registered.", Status.BAD_REQUEST);
                }
            }
            try {
                deparYear = Integer.parseInt(departureYear);
            } catch (NumberFormatException e) {
                return new Response("Error: The departure year must be numeric.", Status.BAD_REQUEST);
            }
            try {
                deparMonth = Integer.parseInt(departureMonth);
            } catch (NumberFormatException e) {
                return new Response("Error: Please select a departure month.", Status.BAD_REQUEST);
            }
            try {
                deparDay = Integer.parseInt(departureDay);
            } catch (NumberFormatException e) {
                return new Response("Error: Please select a day.", Status.BAD_REQUEST);
            }
            try {
                deparHour = Integer.parseInt(departureHour);
            } catch (NumberFormatException e) {
                return new Response("Error: Please select a hour.", Status.BAD_REQUEST);
            }
            try {
                deparMinute = Integer.parseInt(departureMinute);
            } catch (NumberFormatException e) {
                return new Response("Error: Please select a minute.", Status.BAD_REQUEST);
            }
            try {
                departureDateTime = LocalDateTime.of(deparYear, deparMonth, deparDay, deparHour, deparMinute);
            } catch (DateTimeException e) {
                return new Response("Error: Please select a valid date.", Status.BAD_REQUEST);
            }
            try {
                hoursDurArrival = Integer.parseInt(hoursDurationArrival);
            } catch (NumberFormatException e) {
                return new Response("Error: The Arrival Hour must be numeric.", Status.BAD_REQUEST);
            }
            try {
                minutesDurArrival = Integer.parseInt(minutesDurationArrival);
            } catch (NumberFormatException e) {
                return new Response("Error: The Arrival Minute must be numeric.", Status.BAD_REQUEST);
            }
            for (Plane plane1 : planes) {
                if (plane1.getId().equals(departureLocId)) {
                    plane = plane1;
                }

            }

            if (plane == null) {
                return new Response("Error: A plane with this ID is not registered.", Status.BAD_REQUEST);
            }

            for (Location location : departureLocationIdStorages) {
                if (location.getAirportId().equals(departureLocId)) {
                    departureLoc = location;
                }
                if (location.getAirportId().equals(scaleLocId)) {
                    scaleLoc = location;
                }
                if (location.getAirportId().equals(arrivalLoc)) {
                    arrivalLoc = location;
                }
            }
            if (departureLoc == null) {
                return new Response("Error: Location with the departureLocation ID is not registered.", Status.BAD_REQUEST);
            }

            if (arrivalLoc == null) {
                return new Response("Error: Location with the arrivalLocation ID is not registered.", Status.BAD_REQUEST);
            }
            Flight flight;
            if (scaleLoc == null & scaleLocId.equals("Location")) {
                flight = new Flight(idTxt, plane, departureLoc, arrivalLoc, departureDateTime, hoursDurArrival, minutesDurArrival);
                // validar que se siga el formato xxxyyy
                storage.addFlight(flight);
                if (flight.getPlane() != null) {
                    flight.getPlane().addFlight(flight);
                }
                return new Response("Plane added successfully", Status.OK);
            } else if (scaleLoc == null) {
                return new Response("Error: Location with the scaleLocation ID is not registered.", Status.BAD_REQUEST);
            } else {
                if (hoursDurationScale.equals("Hour")) {
                    return new Response("Error: select an hour for scale duration", Status.BAD_REQUEST);
                }

                if (minutesDurationScale.equals("Minute")) {
                    return new Response("Error: select minute for scale duration", Status.BAD_REQUEST);
                }
                try {
                    hoursDurScale = Integer.parseInt(hoursDurationScale);
                } catch (NumberFormatException e) {
                    return new Response("Error: The Scale Hour must be numeric.", Status.BAD_REQUEST);
                }
                try {
                    minutesDurScale = Integer.parseInt(minutesDurationScale);
                } catch (NumberFormatException e) {
                    return new Response("Error: The Scale Minute must be numeric.", Status.BAD_REQUEST);
                }
                flight = new Flight(idTxt, plane, departureLoc, scaleLoc, arrivalLoc, departureDateTime, hoursDurArrival, minutesDurArrival, hoursDurScale, minutesDurArrival);
                storage.addFlight(flight);
                if (flight.getPlane() != null) {
                    flight.getPlane().addFlight(flight);
                }
                return new Response("Plane added successfully", Status.OK);
            }

        } catch (NumberFormatException e) {
            return new Response("Error: Must be numeric", Status.INTERNAL_SERVER_ERROR);
        }

    }
    
    
    
}
