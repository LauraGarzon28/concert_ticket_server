package co.edu.uptc.dtos;

import java.io.Serializable;

public abstract class ZoneDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String name;
    private String description;
    private int capacity;
    private double price;
    private boolean hasSeats;

    public ZoneDTO(String name, String description, boolean hasSeats, int capacity, double price) {
        this.name = name;
        this.description = description;
        this.hasSeats = hasSeats;
        this.capacity = capacity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean hasSeats() {
        return hasSeats;
    }

    public void setHasSeats(boolean hasSeats) {
        this.hasSeats = hasSeats;
    }

}
