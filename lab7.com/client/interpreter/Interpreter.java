package com.lab7.client.interpreter;


import com.lab7.client.Client;
import com.lab7.client.interpreter.commands.*;
import com.lab7.common.io.InputManager;
import com.lab7.common.io.OutputManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class Interpreter {

    private LinkedList<String> history;
    private HashMap<String, InterpreterCommand> executors;
    private InputManager inputManager;
    private OutputManager outputManager;
    private Client client;


    private boolean endFlag;
    public static final HashMap<String, Class> DEFAULT_COMMANDS = new HashMap<String, Class>() {{
        put("help", Help.class);
        put("exit", Exit.class);
        put("info", Info.class);
        put("show", Show.class);
        put("add", Add.class);
        put("update", Update.class);
        put("remove_by_id", RemoveById.class);
        put("clear", Clear.class);
        put("execute_script", ExecuteScript.class);
        put("remove_greater", RemoveGreater.class);
        put("remove_last", RemoveLast.class);
        put("remove_lower", RemoveLower.class);
        put("min_by_status", MinByStatus.class);
        put("filter_by_start_date", FilterByStartDate.class);
        put("filter_greater_than_position", FilterGreaterThanPosition.class);
        put("login", Login.class);
        put("register", Register.class);


    }};


    public Interpreter(InputManager inputManager, OutputManager outputManager, Client client) {


        this.inputManager = inputManager;
        this.outputManager = outputManager;
        this.client = client;
        this.endFlag = false;
        this.history = new LinkedList<String>();
        for (int i = 0; i < 6; i++) this.history.add("");
        this.build();
    }

    /**
     * запуск интерпретатора.
     */
    public void run() {
        try {
            while (!endFlag && this.inputManager.hasNext()) {
                this.outputManager.print(">");
                this.inputManager.nextLine();
                if (this.inputManager.getString().isEmpty()) continue;
                String cmd = this.inputManager.getWords().get(0);
                history.removeLast();
                history.addFirst(cmd);
                if (this.executors.get(cmd) == null) {
                    this.outputManager.println("Неизвестная команда");
                    continue;
                }
                this.executors.get(cmd).execute();
            }

        } catch (IOException ex) {
            this.endFlag = true;
        }
    }

    /**
     * Перессборка интрепретатора. Пересоздаются экземпляры команд.
     */
    public void build() {
        HashMap<String, InterpreterCommand> executors = new HashMap<String, InterpreterCommand>();
        for (String c : DEFAULT_COMMANDS.keySet()) {
            try {

                InterpreterCommand command = (InterpreterCommand) DEFAULT_COMMANDS.get(c).getDeclaredConstructors()[0].newInstance(this);
                executors.put(c, command);
            } catch (ReflectiveOperationException ex) {
                ex.printStackTrace();
                continue;
            }
        }
        this.executors = executors;
    }

    /**
     * Прерывание работы интерпретатора.
     */
    public void exit() {
        this.endFlag = true;
        this.client.disconnect();
    }

    public InputManager getInputManager() {
        return this.inputManager;
    }

    public OutputManager getOutputManager() {
        return this.outputManager;
    }

    public LinkedList<String> getHistory() {
        return this.history;
    }

    public Client getClient() {
        return this.client;
    }

    public HashMap<String, InterpreterCommand> getExecutors() {
        return this.executors;
    }


}
