package co.edu.uptc.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import co.edu.uptc.model.Concert;
import co.edu.uptc.model.GeneralZone;
import co.edu.uptc.model.Reservation;
import co.edu.uptc.model.Seat;
import co.edu.uptc.model.SeatsZone;
import co.edu.uptc.model.Zone;

public class JsonService {

    public List<Concert> readJsonConcerts(String filePath) {
        List<Concert> concerts = new ArrayList<>();
        try (FileReader reader = new FileReader(filePath)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray concertsArray = jsonObject.getAsJsonArray("concerts");

            for (JsonElement element : concertsArray) {
                concerts.add(parseConcert(element.getAsJsonObject()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return concerts;
    }

    private Concert parseConcert(JsonObject obj) {
        String name = obj.get("name").getAsString();
        String place = obj.get("place").getAsString();
        String description = obj.get("description").getAsString();
        LocalDateTime dateHour = LocalDateTime.parse(obj.get("dateHour").getAsString());

        List<String> artists = parseArtists(obj.getAsJsonArray("artists"));
        List<Zone> zones = parseZones(obj.getAsJsonArray("zones"));

        Concert concert = new Concert(name, artists, dateHour, place, description, zones);

        JsonArray reservationsArray = obj.getAsJsonArray("reservations");
        for (JsonElement res : reservationsArray) {
            Reservation reservation = parseReservation(res.getAsJsonObject(), zones, name); // Usamos name

            if (reservation.getZoneName() != null) {
                Zone zone = zones.stream().filter(z -> z.getName().equals(reservation.getZoneName())).findFirst()
                        .orElse(null);
                if (zone instanceof SeatsZone seatsZone && reservation.getSeats() != null) {
                    for (Seat seat : reservation.getSeats()) {
                        seatsZone.reserveSeat(seat.getRow(), seat.getColumn());
                    }
                }
            }

            concert.getReservations().add(reservation);
        }
        return concert;
    }

    private List<String> parseArtists(JsonArray array) {
        List<String> artists = new ArrayList<>();
        array.forEach(el -> artists.add(el.getAsString()));
        return artists;
    }

    private List<Zone> parseZones(JsonArray zonesArray) {
        List<Zone> zones = new ArrayList<>();
        for (JsonElement element : zonesArray) {
            JsonObject zoneObj = element.getAsJsonObject();
            String type = zoneObj.get("type").getAsString();

            if (type.equals("GeneralZone")) {
                zones.add(parseGeneralZone(zoneObj));
            } else if (type.equals("SeatsZone")) {
                zones.add(parseSeatsZone(zoneObj));
            }
        }
        return zones;
    }

    private GeneralZone parseGeneralZone(JsonObject zoneObj) {
        String name = zoneObj.get("name").getAsString();
        double price = zoneObj.get("price").getAsDouble();
        String description = zoneObj.get("description").getAsString();
        boolean hasSeats = zoneObj.get("hasSeats").getAsBoolean();
        int capacity = zoneObj.get("maxCapacity").getAsInt();
        int reserved = zoneObj.get("quantityReserved").getAsInt();

        GeneralZone zone = new GeneralZone(name, price, description, hasSeats, capacity);
        zone.setQuantityReserved(reserved);
        return zone;
    }

    private SeatsZone parseSeatsZone(JsonObject zoneObj) {
        String name = zoneObj.get("name").getAsString();
        double price = zoneObj.get("price").getAsDouble();
        String description = zoneObj.get("description").getAsString();
        boolean hasSeats = zoneObj.get("hasSeats").getAsBoolean();
        int rows = zoneObj.get("numberRows").getAsInt();
        int cols = zoneObj.get("numberColumns").getAsInt();

        SeatsZone zone = new SeatsZone(name, price, description, hasSeats, rows, cols);

        JsonArray matrix = zoneObj.getAsJsonArray("seats");
        for (int i = 0; i < rows; i++) {
            JsonArray row = matrix.get(i).getAsJsonArray();
            for (int j = 0; j < cols; j++) {
                if (row.get(j).getAsBoolean()) {
                    zone.reserveSeat(i, j);
                }
            }
        }

        return zone;
    }

    private Reservation parseReservation(JsonObject obj, List<Zone> zones, String concertName) {
        int clientId = obj.get("clientId").getAsInt();
        String zoneName = obj.get("zoneName").getAsString();
        int quantity = obj.get("quantityReserved").getAsInt();

        if (obj.has("seatPositions")) {
            List<Seat> seats = new ArrayList<>();
            JsonArray positions = obj.getAsJsonArray("seatPositions");
            for (JsonElement el : positions) {
                JsonObject seatObj = el.getAsJsonObject();
                seats.add(new Seat(seatObj.get("row").getAsInt(), seatObj.get("column").getAsInt()));
            }
            return new Reservation(clientId, concertName, zoneName, seats);
        } else {
            return new Reservation(clientId, concertName, zoneName, quantity);
        }
    }

    private JsonObject concertToJson(Concert c) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", c.getName());
        obj.addProperty("place", c.getPlace());
        obj.addProperty("description", c.getDescription());
        obj.addProperty("dateHour", c.getDateHour().toString());

        JsonArray artists = new JsonArray();
        for (String artist : c.getArtists()) {
            artists.add(artist);
        }
        obj.add("artists", artists);

        JsonArray zonesArray = new JsonArray();
        for (Zone z : c.getZones()) {
            if (z instanceof GeneralZone gz) {
                JsonObject gzObj = new JsonObject();
                gzObj.addProperty("type", "GeneralZone");
                gzObj.addProperty("name", gz.getName());
                gzObj.addProperty("price", gz.getPrice());
                gzObj.addProperty("description", gz.getDescription());
                gzObj.addProperty("hasSeats", gz.hasSeats());
                gzObj.addProperty("maxCapacity", gz.getMaxCapacity());
                gzObj.addProperty("quantityReserved", gz.getQuantityReserved());
                zonesArray.add(gzObj);
            } else if (z instanceof SeatsZone sz) {
                JsonObject szObj = new JsonObject();
                szObj.addProperty("type", "SeatsZone");
                szObj.addProperty("name", sz.getName());
                szObj.addProperty("price", sz.getPrice());
                szObj.addProperty("description", sz.getDescription());
                szObj.addProperty("hasSeats", sz.hasSeats());
                szObj.addProperty("numberRows", sz.getRows());
                szObj.addProperty("numberColumns", sz.getColumns());

                JsonArray seatMatrix = new JsonArray();
                for (int i = 0; i < sz.getRows(); i++) {
                    JsonArray row = new JsonArray();
                    for (int j = 0; j < sz.getColumns(); j++) {
                        row.add(sz.getSeat(i, j).isReserved());
                    }
                    seatMatrix.add(row);
                }
                szObj.add("seats", seatMatrix);
                zonesArray.add(szObj);
            }
        }
        obj.add("zones", zonesArray);

        JsonArray reservationsArray = new JsonArray();
        for (Reservation r : c.getReservations()) {
            JsonObject rObj = new JsonObject();
            rObj.addProperty("clientId", r.getClientId());
            rObj.addProperty("zoneName", r.getZoneName()); // antes: r.getReservedZone().getName()
            rObj.addProperty("quantityReserved", r.getQuantityReserved());

            if (r.getSeats() != null && !r.getSeats().isEmpty()) {
                JsonArray seatsArr = new JsonArray();
                for (Seat s : r.getSeats()) {
                    JsonObject sObj = new JsonObject();
                    sObj.addProperty("row", s.getRow());
                    sObj.addProperty("column", s.getColumn());
                    seatsArr.add(sObj);
                }
                rObj.add("seatPositions", seatsArr);
            }
            reservationsArray.add(rObj);
        }
        obj.add("reservations", reservationsArray);

        return obj;
    }

    public void appendConcertToFile(String filePath, Concert concert) {
        try {
            JsonObject root;
            File file = new File(filePath);
            if (file.exists()) {
                try (FileReader reader = new FileReader(file)) {
                    root = JsonParser.parseReader(reader).getAsJsonObject();
                }
            } else {
                root = new JsonObject();
                root.add("concerts", new JsonArray());
            }

            JsonArray concertsArray = root.getAsJsonArray("concerts");
            concertsArray.add(concertToJson(concert));

            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(root.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeConcertsToFile(String filePath, List<Concert> concerts) {
        JsonObject root = new JsonObject();
        JsonArray concertsArray = new JsonArray();

        for (Concert concert : concerts) {
            concertsArray.add(concertToJson(concert));
        }

        root.add("concerts", concertsArray);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(root.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}