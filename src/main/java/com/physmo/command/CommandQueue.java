package com.physmo.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandQueue {
    List<Command> list;

    public CommandQueue() {
        list = new ArrayList<>();
    }

    public Optional<Command> popCommand() {
        if (hasItem()) {
            Command c = list.get(list.size()-1);
            list.remove(list.size()-1);
            return Optional.of(c);
        }
        return Optional.empty();
    }

    public boolean hasItem() {
        return list.size() > 0;
    }

    public void push(Command c) {
        list.add(c);
    }

    public void push(String s, Object o) {
        list.add(new Command(s, o));
    }
}
