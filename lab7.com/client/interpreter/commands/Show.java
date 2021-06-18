package com.lab7.client.interpreter.commands;


import com.lab7.client.interpreter.Interpreter;
import com.lab7.common.dataTransfer.Request;
import com.lab7.common.dataTransfer.Response;
import com.lab7.common.dataTransfer.Utils;

import java.util.HashMap;

public class Show extends InterpreterCommand {
    public Show(Interpreter interpreter) {
        super(interpreter);
    }

    public void execute() {
        Request rq = new Request("show", new HashMap<>());
        if (client.sendRequest(rq)) {
            Response resp = client.receive();
            Utils.printResponseMessages(resp);
        }


    }

    @Override
    public String info() {
        return "показывает список элементов коллекции";
    }
}
