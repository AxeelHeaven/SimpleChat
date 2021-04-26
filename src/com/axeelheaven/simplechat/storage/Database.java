package com.axeelheaven.simplechat.storage;

import java.io.IOException;
import java.sql.*;

public abstract interface Database {

	public abstract String getHost();

    public abstract int getPort();

    public abstract String getDatabase();

    public abstract String getUser();

    public abstract String getPassword();

    public abstract Connection getConnection();

    public abstract void connect() throws SQLException, IOException, ClassNotFoundException;

    public abstract void checkConnection(int timeout) throws SQLException, IOException, ClassNotFoundException;

    public abstract ResultSet query(String query) throws SQLException;

    public abstract boolean update(String query) throws SQLException;

    public abstract void close() throws SQLException;

    public abstract boolean hasConnection(boolean checkOpen, boolean checkValid, int timeout) throws SQLException;

    @Override
    public abstract String toString();
	
	
}
