package com.lab7.server.responseInterpreter.commands;

import com.lab7.common.dataTransfer.Request;
import com.lab7.common.dataTransfer.Response;
import com.lab7.common.lab.Worker;
import com.lab7.server.responseInterpreter.Interpreter;

import java.util.ArrayList;
import java.util.HashMap;


public class RemoveLast extends Command {
    public RemoveLast(Interpreter interpreter) {
        super(interpreter);
    }

    public Response execute(Request rq) {
        ArrayList<String> messages = new ArrayList<>();
        manager.removeLast(rq.auth.login);
        messages.add("Выполнено");

        return new Response(Response.Status.OK, messages, new HashMap<>());


    }

}