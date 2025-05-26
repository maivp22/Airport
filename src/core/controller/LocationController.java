
package core.controller;

import core.controller.utils.Response;
import core.controller.utils.Status;
import core.model.Location;
import core.model.storage.Storage;
import java.util.ArrayList;

public class LocationController {
    public static Response createLocation(String airportId, String airportName, String airportCity, String airportCountry, String airportLatitude, String airportLongitude) {
        double airportLat;
        double airportLong;
        
        
        Storage locationStorage = Storage.getInstance();
        ArrayList<Location> locations = locationStorage.getLocations();

        try {
            if (airportId.equals("")) {
                return new Response("Error: ID must be not empty.", Status.BAD_REQUEST);
            }

            if (airportName.equals("")) {
                return new Response("Error: Name must be not empty.", Status.BAD_REQUEST);
            }

            if (airportCity.equals("")) {
                return new Response("Error: City must be not empty.", Status.BAD_REQUEST);
            }

            if (airportCountry.equals("")) {
                return new Response("Error: Country must be not empty.", Status.BAD_REQUEST);
            }
            if (airportLatitude.equals("")) {
                return new Response("Error: Latitude must be not empty.", Status.BAD_REQUEST);
            }
            if (airportLongitude.equals("")) {
                return new Response("Error: Longitude must be not empty.", Status.BAD_REQUEST);
            }

            for (Location location : locations) {
                if (location.getAirportId().equals(airportId)) {
                    return new Response("Error: ID already exists.", Status.BAD_REQUEST);
                }
            }

            if (airportId.length() != 3) {
                return new Response("Error: ID must have 3 uppercase letters.", Status.BAD_REQUEST);
            }

            if (!airportId.equals(airportId.toUpperCase())) {
                return new Response("Error: ID must be uppercase.", Status.BAD_REQUEST);
            }

            try {
                airportLat = Double.parseDouble(airportLatitude);
                if (airportLat < -90 | airportLat > 90) {
                    return new Response("Error: Airport latitude must be in the range (-90,90).", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException e) {
                return new Response("Error: Airport latitude must be numeric.", Status.BAD_REQUEST);
            }

            try {
                airportLong = Double.parseDouble(airportLongitude);
                if (airportLong < -180 | airportLong > 180) {
                    return new Response("Error: Airport longitude must be in the range (-180,180).", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException e) {
                return new Response("Error: The airport longitude must be numeric.", Status.BAD_REQUEST);
            }
            airportLong = Double.parseDouble(airportLongitude);

            locationStorage.addLocation(new Location(airportId, airportName, airportCity, airportCountry, airportLat, airportLong));
            return new Response("Location added successfully", Status.OK);

        } catch (NumberFormatException e) {
            return new Response("Error: Bad Request", Status.INTERNAL_SERVER_ERROR);
        }
    }

}
