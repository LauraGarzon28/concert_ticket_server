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

    public synchronized boolean createConcert(Concert concert) {
        if (concert == null || concert.getName() == null) {
            return false;
        }
        return concerts.add(concert);
    }

    public Concert searchConcert(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        Concert concert = new Concert();
        concert.setName(name);
        return concerts.search(concert);
    }

    public List<Concert> getAllConcerts() {
        return concerts.in_order();
    }

    public synchronized boolean updateConcert(Concert concert) {
        if (concert == null || concert.getName() == null || concert.getName().trim().isEmpty()) {
            return false;
        }

        Concert newConcert = new Concert();
        newConcert.setName(concert.getName());

        if (concerts.exist(newConcert) != null) {
            concerts.delete(newConcert);
            return concerts.add(concert);
        }
        return false;
    }

    public synchronized void removeConcert(String name) {
        if (name == null || name.trim().isEmpty()) {
            return;
        }
        Concert concert = new Concert();
        concert.setName(name);
        concerts.delete(concert);
    }

    public synchronized boolean addGeneralReservation(Reservation reservation) {
        if (reservation == null || reservation.getReservedConcert() == null)
            return false;

        Concert concert = concerts.exist(reservation.getReservedConcert());
        if (concert == null)
            return false;

        Zone zone = reservation.getReservedZone();
        if (!(zone instanceof GeneralZone))
            return false;

        GeneralZone generalZone = (GeneralZone) zone;
        int quantity = reservation.getQuantityReserved();

        if (generalZone.getAvailableCapacity() >= quantity) {
            boolean reserved = generalZone.reserve(quantity);
            if (reserved) {
                return concert.getReservations().add(reservation);
            }
        }

        return false;
    }

    public synchronized boolean addSeatReservation(Reservation reservation) {
        if (reservation == null || reservation.getReservedConcert() == null)
            return false;

        Concert concert = concerts.exist(reservation.getReservedConcert());
        if (concert == null)
            return false;

        Zone zone = reservation.getReservedZone();
        if (!(zone instanceof SeatsZone))
            return false;

        SeatsZone seatsZone = (SeatsZone) zone;

        for (Seat seat : reservation.getSeats()) {
            if (seatsZone.getSeat(seat.getRow(), seat.getColumn()).isReserved()) {
                return false;
            }
        }

        for (Seat seat : reservation.getSeats()) {
            seatsZone.reserveSeat(seat.getRow(), seat.getColumn());
        }

        return concert.getReservations().add(reservation);
    }

    public List<Reservation> getReservationsByClientId(int clientId) {
        List<Reservation> clientReservations = new ArrayList<>();

        for (Concert concert : concerts.in_order()) {
            for (Reservation res : concert.getReservations()) {
                if (res.getClientId() == clientId) {
                    clientReservations.add(res);
                }
            }
        }

        return clientReservations;
    }

    public boolean removeReservation(Reservation reservation) {
        if (reservation == null || reservation.getReservedConcert() == null)
            return false;

        Concert concert = concerts.exist(reservation.getReservedConcert());
        if (concert == null)
            return false;

        Zone zone = reservation.getReservedZone();

        if (zone instanceof GeneralZone generalZone) {
            generalZone.unreserve(reservation.getQuantityReserved());
        } else if (zone instanceof SeatsZone seatsZone) {
            for (Seat seat : reservation.getSeats()) {
                seatsZone.unreserveSeat(seat.getRow(), seat.getColumn());
            }
        }

        return concert.getReservations().remove(reservation);
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
        ZoneDTO zoneDTO = reservation.getReservedZone() != null ? reservation.getReservedZone().toDTO() : null;
        ConcertDTO concertDTO = reservation.getReservedConcert() != null ? reservation.getReservedConcert().toDTO()
                : null;

        if (reservation.getReservedZone() instanceof SeatsZone) {
            List<SeatDTO> seatDTOs = new ArrayList<>();
            if (reservation.getSeats() != null) {
                for (Seat seat : reservation.getSeats()) {
                    seatDTOs.add(seat.toDTO());
                }
            }

            return new ReservationDTO(
                    reservation.getClientId(),
                    concertDTO,
                    zoneDTO,
                    seatDTOs);
        } else {
            return new ReservationDTO(
                    reservation.getClientId(),
                    concertDTO,
                    zoneDTO,
                    reservation.getQuantityReserved());
        }
    }

}