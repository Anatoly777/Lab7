package com.lab7.client.interpreter.commands;


import com.lab7.client.interpreter.Interpreter;
import com.lab7.common.dataTransfer.Request;
import com.lab7.common.dataTransfer.Response;
import com.lab7.common.dataTransfer.Utils;

import java.util.HashMap;

public class Clear extends InterpreterCommand {
    public Clear(Interpreter interpreter) {
        super(interpreter);
    }

    public void execute() {
        Request rq = new Request("clear", new HashMap<>());
        if (client.sendRequest(rq)) {
            Response resp = client.receive();
            Utils.printResponseMessages(resp);
        }

    }
}
