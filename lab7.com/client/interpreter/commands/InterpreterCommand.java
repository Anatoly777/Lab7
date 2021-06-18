package com.lab7.client.interpreter.commands;

import com.lab7.client.Client;
import com.lab7.client.interpreter.Interpreter;
import com.lab7.common.io.InputManager;
import com.lab7.common.io.OutputManager;

public abstract class InterpreterCommand extends Command {
    Interpreter interpreter;
    InputManager inputManager;
    OutputManager outputManager;
    Client client;

    public InterpreterCommand(Interpreter interpreter) {

        this.interpreter = interpreter;
        this.inputManager = interpreter.getInputManager();
        this.outputManager = interpreter.getOutputManager();
        this.client = interpreter.getClient();
    }

    public String info() {
        return "";
    }

    ;
}
