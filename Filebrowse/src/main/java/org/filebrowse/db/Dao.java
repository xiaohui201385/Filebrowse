package org.filebrowse.db;

import java.sql.Connection;
import java.util.List;


public interface Dao<T> {
	
	int update(Connection connection, String sql, Object... args);


	T get(Connection connection, String sql, Object... args);

	
	List<T> getForList(Connection connection, String sql, Object... args);
}
