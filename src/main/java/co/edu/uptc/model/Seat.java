package co.edu.uptc.model;

import java.io.Serializable;
import java.util.Objects;

import co.edu.uptc.dtos.SeatDTO;

public class Seat implements Serializable {

    private boolean isReserved;
    private int row;
    private int column;

    public Seat(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Seat(int row, int column, boolean isReserved) {
        this.row = row;
        this.column = column;
        this.isReserved = isReserved;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean isReserved) {
        this.isReserved = isReserved;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public SeatDTO toDTO() {
        return new SeatDTO(row, column);
    }

    public static Seat fromDTO(SeatDTO dto) {
        return new Seat(dto.getRow(), dto.getColumn());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Seat seat = (Seat) o;
        return row == seat.row && column == seat.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

}
