package com.lab7.server.responseInterpreter.commands;


import com.lab7.common.dataTransfer.Request;
import com.lab7.common.dataTransfer.Response;
import com.lab7.server.responseInterpreter.Interpreter;

import java.util.ArrayList;
import java.util.HashMap;

public class MinByStatus extends Command {
    public MinByStatus(Interpreter interpreter) {
        super(interpreter);
    }

    public Response execute(Request rq) {
        ArrayList<String> messages = new ArrayList<>();

        messages.add("Элемент с id [" + manager.minByStatus() + "] имеет минимальный статус");
        return new Response(Response.Status.OK, messages, new HashMap<>());

    }

}
