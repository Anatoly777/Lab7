package com.lab7.client.interpreter.commands;

import com.lab7.common.dataTransfer.DataTransference;
import com.lab7.common.dataTransfer.Request;
import com.lab7.common.dataTransfer.Response;
import com.lab7.common.dataTransfer.Utils;
import com.lab7.client.interpreter.Interpreter;

import java.util.HashMap;


public class RemoveById extends InterpreterCommand {
    public RemoveById (Interpreter interpreter){
        super(interpreter);
    }
    public void execute(){
        if (inputManager.getWords().size() < 2 || inputManager.getWords().get(1).isEmpty() ){
            outputManager.println("Неверное количество аргументов");
            return;
        }
        String id = inputManager.getWords().get(1);
        HashMap<String, DataTransference<?>> arguments = new HashMap<>();
        arguments.put("id", new DataTransference<String>(String.class, id));

        Request rq = new Request("remove_by_id", arguments);
        if(client.sendRequest(rq)){
            Response resp = client.receive();
            Utils.printResponseMessages(resp);
        }


    }
    @Override
    public String info(){
        return "Удаляя объект по id. remove <id>";
    }

}
