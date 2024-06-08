package com.DmitriyDevv.dao;

import java.sql.SQLException;
import java.util.List;

public interface ActionsDB<T> {

    List<T> getAll() throws SQLException;
}
