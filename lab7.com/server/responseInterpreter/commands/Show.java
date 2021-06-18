package com.lab7.server.responseInterpreter.commands;


import com.lab7.common.dataTransfer.Request;
import com.lab7.common.dataTransfer.Response;
import com.lab7.common.lab.Worker;
import com.lab7.server.responseInterpreter.Interpreter;

import java.util.ArrayList;
import java.util.HashMap;

public class Show extends Command {
    public String workerAsString(Worker w) {
        String s = "";

        s += "Имя: " + w.name + "\n";
        s += "Зарплата: " + w.salary + "\n";
        s += "id: " + w.id + "\n";
        s += "Дата создания объекта: " + w.creationDate.toString() + "\n";
        s += "Дата заключения контракта: " + w.endDate.toString() + "\n";
        s += "Должность: " + w.position.toString() + "\n";
        s += "Статус: " + w.status.toString() + "\n";

        return s;

    }

    public Show(Interpreter interpreter) {
        super(interpreter);
    }

    public Response execute(Request rq) {
        ArrayList<String> messages = new ArrayList<>();

        ArrayList<Worker> workers = manager.getWorkers();
        for (Worker key : workers) {
            messages.add(workerAsString(key));
        }
        return new Response(Response.Status.OK, messages, new HashMap<>());

    }

}
