package co.edu.uptc.utils;

import java.io.FileReader;
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
            Reservation reservation = parseReservation(res.getAsJsonObject(), zones, concert);

            if (reservation.getReservedZone() instanceof SeatsZone seatsZone && reservation.getSeats() != null) {
                for (Seat seat : reservation.getSeats()) {
                    seatsZone.reserveSeat(seat.getRow(), seat.getColumn());
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

    private Reservation parseReservation(JsonObject obj, List<Zone> zones, Concert concert) {
        int clientId = obj.get("clientId").getAsInt();
        String zoneName = obj.get("zoneName").getAsString();
        int quantity = obj.get("quantityReserved").getAsInt();

        Zone zone = zones.stream().filter(z -> z.getName().equals(zoneName)).findFirst().orElse(null);

        if (zone instanceof SeatsZone && obj.has("seatPositions")) {
            List<Seat> seats = new ArrayList<>();
            JsonArray positions = obj.getAsJsonArray("seatPositions");
            for (JsonElement el : positions) {
                JsonObject seatObj = el.getAsJsonObject();
                seats.add(new Seat(seatObj.get("row").getAsInt(), seatObj.get("column").getAsInt()));
            }
            return new Reservation(clientId, concert, zone, seats);
        } else {
            return new Reservation(clientId, concert, zone, quantity);
        }
    }
}