package co.edu.uptc.model;

import co.edu.uptc.dtos.SeatDTO;
import co.edu.uptc.dtos.SeatsZoneDTO;

public class SeatsZone extends Zone {

    private int rows;
    private int columns;
    private Seat[][] seats;

    public SeatsZone(String name, double price, String description, boolean hasSeats, int rows, int columns) {
        super(name, price, description, hasSeats);
        this.rows = rows;
        this.columns = columns;
        initializeSeats();
    }

    private void initializeSeats() {
        seats = new Seat[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                seats[i][j] = new Seat(i, j); 
            }
        }
    }

    public Seat getSeat(int row, int col) {
        if (row >= 1 && row <= rows && col >= 1 && col <= columns) {
            return seats[row][col];
        }
        return null;
    }

    public void reserveSeat(int row, int column){
        seats[row][column].setReserved(true);
    }

    public void unreserveSeat(int row, int column){
        seats[row][column].setReserved(false);
    }

    @Override
    public boolean hasCapacity() {
        return getAvailableCapacity() > 0;
    }

    @Override
    public int getAvailableCapacity() {
        int available = 0;
        for (Seat[] row : seats) {
            for (Seat seat : row) {
                if (!seat.isReserved()) {
                    available++;
                }
            }
        }
        return available;
    }

    @Override
    public int getTotalCapacity() {
        return rows * columns;
    }

    public Seat[][] getSeats() {
        return seats;
    }

    public void setSeats(Seat[][] seats) {
        this.seats = seats;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    @Override
    public SeatsZoneDTO toDTO() {
        SeatDTO[][] seatDTOs = new SeatDTO[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                seatDTOs[i][j] = seats[i][j].toDTO();
            }
        }

        return new SeatsZoneDTO(
            name,
            description,
            hasSeats,
            rows * columns,
            price,
            rows,
            columns,
            seatDTOs
        );
    }

    public static SeatsZone fromDTO(SeatsZoneDTO dto) {
        SeatsZone zone = new SeatsZone(
            dto.getName(),
            dto.getPrice(),
            dto.getDescription(),
            dto.hasSeats(),
            dto.getRows(),
            dto.getColumns()
        );

        SeatDTO[][] dtoSeats = dto.getSeats();
        Seat[][] seats = new Seat[dto.getRows()][dto.getColumns()];
        for (int i = 0; i < dto.getRows(); i++) {
            for (int j = 0; j < dto.getColumns(); j++) {
                seats[i][j] = Seat.fromDTO(dtoSeats[i][j]);
            }
        }
        zone.setSeats(seats);
        return zone;
    }
}

