package fr.ensma.ia.soundservice.util;

/**
 * Get or close connection with postgresql
 */
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.aeonbits.owner.ConfigCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class JDBCUtil {

	private static ServerConfig cfg = ConfigCache.getOrCreate(ServerConfig.class);
	private static final Logger logger = LogManager.getLogger(JDBCUtil.class);

	public static Connection getPostgreConn () {

		Connection conn = null;
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			logger.error("Could not find \"org.postgresql.Driver\"");
		}

		String pgUrl = "jdbc:postgresql://" + cfg.pgAddress() + ":" + cfg.pgPort() + "/" + cfg.pgDatabase();
		String pgUser = cfg.pgUser();
		String pgPassword = cfg.pgPassword();

		try {
			logger.debug("Connecting to "+pgUrl+"...");
			conn = DriverManager.getConnection(pgUrl, pgUser, pgPassword);
		} catch (SQLException e) {
			logger.error("Could not connect to "+pgUrl, e);
			try {
				conn = DriverManager.getConnection(pgUrl, pgUser, pgPassword);
			} catch (SQLException e1) {
				logger.error("Could not reconnect to the database", e1);
			}
		}

		return conn;
	}

	
	public static void close(ResultSet rs, Statement ps) {

		try {
			if(rs!=null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (ps!=null) {
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	
	public static void close(ResultSet rs, Statement ps, Connection conn) {

		try {
			if(rs!=null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (ps!=null) {
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (conn!=null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void close(Statement ps) {
		try {
			if (ps!=null) {
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void close(Statement ps, Connection conn) {

		try {
			if (ps!=null) {
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (conn!=null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void close(Connection conn) {

		try {
			if (conn!=null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}



	public static List<String> getTableNames(Connection conn) {
		DatabaseMetaData dbMetaData;
		List<String> tables = new ArrayList<String>();

		try {
			dbMetaData = conn.getMetaData();
			String catalog = conn.getCatalog(); //catalog 其实也就是数据库名  
			ResultSet tablesResultSet = dbMetaData.getTables(catalog,null,null,new String[]{"TABLE"});  
			while(tablesResultSet.next()){  
				String tableName = tablesResultSet.getString("TABLE_NAME");  
				tables.add(tableName);
			}  
		} catch (SQLException e) {
			logger.info("could not get primary key");
			e.printStackTrace();
		}
		return tables;

	}

	public static List<String> getPrimarykey(Connection conn,String tableName) {
		ResultSet primaryKeyResultSet;
		List<String> listpk = new ArrayList<String>();
		try {
			primaryKeyResultSet = conn.getMetaData().getPrimaryKeys(conn.getCatalog(),null,tableName);
			while(primaryKeyResultSet.next()){  
				String primaryKeyColumnName = primaryKeyResultSet.getString("COLUMN_NAME");  
				listpk.add(primaryKeyColumnName);
			}  
		} catch (SQLException e) {
			logger.info("could not get primary key");
			e.printStackTrace();
		}
		return listpk;
	}

	public static List<String> getForeinKey(Connection conn,String tableName) {

		ResultSet foreignKeyResultSet;
		List<String> listfk = new ArrayList<String>();
		try {
			foreignKeyResultSet = conn.getMetaData().getImportedKeys(conn.getCatalog(),null,tableName);  
			while(foreignKeyResultSet.next()){  
				String fkColumnName;
				fkColumnName = foreignKeyResultSet.getString("FKCOLUMN_NAME");
				System.out.println("foreignkey name "+fkColumnName);
				listfk.add(fkColumnName);

				String pkTablenName = foreignKeyResultSet.getString("PKTABLE_NAME");  
				System.out.println("foreignkey origin table  "+pkTablenName);
				String pkColumnName = foreignKeyResultSet.getString("PKCOLUMN_NAME");  
				System.out.println("foreignkey name of origin "+pkColumnName);
			}

		} catch (SQLException e) {
			logger.info("could not get foreign key");
			e.printStackTrace();
		}  
		return listfk;
	}  

}
