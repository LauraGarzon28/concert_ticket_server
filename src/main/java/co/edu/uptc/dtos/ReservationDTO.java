package co.edu.uptc.dtos;

import java.io.Serializable;
import java.util.List;

public class ReservationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int clientId;
    private String concertName;
    private String zoneName;
    private List<SeatDTO> seats;
    private int quantityReserved;

    public ReservationDTO(int clientId, String concertName, String zoneName, List<SeatDTO> seats) {
        this.clientId = clientId;
        this.concertName = concertName;
        this.zoneName = zoneName;
        this.seats = seats;
    }

    public ReservationDTO(int clientId, String concertName, String zoneName, int quantityReserved) {
        this.clientId = clientId;
        this.concertName = concertName;
        this.zoneName = zoneName;
        this.quantityReserved = quantityReserved;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public List<SeatDTO> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatDTO> seats) {
        this.seats = seats;
    }

    public int getQuantityReserved() {
        return quantityReserved;
    }

    public void setQuantityReserved(int quantityReserved) {
        this.quantityReserved = quantityReserved;
    }

    public String getConcertName() {
        return concertName;
    }

    public void setConcertName(String concertName) {
        this.concertName = concertName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

}
