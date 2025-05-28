package co.edu.uptc.dtos;
public class GeneralZoneDTO extends ZoneDTO {

    private static final long serialVersionUID = 1L;

    private int quantityReserved;

    public GeneralZoneDTO(String name, String description, boolean hasSeats, int capacity, double price, int quantityReserved) {
        super(name, description, hasSeats, capacity, price);
        this.quantityReserved = quantityReserved;
    }

    public int getQuantityReserved() {
        return quantityReserved;
    }

    public void setQuantityReserved(int quantityReserved) {
        this.quantityReserved = quantityReserved;
    } 

}