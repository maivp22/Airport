
package core.controller;

import core.controller.utils.Response;
import core.controller.utils.Status;
import core.model.Flight;
import core.model.Passenger;
import core.model.storage.Storage;
import java.util.regex.Pattern;

public class AddFlightController {
    public static Response addToFlight(String id, String flightId) {
    Pattern pattern = Pattern.compile("^[A-z]{3}\\d{3}$");
    Storage storage = Storage.getInstance();

    try {
        if (flightId.isBlank()) 
            return new Response("Error: ID Flight must be not empty.", Status.BAD_REQUEST);

        if (!pattern.matcher(flightId).matches()) 
            return new Response("Error: Invalid flight ID format found", Status.BAD_REQUEST);

        long idUser = Long.parseLong(id);

        Passenger passenger = null;
        
        for(Passenger p : storage.getPassengers()){
            if(p.getId() == idUser) passenger = p;
        }

        if (passenger == null)
            return new Response("Error: Passenger not found", Status.NOT_FOUND);

        Flight flight = null;
        
        for(Flight f : storage.getFlights()){
            if(f.getId().equals(flightId)) flight = f;
        }

        if (flight == null)
            return new Response("Error: Flight not found", Status.NOT_FOUND);

        flight.addPassenger(passenger);
        passenger.addFlight(flight);

        return new Response("Passenger added to flight successfully", Status.OK);

    } catch (NumberFormatException e) {
        return new Response("Error: ID must be numeric", Status.BAD_REQUEST);
    } catch (Exception e) {
        return new Response("Internal server error", Status.INTERNAL_SERVER_ERROR);
    }
}

}
