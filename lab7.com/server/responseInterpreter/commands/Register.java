package com.lab7.server.responseInterpreter.commands;

import com.lab7.common.dataTransfer.Request;
import com.lab7.common.dataTransfer.Response;
import com.lab7.server.database.exceptions.UserAlreadyExistsException;
import com.lab7.server.responseInterpreter.Interpreter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Register extends Command{
    public Register(Interpreter interpreter) {
        super(interpreter);
    }

    public Response execute(Request rq) {
        ArrayList<String> messages = new ArrayList<>();
        try {
            interpreter.controller.register(rq.auth.login, rq.auth.password);

        }
        catch(SQLException ex){
            messages.add("Внутренняя ошибка сервера");
            logger.error(ex.getMessage());
        }
        catch(UserAlreadyExistsException ex){
            messages.add("данное имя пользователя уже занято");

        }



        return new Response(Response.Status.OK, messages, new HashMap<>());
    }
}
