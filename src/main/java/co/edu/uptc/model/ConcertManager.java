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
    private final String filePath = "./data/concerts.json";

    public ConcertManager() {
        this.concerts = new BinaryTree<>((concert1, concert2) -> concert1.getName().compareTo(concert2.getName()));
        this.jsonService = new JsonService();
        loadConcerts();
    }

    private void loadConcerts() {
        List<Concert> concertsFromFile = jsonService.readJsonConcerts(filePath);
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
        concerts.add(concert);
        jsonService.appendConcertToFile(filePath, concert);
        return true;
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

    public synchronized boolean updateConcert(String oldName, Concert concert) {
        if (concert == null || concert.getName() == null || concert.getName().trim().isEmpty()) {
            return false;
        }

        Concert oldConcert = new Concert();
        oldConcert.setName(oldName);

        if (concerts.exist(oldConcert) != null) {
            concerts.delete(oldConcert);
            concerts.add(concert);

            jsonService.writeConcertsToFile(filePath, getAllConcerts());
            return true;
        }
        return false;
    }

    public synchronized boolean removeConcert(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        Concert concert = new Concert();
        concert.setName(name);
        if (concerts.exist(concert) != null) {
            concerts.delete(concert);
            jsonService.writeConcertsToFile(filePath, getAllConcerts());
            return true;
        } else
            return false;
    }

    public synchronized boolean addGeneralReservation(Reservation reservation) {
        if (reservation == null)
            return false;

        Concert concert = new Concert();
        concert.setName(reservation.getConcertName());
        concert = concerts.search(concert);
        if (concert == null)
            return false;

        Zone zone = findZoneByName(concert, reservation.getZoneName());
        if (!(zone instanceof GeneralZone generalZone))
            return false;

        int quantity = reservation.getQuantityReserved();

        if (generalZone.getAvailableCapacity() >= quantity) {
            boolean reserved = generalZone.reserve(quantity);
            if (reserved) {
                concert.getReservations().add(reservation);
                jsonService.writeConcertsToFile(filePath, getAllConcerts());
                return true;
            }
        }

        return false;
    }

    public synchronized boolean addSeatReservation(Reservation reservation) {
        if (reservation == null)
            return false;

        Concert concert = new Concert();
        concert.setName(reservation.getConcertName());
        concert = concerts.search(concert);
        if (concert == null)
            return false;

        Zone zone = findZoneByName(concert, reservation.getZoneName());
        if (!(zone instanceof SeatsZone seatsZone))
            return false;

        for (Seat seat : reservation.getSeats()) {
            if (seatsZone.getSeat(seat.getRow(), seat.getColumn()).isReserved()) {
                return false;
            }
        }

        for (Seat seat : reservation.getSeats()) {
            seatsZone.reserveSeat(seat.getRow(), seat.getColumn());
        }

        concert.getReservations().add(reservation);
        jsonService.writeConcertsToFile(filePath, getAllConcerts());
        return true;
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
        if (reservation == null)
            return false;
        Concert concert = new Concert();
        concert.setName(reservation.getConcertName());
        concert = concerts.search(concert);
        if (concert == null)
            return false;
        Zone zone = findZoneByName(concert, reservation.getZoneName());
        if (zone instanceof GeneralZone generalZone) {
            generalZone.unreserve(reservation.getQuantityReserved());
        } else if (zone instanceof SeatsZone seatsZone) {
            for (Seat seat : reservation.getSeats()) {
                seatsZone.unreserveSeat(seat.getRow(), seat.getColumn());
            }
        }
        concert.getReservations().remove(reservation);
        jsonService.writeConcertsToFile(filePath, getAllConcerts());
        return true;
    }

    public List<ConcertDTO> toDTO(List<Concert> concerts) {
        List<ConcertDTO> dtoList = new ArrayList<>();
        for (Concert concert : concerts) {
            dtoList.add(convertConcertToDTO(concert));
        }
        return dtoList;
    }

    public ConcertDTO convertConcertToDTO(Concert concert) {
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
            if (zone instanceof GeneralZone generalZone) {
                dtoList.add(generalZone.toDTO());
            } else if (zone instanceof SeatsZone seatsZone) {
                dtoList.add(seatsZone.toDTO());
            }
        }
        return dtoList;
    }

    public List<ReservationDTO> convertReservationsToDTOs(List<Reservation> reservations) {
        List<ReservationDTO> dtoList = new ArrayList<>();
        for (Reservation res : reservations) {
            dtoList.add(convertReservationToDTO(res));
        }
        return dtoList;
    }

    private ReservationDTO convertReservationToDTO(Reservation reservation) {
        String concertName = reservation.getConcertName();
        String zoneName = reservation.getZoneName();

        if (reservation.getSeats() != null && !reservation.getSeats().isEmpty()) {
            List<SeatDTO> seatDTOs = new ArrayList<>();
            for (Seat seat : reservation.getSeats()) {
                seatDTOs.add(seat.toDTO());
            }

            return new ReservationDTO(
                    reservation.getClientId(),
                    concertName,
                    zoneName,
                    seatDTOs);
        } else {
            return new ReservationDTO(
                    reservation.getClientId(),
                    concertName,
                    zoneName,
                    reservation.getQuantityReserved());
        }
    }

    private Zone findZoneByName(Concert concert, String zoneName) {
        return concert.getZones().stream()
                .filter(z -> z.getName().equals(zoneName))
                .findFirst()
                .orElse(null);
    }

}