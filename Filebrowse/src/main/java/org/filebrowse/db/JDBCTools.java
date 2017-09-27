package org.filebrowse.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;

public class JDBCTools {
	public static DataSource ds;
	static {
		Properties properties = new Properties();
		try {
		    //initialSize=10
//		    maxActive=10000
//		            minIdel=5
//		            maxWait=1000000
			properties.setProperty("username", "root");
			properties.setProperty("password", "123456");
			properties.setProperty("url", "jdbc:mysql://localhost:3306/ssm_crud?characterEncoding=utf8");
			properties.setProperty("driverClassName", "com.mysql.jdbc.Driver");
			properties.setProperty("initialSize", "10");
			properties.setProperty("maxActive", "10000");
			properties.setProperty("minIdel", "5");
			properties.setProperty("maxWait", "10000000");
			ds = BasicDataSourceFactory.createDataSource(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void closeResources(Connection con, PreparedStatement ps, ResultSet rs) {
		if (null != con) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (null != rs) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (null != ps) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
