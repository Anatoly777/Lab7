package com.lab7.client.interpreter.commands;

import com.lab7.client.interpreter.Interpreter;
import com.lab7.common.dataTransfer.Request;
import com.lab7.common.dataTransfer.Response;
import com.lab7.common.dataTransfer.Utils;

import java.util.HashMap;


public class RemoveLast extends InterpreterCommand{
    public RemoveLast(Interpreter interpreter){
        super(interpreter);
    }
    public void execute(){
        if(inputManager.getWords().size() < 1 ){ outputManager.println("Неверное количество аргументов"); return;}
            Request rq = new Request("remove_last", new HashMap<>());
            if(client.sendRequest(rq)){
                Response resp = client.receive();
                Utils.printResponseMessages(resp);
            }
    }
    @Override
    public String info(){
        return "Удаляет последнего рабочего";
    }
}
