package com.lab7.server.responseInterpreter.commands;

import com.lab7.common.dataTransfer.Request;
import com.lab7.common.dataTransfer.Response;
import com.lab7.common.lab.Worker;
import com.lab7.server.responseInterpreter.Interpreter;

import java.util.ArrayList;
import java.util.HashMap;


public class RemoveLower extends Command {
    public RemoveLower(Interpreter interpreter) {
        super(interpreter);
    }

    public Response execute(Request rq) {
        Float salary = (Float) rq.attachments.get("salary").get();
        ArrayList<String> messages = new ArrayList<>();
        manager.removeLower(salary, rq.auth.login);
        messages.add("Выполнено");

        return new Response(Response.Status.OK, messages, new HashMap<>());


    }

}