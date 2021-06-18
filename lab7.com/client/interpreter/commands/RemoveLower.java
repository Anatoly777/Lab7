package com.lab7.client.interpreter.commands;

import com.lab7.common.dataTransfer.DataTransference;
import com.lab7.common.dataTransfer.Request;
import com.lab7.common.dataTransfer.Response;
import com.lab7.common.dataTransfer.Utils;
import com.lab7.client.interpreter.Interpreter;
import com.lab7.common.lab.Worker;

import java.util.HashMap;


public class RemoveLower extends InterpreterCommand{
    public RemoveLower(Interpreter interpreter){
        super(interpreter);
    }
    public void execute(){
        if(inputManager.getWords().size() < 2 ){ outputManager.println("Неверное количество аргументов"); return;}
        String sal = inputManager.getWords().get(1);
        if(Worker.Params.salary.parse(sal)){
            Float salary = Worker.Params.salary.get();
            HashMap<String, DataTransference<?>> arguments = new HashMap<>();
            arguments.put("salary", new DataTransference<Float>(Float.class, salary));
            Request rq = new Request("remove_lower", arguments);
            if(client.sendRequest(rq)){
                Response resp = client.receive();
                Utils.printResponseMessages(resp);
            }
        }
        else{
            outputManager.println("Синтаксическая ошибка. проверьте корректность аргументов"); return;
        }


    }
    @Override
    public String info(){
        return "Удаляет всех рабочих, ЗП которых ниже заданной. remove_lower <salary>";
    }
}
