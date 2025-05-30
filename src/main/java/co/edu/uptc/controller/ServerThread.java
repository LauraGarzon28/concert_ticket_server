package co.edu.uptc.controller;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import co.edu.uptc.connection.Connection;
import co.edu.uptc.dtos.Command;
import co.edu.uptc.dtos.ConcertDTO;
import co.edu.uptc.dtos.ReservationDTO;
import co.edu.uptc.model.Concert;
import co.edu.uptc.model.ConcertManager;
import co.edu.uptc.model.Reservation;

public class ServerThread extends Thread {

    private Connection connection;
    private ConcertManager concertManager;
    private Socket socket;

    public ServerThread(Socket socket, ConcertManager concertManager) {
        this.socket = socket;
        this.concertManager = concertManager;
        try {
            this.connection = new Connection(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            attend();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void attend() throws Exception {
        while (true) {
            Object request = connection.receiveObject();
            if (request == null) {
                break;
            }
            if (request instanceof Command command) {
                switch (command.getCommand()) {
                    case "GET_CONCERTS" -> {
                        List<Concert> concerts = concertManager.getAllConcerts();
                        List<ConcertDTO> concertDTOs = concertManager.toDTO(concerts);
                        connection.sendObject(concertDTOs);
                    }
                    case "EDIT_CONCERT" -> {
                        List<Object> data = (List<Object>) command.getObject();
                        String oldConcert = (String) data.get(0);
                        ConcertDTO updatedConcert = (ConcertDTO) data.get(1);
                        concertManager.updateConcert(oldConcert, Concert.fromDTO(updatedConcert));
                    }
                    case "ADD_CONCERT" -> {
                        ConcertDTO newConcert = (ConcertDTO) command.getObject();
                        concertManager.createConcert(Concert.fromDTO(newConcert));
                    }
                    case "DELETE_CONCERT" -> {
                        String concertName = (String) command.getObject();
                        concertManager.removeConcert(concertName);
                    }
                    case "CREATE_GENERAL_RESERVATION" -> {
                        ReservationDTO reservationDTO = (ReservationDTO) command.getObject();
                        Reservation reservation = Reservation.fromDTO(reservationDTO);
                        concertManager.addGeneralReservation(reservation);
                    }
                    case "CREATE_SEATS_RESERVATION" -> {
                        ReservationDTO reservationDTO = (ReservationDTO) command.getObject();
                        Reservation reservation = Reservation.fromDTO(reservationDTO);
                        concertManager.addSeatReservation(reservation);
                    }
                    case "GET_CONCERT_BY_NAME" -> {
                        String concertName = (String) command.getObject();
                        Concert concert = concertManager.searchConcert(concertName);
                        ConcertDTO concertDTOSend = concertManager.convertConcertToDTO(concert);
                        connection.sendObject(concertDTOSend);
                    }
                    case "GET_RESERVATIONS_BY_CLIENT_ID" -> {
                        int clientId = (int) command.getObject();
                        List<Reservation> reservations = concertManager.getReservationsByClientId(clientId);
                        List<ReservationDTO> reservationDTOs = concertManager.convertReservationsToDTOs(reservations);
                        connection.sendObject(reservationDTOs);
                    }
                    case "DELETE_RESERVATION" -> {
                        ReservationDTO reservationDTO = (ReservationDTO) command.getObject();
                        Reservation reservation = Reservation.fromDTO(reservationDTO);
                        concertManager.removeReservation(reservation);
                    }  
                }
            }
        }
    }

}
