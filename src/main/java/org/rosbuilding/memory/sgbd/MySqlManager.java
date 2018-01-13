/*
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.rosbuilding.memory.sgbd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.rosbuilding.memory.tsdb.DetectNode;

public class MySqlManager {
    static final String DB_NAME = "alfred_www";
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/" + DB_NAME;

//  Database credentials
    static final String USER = "devel";
    static final String PASS = "rt2135mg";

    static {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getId(DetectNode node) {
//    	Add Pool...
//    	DataSource ds =
        Connection conn = null;
        Statement stmt = null;
        try{
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String sql = "select n.id, z.id, w.id\n" +
                    "from alf_node as n \n" +
                    "LEFT OUTER join alf_world as w on w.id = n.world_id\n" +
                    "LEFT OUTER join alf_zone as z on z.id = n.zone_id\n" +
                    "where n.endpoint = '" + node.getTopic() + "';";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                //Retrieve by column name
                int nid  = rs.getInt("n.id");
                int zid  = rs.getInt("z.id");
                int wid  = rs.getInt("w.id");
                node.setId(nid, zid, wid);
            }
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if (stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if (conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
    }
}
