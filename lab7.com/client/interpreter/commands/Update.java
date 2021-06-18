package com.lab7.client.interpreter.commands;


import com.lab7.client.builders.lineBuilders.LineWorkerBuilder;
import com.lab7.common.dataTransfer.DataTransference;
import com.lab7.common.dataTransfer.Request;
import com.lab7.common.dataTransfer.Response;
import com.lab7.common.dataTransfer.Utils;
import com.lab7.client.interpreter.Interpreter;
import com.lab7.common.lab.Worker;

import java.io.IOException;
import java.util.HashMap;


public class Update extends InterpreterCommand{
    public Update(Interpreter interpreter){
        super(interpreter);
    }
    public void execute() {
        if(inputManager.getWords().size() < 2 || inputManager.getWords().get(1).isEmpty() ){ outputManager.println("Неверное количество аргументов"); return;}

        LineWorkerBuilder builder = new LineWorkerBuilder(inputManager, outputManager);
        String idd = inputManager.getWords().get(1);
        if(!Worker.Params.id.parse(idd)){
            outputManager.println("Синтаксическия ошибка");
            return;
        }
        try {
            Worker worker = builder.build();
            Long id = Worker.Params.id.get();
            HashMap<String, DataTransference<?>> arguments = new HashMap<>();
            arguments.put("id", new DataTransference<Long>(Long.class, id));
            arguments.put("worker", new DataTransference<Worker>(Worker.class, worker));

            Request rq = new Request("update", arguments);
            if(client.sendRequest(rq)){
                Response resp = client.receive();
                Utils.printResponseMessages(resp);
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }
    @Override
    public String info(){
        return "Обновляет объект по его id. update <id>";
    }
}
