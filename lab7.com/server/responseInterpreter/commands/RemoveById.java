package com.lab7.server.responseInterpreter.commands;

import com.lab7.common.dataTransfer.Request;
import com.lab7.common.dataTransfer.Response;
import com.lab7.server.responseInterpreter.Interpreter;
import com.lab7.server.workersManager.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;


public class RemoveById extends Command {
    public RemoveById(Interpreter interpreter) {
        super(interpreter);
    }

    public Response execute(Request rq) {
        Long id = Long.valueOf((String) rq.attachments.get("id").get());
        ArrayList<String> messages = new ArrayList<>();
        manager.removeById(id, rq.auth.login);
        messages.add("Элемент [" + id + "] удалён");


        return new Response(Response.Status.OK, messages, new HashMap<>());

    }


}
