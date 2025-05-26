
package core.controller;

import core.controller.utils.Response;
import core.controller.utils.Status;
import core.model.Plane;
import core.model.storage.Storage;
import java.util.ArrayList;
import java.util.regex.Pattern;


public class PlaneController {
        public static Response createPlane(String id, String brand, String model, String maxCapacity, String airline) {
        Pattern ID_PATTERN = Pattern.compile("^[A-Z]{2}\\d{5}$");
        int maxCapacity1;
        Storage planeStorage = Storage.getInstance();
        ArrayList<Plane> planes = planeStorage.getPlanes();

        try {
            if (id.equals("")) {
                return new Response("Error: ID must be not empty.", Status.BAD_REQUEST);
            }

            if (brand.equals("")) {
                return new Response("Error: Brand must be not empty.", Status.BAD_REQUEST);
            }

            if (model.equals("")) {
                return new Response("Error: Model must be not empty.", Status.BAD_REQUEST);
            }

            if (maxCapacity.equals("")) {
                return new Response("Error: Max Capacity must be not empty.", Status.BAD_REQUEST);
            }
            if (airline.equals("")) {
                return new Response("Error: Airline must be not empty.", Status.BAD_REQUEST);
            }

            for (Plane plane : planes) {
                if (plane.getId().equals(id)) {
                    return new Response("Error: Plane ID already exists.", Status.BAD_REQUEST);
                }
            }

            if (!ID_PATTERN.matcher(id).matches()) {
                return new Response("Error: Invalid plane ID format found.", Status.BAD_REQUEST);
            }

            try {
                maxCapacity1 = Integer.parseInt(maxCapacity);
            } catch (NumberFormatException e) {
                return new Response("Error: The maximum Capacity must be numeric.", Status.BAD_REQUEST);
            }

            planeStorage.addPlane(new Plane(id, brand, model, maxCapacity1, airline));
            return new Response("Plane added successfully", Status.OK);

        } catch (NumberFormatException e) {
            return new Response("Error: Bad Request", Status.INTERNAL_SERVER_ERROR);
        }
    }

}
