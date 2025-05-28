package co.edu.uptc.model;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.dtos.ReservationDTO;
import co.edu.uptc.dtos.SeatDTO;

public class Reservation {

    private int clientId;
    private Concert reservedConcert;
    private Zone reservedZone;
    private List<Seat> seats;
    private int quantityReserved;

    public Reservation(int clientId, Concert reservedConcert, Zone reservedZone, List<Seat> seats,
            int quantityReserved) {
        this.clientId = clientId;
        this.reservedConcert = reservedConcert;
        this.reservedZone = reservedZone;
        this.seats = seats;
        this.quantityReserved = quantityReserved;
    }

    public Reservation(int clientId, Concert reservedConcert, Zone reservedZone, int quantityReserved) {
        this.clientId = clientId;
        this.reservedConcert = reservedConcert;
        this.reservedZone = reservedZone;
        this.quantityReserved = quantityReserved;
    }

    public ReservationDTO toDTO() {
        List<SeatDTO> seatDTOs = null;
        if (seats != null) {
            seatDTOs = new ArrayList<>();
            for (Seat seat : seats) {
                seatDTOs.add(seat.toDTO());
            }
        }

        return new ReservationDTO(
                clientId,
                reservedConcert != null ? reservedConcert.toDTO() : null,
                reservedZone != null ? reservedZone.toDTO() : null,
                seatDTOs,
                quantityReserved);
    }

    public static Reservation fromDTO(ReservationDTO dto) {
        List<Seat> seatList = null;
        if (dto.getSeats() != null) {
            seatList = new ArrayList<>();
            for (SeatDTO seatDTO : dto.getSeats()) {
                seatList.add(Seat.fromDTO(seatDTO));
            }
        }

        Concert concert = dto.getConcert() != null ? Concert.fromDTO(dto.getConcert()) : null;
        Zone zone = dto.getReservedZone() != null ? Zone.fromDTO(dto.getReservedZone()) : null;

        return new Reservation(
                dto.getClientId(),
                concert,
                zone,
                seatList,
                dto.getQuantityReserved());
    }

    public Concert getReservedConcert() {
        return reservedConcert;
    }

    public void setReservedConcert(Concert reservedConcert) {
        this.reservedConcert = reservedConcert;
    }

    public Zone getReservedZone() {
        return reservedZone;
    }

    public void setReservedZone(Zone reservedZone) {
        this.reservedZone = reservedZone;
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