package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class insertPicture {
    public static void insert(Connection connection, String title, String url, String imgurl, String time) {

        try {
            Statement statement = connection.createStatement();
            String querySql = "select * from news_picture1 where title = " + "'" + title + "';";
            ResultSet resultSet = statement.executeQuery(querySql);
            if (resultSet.next()) {
                // repeat!

                return;

            }
            String sql = "insert into news_picture1 values('" + title + "','" + url + "','" + imgurl + "','" + time
                    + "');";
            statement.execute(sql);
            resultSet.close();
            statement.close();


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        insertPicture.insert(Db.getConnect(), "这是一条 测试新闻", "www.url.com", "www.img.com", "2018年12月15日");
    }
}
