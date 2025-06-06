package co.edu.uptc.connection;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import co.edu.uptc.dtos.Command;
import co.edu.uptc.dtos.ConcertDTO;
import co.edu.uptc.dtos.ReservationDTO;
import co.edu.uptc.model.Concert;
import co.edu.uptc.model.ConcertManager;
import co.edu.uptc.model.Reservation;

public class ServerThread extends Thread {

    private ClientConnection connection;
    private ConcertManager concertManager;
    private Socket socket;

    public ServerThread(Socket socket, ConcertManager concertManager) {
        this.socket = socket;
        this.concertManager = concertManager;
        try {
            this.connection = new ClientConnection(socket);
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
            if (request == null)
                break;

            if (request instanceof Command command) {
                handleCommand(command);
            }
        }
    }

    private void handleCommand(Command command) throws Exception {
        switch (command.getCommand()) {
            case "GET_CONCERTS" -> handleGetConcerts();
            case "EDIT_CONCERT" -> handleEditConcert(command);
            case "ADD_CONCERT" -> handleAddConcert(command);
            case "DELETE_CONCERT" -> handleDeleteConcert(command);
            case "CREATE_GENERAL_RESERVATION" -> handleGeneralReservation(command);
            case "CREATE_SEATS_RESERVATION" -> handleSeatReservation(command);
            case "GET_CONCERT_BY_NAME" -> handleGetConcertByName(command);
            case "GET_RESERVATIONS_BY_CLIENT_ID" -> handleGetReservationsByClient(command);
            case "DELETE_RESERVATION" -> handleDeleteReservation(command);
        }
    }

    private void handleGetConcerts() throws IOException {
        List<ConcertDTO> concertDTOs = concertManager.toDTO(concertManager.getAllConcerts());
        connection.sendObject(concertDTOs);
    }

    private void handleEditConcert(Command command) throws IOException {
        List<Object> data = (List<Object>) command.getObject();
        String oldConcert = (String) data.get(0);
        ConcertDTO updatedConcert = (ConcertDTO) data.get(1);
        connection.sendObject(concertManager.updateConcert(oldConcert, Concert.fromDTO(updatedConcert)));
    }

    private void handleAddConcert(Command command) throws IOException {
        ConcertDTO newConcert = (ConcertDTO) command.getObject();
        connection.sendObject(concertManager.createConcert(Concert.fromDTO(newConcert)));
    }

    private void handleDeleteConcert(Command command) throws IOException {
        String concertName = (String) command.getObject();
        connection.sendObject(concertManager.removeConcert(concertName));
    }

    private void handleGeneralReservation(Command command) throws IOException {
        Reservation reservation = Reservation.fromDTO((ReservationDTO) command.getObject());
        connection.sendObject(concertManager.addGeneralReservation(reservation));
    }

    private void handleSeatReservation(Command command) throws IOException {
        Reservation reservation = Reservation.fromDTO((ReservationDTO) command.getObject());
        connection.sendObject(concertManager.addSeatReservation(reservation));
    }

    private void handleDeleteReservation(Command command) throws IOException {
        Reservation reservation = Reservation.fromDTO((ReservationDTO) command.getObject());
        connection.sendObject(concertManager.removeReservation(reservation));
    }

    private void handleGetConcertByName(Command command) throws IOException {
        Concert concert = concertManager.searchConcert((String) command.getObject());
        connection.sendObject(concertManager.convertConcertToDTO(concert));
    }

    private void handleGetReservationsByClient(Command command) throws IOException {
        int clientId = (int) command.getObject();
        List<ReservationDTO> reservationDTOs = concertManager
                .convertReservationsToDTOs(concertManager.getReservationsByClientId(clientId));
        connection.sendObject(reservationDTOs);
    }

}
