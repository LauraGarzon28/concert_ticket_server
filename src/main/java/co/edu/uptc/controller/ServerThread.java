package co.edu.uptc.controller;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import co.edu.uptc.connection.Connection;
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
        Object request = connection.receiveObject();
        if (request instanceof String action) {
            switch (action) {
                case "GET_CONCERTS" -> {
                    List<Concert> concerts = concertManager.getAllConcerts();
                    List<ConcertDTO> concertDTOs = concertManager.toDTO(concerts);
                    connection.sendObject(concertDTOs);
                }
                case "CREATE_RESERVATION" -> {
                    ReservationDTO reservationDTO = (ReservationDTO) connection.receiveObject();
                    Reservation reservation = Reservation.fromDTO(reservationDTO);
                    //   boolean success = concertManager.addReservation(reservation);
                    //  connection.sendObject(success);
                }
                case "EDIT_CONCERT" -> {
                    ConcertDTO updatedConcert = (ConcertDTO) connection.receiveObject();
                    concertManager.updateConcert(Concert.fromDTO(updatedConcert));
                }
            }
        }
    }

}
