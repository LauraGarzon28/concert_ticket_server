package co.edu.uptc.dtos;

import java.io.Serializable;
import java.util.List;

public class ReservationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int clientId;
    private ConcertDTO concert;
    private ZoneDTO reservedZone;
    private List<SeatDTO> seats;
    private int quantityReserved;

    public ReservationDTO(int clientId, ConcertDTO concert, ZoneDTO reservedZone, List<SeatDTO> seats,
            int quantityReserved) {
        this.clientId = clientId;
        this.reservedZone = reservedZone;
        this.seats = seats;
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

    public ConcertDTO getConcert() {
        return concert;
    }

    public void setConcert(ConcertDTO concert) {
        this.concert = concert;
    }

    public ZoneDTO getReservedZone() {
        return reservedZone;
    }

    public void setReservedZone(ZoneDTO reservedZone) {
        this.reservedZone = reservedZone;
    }

}
