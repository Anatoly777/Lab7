package com.lab7.server.workersManager;


import ch.qos.logback.classic.Logger;
import com.lab7.common.lab.Position;
import com.lab7.common.lab.Status;
import com.lab7.common.lab.Worker;
import com.lab7.server.database.WorkersDBController;
import com.lab7.server.workersManager.exceptions.IllegalDataAccessException;
import com.lab7.server.workersManager.exceptions.NotFoundException;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class WorkersManager {
    private ArrayList<Worker> workers;
    public final LocalDateTime creationTime;
    private WorkersDBController controller;
    private HashMap<Long, String> keyOwnerRelating;
    public static final Logger logger = (Logger) LoggerFactory.getLogger(WorkersManager.class);

    public WorkersManager(WorkersDBController controller) {
        this.workers = new ArrayList();
        this.creationTime = LocalDateTime.now();
        this.controller = controller;
        this.load();
    }

    public synchronized ArrayList<Worker> getWorkers () {
        return (ArrayList<Worker>) this.workers.clone();
    }

    public synchronized void clear(String owner) throws SQLException {
        for(Worker key: getWorkers()){
            if(this.controller.checkOwner(key.id, owner)){
                controller.remove(key.id);
                this.workers.remove(key);
            }
        }
        this.updateOwnerRelating();

    }

    public synchronized Worker add(Worker w, String owner) throws SQLException{
        w.creationDate = LocalDate.now();
        controller.add(w, owner);
        workers.add(w);
        this.updateOwnerRelating();
        return w;
    }

    public synchronized Long minByStatus() {

        Worker test = workers.stream()
                .filter(x -> x.getStatus() == Status.FIRED ||
                        x.getStatus() == Status.PROBATION ||
                        x.getStatus() == Status.HIRED ||
                        x.getStatus() == Status.REGULAR ||
                        x.getStatus() == Status.RECOMMENDED_FOR_PROMOTION)
                .findFirst().orElse(null);
        return ((test).getId());
    }

    public synchronized void removeGreater(Float salary, String owner) {
        workers.stream().filter(x -> x.getSalary() < salary).collect(Collectors.toSet()).forEach(p ->{
            try {
                if (this.controller.checkOwner(p.id, owner)) {
                    this.controller.remove(p.id);
                    this.workers.remove(p);
                    this.updateOwnerRelating();
                }
            } catch (SQLException ex) {
                logger.error(ex.getMessage());
            }
        });
        Collections.sort(workers);
    }

    public synchronized void removeLower(Float salary, String owner) {
        workers.stream().filter(x -> x.getSalary() > salary).collect(Collectors.toSet()).forEach(p ->{
            try {
                if (this.controller.checkOwner(p.id, owner)) {
                    this.controller.remove(p.id);
                    this.workers.remove(p);
                    this.updateOwnerRelating();
                }
            } catch (SQLException ex) {
                logger.error(ex.getMessage());
            }
        });
        Collections.sort(workers);
    }

    public synchronized void removeLast(String owner){
        int l = workers.size();
        Worker p = workers.get(l - 1);
        Long last_id = p.id;
        try {
            while (!this.controller.checkOwner(last_id, owner)){
                l--;
                p = workers.get(l - 1);
                last_id = p.id;
                if (l == 0){
                    return;
                }
            }
            this.controller.remove(p.id);
            this.workers.remove(p);
            this.updateOwnerRelating();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
        Collections.sort(workers);
    }

    public synchronized Long update(Long id, Worker w, String owner) throws IllegalDataAccessException, SQLException {
        Optional<Worker> okey = workers.stream().filter(p -> id == p.id).findFirst();
        if (!okey.isPresent()) try {
            throw new NotFoundException();
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage());
        }
            if (!this.controller.checkOwner(id, owner))
                throw new IllegalDataAccessException();
            this.controller.remove(id);
            this.add(w, owner);
            this.controller.update(id, w);
            this.updateOwnerRelating();
        return id;
    }

    public synchronized void removeById(Long id, String owner) {
        Optional<Worker> okey = workers.stream().filter(p -> id == p.id).findFirst();
        if (!okey.isPresent()) return;
        try {
            if (!this.controller.checkOwner(id, owner)) throw new IllegalDataAccessException();
            this.controller.remove(okey.get().id);
            this.workers.remove(okey.get());
            this.updateOwnerRelating();
        } catch (SQLException | IllegalDataAccessException ex) {
            logger.error(ex.getMessage());
        }
    }

    public synchronized ArrayList<String> filterByStartDate(LocalDate start_date){
        ArrayList<String> messages = new ArrayList<>();
        Collections.sort(workers);
        List<Worker> test = workers.stream()
                .filter(x -> x.getEndDate().equals(start_date))
                .collect(Collectors.toList());
        for (Worker element : test) {
            messages.add(workerAsString(element));
        }
        return messages;
    }

    public synchronized ArrayList<String> filterGreaterThanPosition(Position position){
        ArrayList<String> messages = new ArrayList<>();
        Collections.sort(workers);
        List<Worker> test = workers.stream()
                .filter(x -> x.getPosition().compareTo(position) > 0)
                .collect(Collectors.toList());
        for (Worker element : test) {
            messages.add(workerAsString(element));
        }
        return messages;
    }

    public String workerAsString(Worker w) {
        String str = "";
        str += "Имя: " + w.name + "\n";
        str += "Зарплата: " + w.salary + "\n";
        str += "id: " + w.id + "\n";
        str += "Дата создания: " + w.creationDate.toString() + "\n";
        str += "Дата заключения контракта: " + w.endDate.toString() + "\n";
        str += "Должность: " + w.position.toString() + "\n";
        str += "Статус: " + w.status.toString() + "\n";
        return str;

    }

    public void load() {
        try{
            this.workers = controller.getWorkers();
            this.keyOwnerRelating = controller.getKeyOwnerRelations();

        }
        catch(SQLException ex){
            logger.error(ex.getMessage());
        }

    }

    private void updateOwnerRelating() throws SQLException{
        this.keyOwnerRelating = controller.getKeyOwnerRelations();
    }

}
