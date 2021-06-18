package com.lab7.server.responseInterpreter.commands;

import com.lab7.common.dataTransfer.Request;
import com.lab7.common.dataTransfer.Response;
import com.lab7.common.lab.Worker;
import com.lab7.server.responseInterpreter.Interpreter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Add extends Command {
    public Add(Interpreter interpreter) {
        super(interpreter);
    }

    public Response execute(Request rq) {
        ArrayList<String> messages = new ArrayList<>();
        Worker w = (Worker) rq.attachments.get("worker").get();
        try {
            manager.add(w, rq.auth.login);
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
        messages.add("Элемент [" + w + "] успешно добавлен");


        return new Response(Response.Status.OK, messages, new HashMap<>());
    }
}
