package com.lab7.server.responseInterpreter.commands;

import com.lab7.common.dataTransfer.Request;
import com.lab7.common.dataTransfer.Response;
import com.lab7.common.lab.Position;
import com.lab7.server.responseInterpreter.Interpreter;

import java.util.ArrayList;
import java.util.HashMap;

public class FilterGreaterThanPosition extends Command{
    public FilterGreaterThanPosition(Interpreter interpreter) {
        super(interpreter);
    }

    public Response execute(Request rq) {
        ArrayList<String> messages;
        Position pos = (Position) rq.attachments.get("position").get();
        messages = manager.filterGreaterThanPosition(pos);

        return new Response(Response.Status.OK, messages, new HashMap<>());


    }
}