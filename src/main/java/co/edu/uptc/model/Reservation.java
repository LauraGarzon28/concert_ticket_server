package co.edu.uptc.model;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.dtos.ReservationDTO;
import co.edu.uptc.dtos.SeatDTO;

public class Reservation {

    private int clientId;
    private String concertName;
    private String zoneName;
    private List<Seat> seats;
    private int quantityReserved;

    public Reservation(int clientId, String concertName, String zoneName, List<Seat> seats) {
        this.clientId = clientId;
        this.concertName = concertName;
        this.zoneName = zoneName;
        this.seats = seats;
    }

    public Reservation(int clientId, String concertName, String zoneName, int quantityReserved) {
        this.clientId = clientId;
        this.concertName = concertName;
        this.zoneName = zoneName;
        this.quantityReserved = quantityReserved;
    }

    public ReservationDTO toDTO() {
        if (seats != null) {
            List<SeatDTO> seatDTOs = new ArrayList<>();
            for (Seat seat : seats) {
                seatDTOs.add(seat.toDTO());
            }
            return new ReservationDTO(
                    clientId,
                    concertName,
                    zoneName,
                    seatDTOs);
        } else {
            return new ReservationDTO(
                    clientId,
                    concertName,
                    zoneName,
                    quantityReserved);
        }
    }

    public static Reservation fromDTO(ReservationDTO dto) {
        if (dto.getSeats() != null) {
            List<Seat> seatList = new ArrayList<>();
            for (SeatDTO seatDTO : dto.getSeats()) {
                seatList.add(Seat.fromDTO(seatDTO));
            }
            return new Reservation(
                    dto.getClientId(),
                    dto.getConcertName(),
                    dto.getZoneName(),
                    seatList);
        } else {
            return new Reservation(
                    dto.getClientId(),
                    dto.getConcertName(),
                    dto.getZoneName(),
                    dto.getQuantityReserved());
        }
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

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public int getQuantityReserved() {
        return quantityReserved;
    }

    public void setQuantityReserved(int quantityReserved) {
        this.quantityReserved = quantityReserved;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

}