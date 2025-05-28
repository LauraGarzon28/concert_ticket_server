package co.edu.uptc.dtos;

public class SeatsZoneDTO extends ZoneDTO {

    private static final long serialVersionUID = 1L;

    private int rows;
    private int columns;
    private SeatDTO[][] seats;

    public SeatsZoneDTO(String name, String description, boolean hasSeats, int capacity, double price, int rows, int columns,
            SeatDTO[][] seats) {
        super(name, description, hasSeats,  capacity, price);
        this.rows = rows;
        this.columns = columns;
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

    public SeatDTO[][] getSeats() {
        return seats;
    }

    public void setSeats(SeatDTO[][] seats) {
        this.seats = seats;
    }

}
