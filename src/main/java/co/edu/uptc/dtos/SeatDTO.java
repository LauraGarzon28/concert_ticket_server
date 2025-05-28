package co.edu.uptc.dtos;

import java.io.Serializable;

public class SeatDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private boolean isReserved;
    private int row;
    private int column;

    public SeatDTO(int row, int column) {
        this.row = row;
        this.column = column;
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

}
