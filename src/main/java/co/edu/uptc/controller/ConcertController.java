package co.edu.uptc.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import co.edu.uptc.model.Concert;
import co.edu.uptc.model.ConcertManager;
import co.edu.uptc.model.GeneralZone;
import co.edu.uptc.model.Reservation;
import co.edu.uptc.model.Seat;
import co.edu.uptc.model.SeatsZone;
import co.edu.uptc.model.Zone;

public class ConcertController {

    private ServerSocket serverSocket;
    private ConcertManager concertManager;

    public ConcertController(int port) {
        concertManager = new ConcertManager();

        GeneralZone generalZone = new GeneralZone("General", 50.0, "Zona general sin asientos", false, 100);
        SeatsZone seatsZone = new SeatsZone("VIP", 150.0, "Zona con asientos asignados", true, 5, 5);

        Concert concert = new Concert("Legend 1997", new ArrayList<String>(), LocalDateTime.now(), "Cuba", "Una descripción", null);

        concert.addZone(generalZone);
        concert.addZone(seatsZone);

        System.out.println("Crear concierto: " + concertManager.createConcert(concert));

        Concert foundConcert = concertManager.searchConcert("Rock Fest 2025");

        System.out.println("Concierto encontrado: " + (foundConcert != null));

        Reservation generalReservation = new Reservation(
                1, // clientId
                concert, // concierto
                (Zone) generalZone, // zona
                5);
        System.out.println("Reservar en zona general: " + concertManager.addGeneralReservation(generalReservation));

        List<Seat> seats = List.of(
                new Seat(0, 0),
                new Seat(0, 1));

        Reservation seatReservation = new Reservation(
                1, // clientId
                concert,
                seatsZone,
                seats);

        System.out.println("Reservar asientos: " + concertManager.addSeatReservation(seatReservation));

        List<Reservation> client1Reservations = concertManager.getReservationsByClientId(1);
        System.out.println("Reservaciones del cliente 1: " + client1Reservations.size());

        // Eliminar reservación general
        System.out.println("Eliminar reservación general: " + concertManager.removeReservation(generalReservation));

        // Eliminar reservación de asientos
        System.out.println("Eliminar reservación de asientos: " + concertManager.removeReservation(seatReservation));

        // Verificar si aún quedan reservaciones
        List<Reservation> allRemaining = concertManager.getReservationsByClientId(1);
        System.out.println("Reservaciones restantes para cliente 1: " + allRemaining.size());

        concertManager.removeConcert("Legend 1997");
        System.out.println("Concierto eliminado: " + (concertManager.searchConcert("Legend 1997") == null));

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            new ServerThread(socket, concertManager).start();
        }
    }

    public static int getPort() {
        try (InputStream input = new FileInputStream("./data/config.properties")) {
            Properties properties = new Properties();
            properties.load(input);
            return Integer.parseInt(properties.getProperty("port"));
        } catch (IOException e) {
            e.printStackTrace();
            return 1234;
        }
    }

    public static void main(String[] args) throws Exception {
        new ConcertController(getPort()).start();
    }

}
