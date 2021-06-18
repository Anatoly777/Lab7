package com.lab7.client.interpreter.commands;

import com.lab7.common.dataTransfer.DataTransference;
import com.lab7.common.dataTransfer.Request;
import com.lab7.common.dataTransfer.Response;
import com.lab7.common.dataTransfer.Utils;
import com.lab7.client.interpreter.Interpreter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;


public class FilterByStartDate extends InterpreterCommand {
    public FilterByStartDate (Interpreter interpreter){
        super(interpreter);
    }
    public void execute(){
        if(inputManager.getWords().size() < 2 || inputManager.getWords().get(1).isEmpty() ){ outputManager.println("Неверное количество аргументов"); return;}
        LocalDate startDate = LocalDate.parse(inputManager.getWords().get(1));

        HashMap<String, DataTransference<?>> arguments = new HashMap<>();
        arguments.put("start_date", new DataTransference<LocalDate>(LocalDate.class, startDate));

        Request rq = new Request("filter_by_start_date", arguments);
        if(client.sendRequest(rq)){
            Response resp = client.receive();
            Utils.printResponseMessages(resp);
        }


    }
    @Override
    public String info(){
        return "Сортировка по дате. filter_by_start_date <Start date>";
    }

}
