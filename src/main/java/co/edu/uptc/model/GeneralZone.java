package co.edu.uptc.model;

import co.edu.uptc.dtos.GeneralZoneDTO;

public class GeneralZone extends Zone {

    private int maxCapacity;
    private int quantityReserved;

    public GeneralZone(String name, double price, String description, boolean hasSeats, int maxCapacity) {
        super(name, price, description, hasSeats);
        this.maxCapacity = maxCapacity;
        this.quantityReserved = 0;

    }

    public boolean reserve(int quantity) {
        if (quantityReserved < maxCapacity) {
            quantityReserved += quantity;
            return true;
        }
        return false;
    }

    public boolean unreserve(int quantity) {
        if (quantityReserved >= quantity) {
            quantityReserved -= quantity;
            return true;
        }
        return false;
    }

    @Override
    public GeneralZoneDTO toDTO() {
        return new GeneralZoneDTO(
                this.name,
                this.description,
                this.hasSeats,
                this.maxCapacity,
                this.price,
                this.quantityReserved);
    }

    public static GeneralZone fromGeneralZoneDTO(GeneralZoneDTO dto) {
        GeneralZone zone = new GeneralZone(
                dto.getName(),
                dto.getPrice(),
                dto.getDescription(),
                dto.hasSeats(),
                dto.getCapacity());
        zone.setQuantityReserved(dto.getQuantityReserved());
        return zone;
    }

    @Override
    public boolean hasCapacity() {
        return quantityReserved < maxCapacity;
    }

    @Override
    public int getAvailableCapacity() {
        return maxCapacity - quantityReserved;
    }

    @Override
    public int getTotalCapacity() {
        return maxCapacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getQuantityReserved() {
        return quantityReserved;
    }

    public void setQuantityReserved(int quantityReserved) {
        this.quantityReserved = quantityReserved;
    }

}
