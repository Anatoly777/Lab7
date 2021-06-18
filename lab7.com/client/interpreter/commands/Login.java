package com.lab7.client.interpreter.commands;


import com.lab7.client.builders.lineBuilders.AuthBuilder;
import com.lab7.client.interpreter.Interpreter;
import com.lab7.common.dataTransfer.*;

import java.io.IOException;
import java.util.HashMap;

public class Login extends InterpreterCommand {
    public Login(Interpreter interpreter) {
        super(interpreter);
    }

    public void execute() {

        try {
            Auth auth = (new AuthBuilder(inputManager, outputManager)).build();
            client.setAuth(auth);
            Request rq = new Request("login", new HashMap<>());
            if (client.sendRequest(rq)) {
                Response resp = client.receive();
                Utils.printResponseMessages(resp);
                if (resp.status.equals(Response.Status.ERROR)) {
                    client.setAuth(new Auth("", ""));
                }
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }


    }

    @Override
    public String info() {
        return "Вход в аккаунт login <username> <password>";
    }
}
