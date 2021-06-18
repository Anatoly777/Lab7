package com.lab7.server.responseInterpreter.commands;


import com.lab7.common.dataTransfer.Request;
import com.lab7.common.dataTransfer.Response;
import com.lab7.common.lab.Worker;
import com.lab7.server.responseInterpreter.Interpreter;
import com.lab7.server.workersManager.exceptions.IllegalDataAccessException;
import com.lab7.server.workersManager.exceptions.NotFoundException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public class Update extends Command {
    public Update(Interpreter interpreter) {
        super(interpreter);
    }

    public Response execute(Request rq) throws NotFoundException {
        ArrayList<String> messages = new ArrayList<>();
        Long id = (Long) rq.attachments.get("id").get();
        Worker w = (Worker) rq.attachments.get("worker").get();
        try {
            manager.update(id, w, rq.auth.login);
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        } catch (IllegalDataAccessException ex) {
            logger.error(ex.getMessage());
        }
        messages.add("Элемент [" + w + "] успешно обновлён");


        return new Response(Response.Status.OK, messages, new HashMap<>());
    }
}