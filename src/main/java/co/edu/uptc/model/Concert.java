package co.edu.uptc.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.dtos.ConcertDTO;
import co.edu.uptc.dtos.ReservationDTO;
import co.edu.uptc.dtos.ZoneDTO;public class Concert {

    private String name;
    private List<String> artists;
    private LocalDateTime dateHour;
    private String place;
    private String description;
    private List<Zone> zones;
    private List<Reservation> reservations;

    public Concert(String name, List<String> artists, LocalDateTime dateHour, String place, String description,
            List<Zone> zones) {
        this.name = name;
        this.artists = artists;
        this.dateHour = dateHour;
        this.place = place;
        this.description = description;
        this.zones = (zones != null) ? zones : new ArrayList<>();
        this.reservations = (reservations != null) ? reservations : new ArrayList<>();
    }

    public Concert() {}

     public Zone getZoneByName(String zoneName) {
        for (Zone zone : zones) {
            if (zone.getName().equalsIgnoreCase(zoneName)) {
                return zone;
            }
        }
        return null;
    }

    public void addZone(Zone zone) {
        this.zones.add(zone);
    }

    public boolean removeZone(String zoneName) {
        return this.zones.removeIf(zone -> zone.getName().equalsIgnoreCase(zoneName));
    }
    
    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
    }

    public void removeReservation(Reservation reservation) {
        this.reservations.remove(reservation);
    }

    public ConcertDTO toDTO() {
        List<ZoneDTO> zoneDTOs = new ArrayList<>();
        for (Zone zone : zones) {
            zoneDTOs.add(zone.toDTO());
        }
        List<ReservationDTO> reservationDTOs = new ArrayList<>();
        for (Reservation reservation : reservations) {
            reservationDTOs.add(reservation.toDTO());
        }
        return new ConcertDTO(
                name,
                artists,
                dateHour,
                place,
                description,
                zoneDTOs,
                reservationDTOs);
    }

    public static Concert fromDTO(ConcertDTO dto) {
        List<Zone> zoneList = new ArrayList<>();
        for (ZoneDTO zoneDTO : dto.getZones()) {
            zoneList.add(Zone.fromDTO(zoneDTO));
        }
        Concert concert = new Concert(
                dto.getName(),
                dto.getArtists(),
                dto.getDateHour(),
                dto.getPlace(),
                dto.getDescription(),
                zoneList);

        for (ReservationDTO resDTO : dto.getReservations()) {
            concert.addReservation(Reservation.fromDTO(resDTO));
        }
        return concert;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getArtists() {
        return artists;
    }

    public void setArtists(List<String> artists) {
        this.artists = artists;
    }

    public LocalDateTime getDateHour() {
        return dateHour;
    }

    public void setDateHour(LocalDateTime dateHour) {
        this.dateHour = dateHour;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Zone> getZones() {
        return zones;
    }

    public void setZones(List<Zone> zones) {
        this.zones = zones;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

}
