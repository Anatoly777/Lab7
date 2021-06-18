package com.lab7.client.interpreter.commands;

import com.lab7.common.dataTransfer.DataTransference;
import com.lab7.common.dataTransfer.Request;
import com.lab7.common.dataTransfer.Response;
import com.lab7.common.dataTransfer.Utils;
import com.lab7.client.interpreter.Interpreter;
import com.lab7.common.lab.Position;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;


public class FilterGreaterThanPosition extends InterpreterCommand {
    public FilterGreaterThanPosition (Interpreter interpreter){
        super(interpreter);
    }
    public void execute(){
        if(inputManager.getWords().size() < 2 || inputManager.getWords().get(1).isEmpty() ){ outputManager.println("Неверное количество аргументов"); return;}
        Position pos = Position.valueOf(inputManager.getWords().get(1).toUpperCase());

        HashMap<String, DataTransference<?>> arguments = new HashMap<>();
        arguments.put("position", new DataTransference<Position>(Position.class, pos));

        Request rq = new Request("filter_greater_than_position", arguments);
        if(client.sendRequest(rq)){
            Response resp = client.receive();
            Utils.printResponseMessages(resp);
        }


    }
    @Override
    public String info(){
        return "Вывести работников на позиции выше заданной отсортировав. filter_by_start_date <Start date>";
    }

}
