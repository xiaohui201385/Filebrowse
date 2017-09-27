package org.filebrowse.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;



public class JdbcDaoImpl<T> implements Dao<T> {
	private QueryRunner queryRunner = null;
	private Class<T> type;

	public JdbcDaoImpl() {
		queryRunner = new QueryRunner();
		type=ReflectionUtils.getSuperGenericType(this.getClass());
	}

	@Override
	public int update(Connection connection, String sql, Object... args) {
		int i = 0;
		try {
			i = queryRunner.update(connection, sql, args);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return i;
	}

	@SuppressWarnings("deprecation")
	@Override
	public T get(Connection connection, String sql, Object... args) {
		try {
			return queryRunner.query(connection, sql, args, new BeanHandler<T>(type));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<T> getForList(Connection connection, String sql, Object... args) {
		List<T> ls = null;
		try {
			ls = queryRunner.query(connection, sql, args, new BeanListHandler<T>(type));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ls;
	}

}
