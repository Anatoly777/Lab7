package com.lab7.server.responseInterpreter.commands;

import ch.qos.logback.classic.Logger;
import com.lab7.common.dataTransfer.Request;
import com.lab7.common.dataTransfer.Response;
import com.lab7.server.responseInterpreter.Interpreter;
import com.lab7.server.workersManager.WorkersManager;
import com.lab7.server.workersManager.exceptions.NotFoundException;
import org.slf4j.LoggerFactory;

public abstract class Command {
    Interpreter interpreter;
    WorkersManager manager;
    public static final Logger logger = (Logger) LoggerFactory.getLogger(Command.class);
    public Command(Interpreter interpreter) {
        this.interpreter = interpreter;
        this.manager = this.interpreter.getManager();
    }

    public abstract Response execute(Request rq) throws NotFoundException;

}
