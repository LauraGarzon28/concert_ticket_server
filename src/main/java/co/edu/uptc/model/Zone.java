package co.edu.uptc.model;

import co.edu.uptc.dtos.ZoneDTO;

public abstract class Zone {

    protected String name;
    protected String description;
    protected double price;
    protected boolean hasSeats;

    public Zone(String name, double price, String description, boolean hasSeats) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.hasSeats = hasSeats;
    }

    public abstract boolean hasCapacity();

    public abstract int getAvailableCapacity();

    public abstract int getTotalCapacity();

    public abstract ZoneDTO toDTO();

    public static Zone fromDTO(ZoneDTO dto) {
        if (dto.hasSeats()) {
            return SeatsZone.fromDTO(dto);
        } else {
            return GeneralZone.fromDTO(dto);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean hasSeats() {
        return hasSeats;
    }

    public void setHasSeats(boolean hasSeats) {
        this.hasSeats = hasSeats;
    }

}