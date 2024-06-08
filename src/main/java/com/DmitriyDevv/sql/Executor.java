package com.DmitriyDevv.sql;

import java.sql.Connection;
import java.sql.SQLException;

public interface Executor {

    void execute(Connection conn) throws SQLException;
}
