package co.edu.uptc.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
public class ConcertDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private List<String> artists;
    private LocalDateTime dateHour;
    private String place;
    private String description;
    private List<ZoneDTO> zones;
    private List<ReservationDTO> reservations;

    public ConcertDTO(String name, List<String> artists, LocalDateTime dateHour, String place, String description,
            List<ZoneDTO> zones, List<ReservationDTO> reservations) {
        this.name = name;
        this.artists = artists;
        this.dateHour = dateHour;
        this.place = place;
        this.description = description;
        this.zones = zones;
        this.reservations = reservations;
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

    public List<ZoneDTO> getZones() {
        return zones;
    }

    public void setZones(List<ZoneDTO> zones) {
        this.zones = zones;
    }

    public List<ReservationDTO> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationDTO> reservations) {
        this.reservations = reservations;
    } 

}
