package com.lab7.server.responseInterpreter.commands;

import com.lab7.common.dataTransfer.Request;
import com.lab7.common.dataTransfer.Response;
import com.lab7.server.responseInterpreter.Interpreter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class FilterByStartDate extends Command{
    public FilterByStartDate(Interpreter interpreter) {
        super(interpreter);
    }

    public Response execute(Request rq) {
        ArrayList<String> messages;
        LocalDate sd = (LocalDate) rq.attachments.get("start_date").get();
        messages = manager.filterByStartDate(sd);

        return new Response(Response.Status.OK, messages, new HashMap<>());


    }
}
