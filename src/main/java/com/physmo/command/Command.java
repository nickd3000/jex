package com.physmo.command;

public class Command {
    public String type;
    public Object object;

    public Command() {
    }

    public Command(String s, Object o) {
        this.type = s;
        this.object = o;
    }
}
