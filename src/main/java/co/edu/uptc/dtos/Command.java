package co.edu.uptc.dtos;

import java.io.Serializable;

public class Command implements Serializable{
    private  String command;
    private Object object;

    
    public Command(String command, Object object) {
        this.command = command;
        this.object = object;
    }
    public String getCommand() {
        return command;
    }
    public void setCommand(String command) {
        this.command = command;
    }
    public Object getObject() {
        return object;
    }
    public void setObject(Object object) {
        this.object = object;
    }

    
}
