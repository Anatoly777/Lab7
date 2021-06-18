package com.lab7.server.database;

import com.lab7.common.Utils.ObjectSerializer;
import com.lab7.common.lab.Worker;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class WorkersDBController extends DBController{
    public Worker add(Worker worker, String owner) throws SQLException{
        worker.id = 1 + (long) (Math.random() * (Long.MAX_VALUE - 1));
        byte[] workerBytes = ObjectSerializer.serialize(worker);
        PreparedStatement s = this.connection.prepareStatement("INSERT INTO workers VALUES(?,?,?)");

        Statement id_statement = this.connection.createStatement();
        id_statement.executeQuery("SELECT nextval('workers_id_seq')");
        ResultSet id_res = id_statement.getResultSet();
        id_res.next();
        s.setLong(1,worker.id);
        s.setBytes(2, workerBytes);
        s.setString(3, owner);
        s.executeUpdate();
        return worker;
    }
    public ArrayList<Worker> getWorkers() throws SQLException{
        ArrayList<Worker> parsed = new ArrayList<>();
        Statement s = this.connection.createStatement();
        s.executeQuery("SELECT * FROM workers");
        ResultSet resultSet = s.getResultSet();
        try {
            ByteArrayInputStream bas;
            ObjectInputStream objectInputStream;
            while (resultSet.next()) {
                byte[] bytes = resultSet.getBytes(2);
                bas = new ByteArrayInputStream(bytes);
                objectInputStream = new ObjectInputStream(bas);
                Worker w = (Worker)objectInputStream.readObject();
                parsed.add(w);

            }
        }
        catch(IOException | ClassNotFoundException ex){
            logger.error(ex.getMessage());
        }
        return parsed;

    }
    public HashMap<Long, String> getKeyOwnerRelations() throws SQLException{
        HashMap<Long, String> parsed = new HashMap<>();
        Statement s = this.connection.createStatement();
        s.executeQuery("SELECT key, owner FROM workers");
        ResultSet resultSet = s.getResultSet();

            while (resultSet.next()) {
                Long key = resultSet.getLong(1);
                String owner = resultSet.getString(2);
                parsed.put(key, owner);
        }

        return parsed;

    }
    public boolean checkOwner(Long key, String owner) throws SQLException{
        PreparedStatement s = this.connection.prepareStatement("SELECT * FROM workers WHERE key = ? AND owner = ?");
        s.setLong(1,key);
        s.setString(2,owner);
        s.executeQuery();
        ResultSet res = s.getResultSet();

        return res.next();

    }
    public boolean remove(Long key) throws SQLException{
        PreparedStatement s = this.connection.prepareStatement("DELETE FROM workers WHERE key = ?");
        s.setLong(1, key);
        return s.executeUpdate() != 0;
    }
    public void update(Long key, Worker worker) throws SQLException{

        byte[] workerBytes = ObjectSerializer.serialize(worker);
        PreparedStatement s = this.connection.prepareStatement("UPDATE workers SET worker = ? WHERE key = ?");
        s.setBytes(1, workerBytes);
        s.setLong(2, key);
    }
    @Override
    void tableConnect() throws SQLException {
        Statement s = this.connection.createStatement();
        try{
            s.executeUpdate("CREATE SEQUENCE IF NOT EXISTS workers_id_seq  MINVALUE 1 ");
            s.executeUpdate("CREATE TABLE workers(" +
                    "key BIGINT PRIMARY KEY," +
                    "worker BYTEA, " +
                    "owner VARCHAR(50)" +
                    ")");

        }
        catch (SQLException ex){
            logger.error(ex.getMessage());
        }

    }
}
