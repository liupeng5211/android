package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class insertVideo {
    public static void insert(Connection connection, String id, String descripe, String imgsrc, String videoUrl, String playUrl, String time) {

        Connection conn = connection;
        try {
            Statement statement = conn.createStatement();
            String querySql = "select * from news_video where id = " + "'" + id + "';";
            ResultSet resultSet = statement.executeQuery(querySql);
            if (resultSet.next()) {
                // repeat!

                return;

            }
            String sql = "insert into news_video values('" + id + "','"+ descripe + "','" + imgsrc + "','" + videoUrl + "','"
                    + playUrl + "','" + time + "');";
            statement.execute(sql);
            resultSet.close();
            statement.close();


        } catch (SQLException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        }

    }


}
