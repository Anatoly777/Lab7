package com.lab7.client.interpreter.commands;


import com.lab7.client.interpreter.Interpreter;
import com.lab7.common.dataTransfer.Request;
import com.lab7.common.dataTransfer.Response;
import com.lab7.common.dataTransfer.Utils;

import java.util.HashMap;

public class Info extends InterpreterCommand {
    public Info(Interpreter interpreter) {
        super(interpreter);
    }

    public void execute() {
        Request rq = new Request("info", new HashMap<>());
        if (client.sendRequest(rq)) {
            Response resp = client.receive();
            Utils.printResponseMessages(resp);
        }


    }

    @Override
    public String info() {
        return "Вывести информацию о коллекции";
    }
}
