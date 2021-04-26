package com.axeelheaven.simplechat.storage.types;

import java.sql.*;

import com.axeelheaven.simplechat.storage.Database;


public class MySQL implements Database {

    private final String host;
    private final int port;
    private final String database;
    private final String user;
    private final String password;
    private Connection connection;

    public MySQL(final String host, final int port, final String database, final String user, final String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public Connection getConnection() {
        return connection;
    }

    public synchronized void connect(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", user, password);
	        connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?user=" + user + "&password=" + password);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}

    }

    public synchronized void checkConnection(int timeout) throws SQLException {
        if (connection == null) {
            connect();
        } else {
            boolean connectionValid = false;

            try {
                connectionValid = connection.isValid(timeout);
            } catch (SQLException e) {
                e.printStackTrace();
                connect();
                connectionValid = true;
            }

            if (!connectionValid) {
                connect();
            }
        }
    }

    public synchronized ResultSet query(String query) throws SQLException {
        return connection.prepareStatement(query).executeQuery();
    }

    public synchronized boolean update(String query) throws SQLException {
        return connection.prepareStatement(query).execute();
    }

    public synchronized void close() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    public synchronized boolean hasConnection(boolean checkOpen, boolean checkValid, int timeout) throws SQLException {
    	return this.connection != null;
        //return connection != null && (!checkOpen || !connection.isClosed()) && (!checkValid || connection.isValid(timeout));
    }

    @Override
    public String toString() {
        return host + ":" + port + ", " + database + ", " + user + ", " + password;
    }
}
