package com.lab7.server.responseInterpreter;

import ch.qos.logback.classic.Logger;
import com.lab7.common.dataTransfer.Auth;
import com.lab7.common.dataTransfer.Request;
import com.lab7.common.dataTransfer.Response;
import com.lab7.server.database.AuthDBController;
import com.lab7.server.responseInterpreter.commands.*;
import com.lab7.server.workersManager.WorkersManager;
import com.lab7.server.workersManager.exceptions.NotFoundException;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Interpreter {
    private final WorkersManager manager;
    public final HashMap<String, Command> executors;
    private final ArrayList<String> ignoreAuth;
    private Request request;
    public final AuthDBController controller;
    public static final Logger logger = (Logger) LoggerFactory.getLogger(Interpreter.class);
    public Interpreter(WorkersManager manager, AuthDBController controller) {
        this.manager = manager;
        this.controller = controller;
        this.executors = new HashMap<>();
        this.executors.put("info", new Info(this));
        this.executors.put("show", new Show(this));
        this.executors.put("add", new Add(this));
        this.executors.put("update", new Update(this));
        this.executors.put("remove_by_id", new RemoveById(this));
        this.executors.put("clear", new Clear(this));
        this.executors.put("remove_greater", new RemoveGreater(this));
        this.executors.put("remove_lower", new RemoveLower(this));
        this.executors.put("remove_last", new RemoveLast(this));
        this.executors.put("min_by_status", new MinByStatus(this));
        this.executors.put("filter_by_start_date", new FilterByStartDate(this));
        this.executors.put("filter_greater_than_position", new FilterGreaterThanPosition(this));
        this.executors.put("login", new Login(this));
        this.executors.put("register", new Register(this));
        this.ignoreAuth = new ArrayList<>();
        this.ignoreAuth.add("login");
        this.ignoreAuth.add("register");
    }
    public class RequestExecutor {
        private final Request request;
        public RequestExecutor(Request request){
            this.request = request;
        }

        public Response compute() {
            String method = this.request.method;
            Auth auth = this.request.auth;
            if(!ignoreAuth.contains(method)){
                try {
                    if (!Interpreter.this.controller.checkExistence(auth.login, auth.password)){
                        return new Response(Response.Status.ERROR, Stream.of("Запрос не выполнен. Неверные данные авторизации").collect(Collectors.toCollection(ArrayList::new)), new HashMap<>());
                    }
                }
                catch (SQLException ex){
                    logger.error(ex.getMessage());
                    return new Response(Response.Status.ERROR, Stream.of("Внутренняя ошибка сервера").collect(Collectors.toCollection(ArrayList::new)), new HashMap<>());
                }
            }
            if (Interpreter.this.executors.containsKey(method)) {
                try {
                    return Interpreter.this.executors.get(method).execute(this.request);
                } catch (NotFoundException ex) {
                    logger.error(ex.getMessage());
                }
            }
            return new Response(Response.Status.ERROR, new ArrayList<>(), new HashMap<>());
        }
    }
    public RequestExecutor buildExecutor(Request rq){
        return new RequestExecutor(rq);
    }

    public WorkersManager getManager() {
        return this.manager;
    }


}
