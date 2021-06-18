package com.lab7.client.interpreter.commands;


import com.lab7.common.dataTransfer.Request;
import com.lab7.common.dataTransfer.Response;
import com.lab7.common.dataTransfer.Utils;
import com.lab7.client.interpreter.Interpreter;


import java.util.HashMap;

public class MinByStatus extends InterpreterCommand {
    public MinByStatus(Interpreter interpreter){
        super(interpreter);
    }
    public void execute(){
        Request rq = new Request("min_by_status", new HashMap<>());
        if(client.sendRequest(rq)){
            Response resp = client.receive();
            Utils.printResponseMessages(resp);
        }


    }
    @Override
    public String info(){
        return "Показывает рабочего с минимальным статусом";
    }

}
