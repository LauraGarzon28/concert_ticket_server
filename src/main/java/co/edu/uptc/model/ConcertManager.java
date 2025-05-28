package co.edu.uptc.model;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.dtos.ConcertDTO;
import co.edu.uptc.dtos.ReservationDTO;
import co.edu.uptc.dtos.SeatDTO;
import co.edu.uptc.dtos.ZoneDTO;
import co.edu.uptc.structures.BinaryTree;
import co.edu.uptc.utils.JsonService;

public class ConcertManager {

    private BinaryTree<Concert> concerts;
    private JsonService jsonService;

    public ConcertManager() {
        this.concerts = new BinaryTree<>((concert1, concert2) -> concert1.getName().compareTo(concert2.getName()));
        this.jsonService = new JsonService();
        loadConcerts();
    }

    private void loadConcerts() {
        List<Concert> concertsFromFile = jsonService.readJsonConcerts("./data/concerts.json");
        if (concertsFromFile != null) {
            for (Concert concert : concertsFromFile) {
                concerts.add(concert);
            }
        }
    }

    public Concert searchConcert(String name) {
        Concert concert = new Concert();
        concert.setName(name); 
        return this.concerts.exist(concert);
    }

    public synchronized void removeConcert(String name) {
        Concert concert = new Concert();
        concert.setName(name); 
        this.concerts.delete(concert);
    }

    public synchronized boolean createConcert(Concert concert) {
        return concerts.add(concert);
    }

    public synchronized boolean updateConcert(Concert concert) {
        if (concert != null) {
            concerts.delete(concert);
            concerts.add(concert);
            return true;
        }
        return false;
    }

    public Concert searchConcert(Concert concert) {
        return concerts.exist(concert);
    }

    public List<Concert> getAllConcerts() {
        return concerts.in_order();
    }

    public synchronized void addReservation(Concert concert, Reservation reservation) {
        if (concert != null && reservation != null) {
            concert.addReservation(reservation);
        }
    }

    public List<ConcertDTO> toDTO(List<Concert> concerts) {
        List<ConcertDTO> dtoList = new ArrayList<>();
        for (Concert concert : concerts) {
            dtoList.add(convertConcertToDTO(concert));
        }
        return dtoList;
    }

    private ConcertDTO convertConcertToDTO(Concert concert) {
        List<ZoneDTO> zoneDTOs = convertZonesToDTOs(concert.getZones());
        List<ReservationDTO> reservationDTOs = convertReservationsToDTOs(concert.getReservations());

        return new ConcertDTO(
                concert.getName(),
                concert.getArtists(),
                concert.getDateHour(),
                concert.getPlace(),
                concert.getDescription(),
                zoneDTOs,
                reservationDTOs);
    }

    private List<ZoneDTO> convertZonesToDTOs(List<Zone> zones) {
        List<ZoneDTO> dtoList = new ArrayList<>();
        for (Zone zone : zones) {
            dtoList.add(zone.toDTO());
        }
        return dtoList;
    }

    private List<ReservationDTO> convertReservationsToDTOs(List<Reservation> reservations) {
        List<ReservationDTO> dtoList = new ArrayList<>();
        for (Reservation res : reservations) {
            dtoList.add(convertReservationToDTO(res));
        }
        return dtoList;
    }

    private ReservationDTO convertReservationToDTO(Reservation reservation) {

        ZoneDTO zoneDTO = reservation.getReservedZone().toDTO();
        List<SeatDTO> seatDTOs = new ArrayList<>();

        if (reservation.getSeats() != null) {
            for (Seat seat : reservation.getSeats()) {
                seatDTOs.add(seat.toDTO());
            }
        }

        return new ReservationDTO(
                reservation.getClientId(),
                null,
                zoneDTO,
                seatDTOs,
                reservation.getQuantityReserved());
    }
}