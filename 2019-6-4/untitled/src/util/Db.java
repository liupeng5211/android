package util;

import util.GetImageData;

import java.sql.*;

public class Db {
    static Connection conn;
    static String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    //mysql
    static String dbURL = "jdbc:mysql://localhost/mySeveer?user=root&serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false&password=123456";

    //sqlsever
//    static String dbURL = "jdbc:sqlserver://localhost:1433;integratedSecurity=true;" + "DatabaseName=mySeveer";
    public static Connection getConnect() {
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(dbURL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }

    public static void main(String arges[]) {
        conn = Db.getConnect();
        String name = "test";
        String password = "123456";
        String imgdata = "iVBORw0KGgoAAAANSUhEUgAAAEgAAABICAYAAABV7bNHAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAABmJLR0QAAAAAAAD5Q7t/AAAACXBIWXMAAAsTAAALEwEAmpwYAAAMXElEQVR42u2ceZAU9RXHv6+vufZg2QsWORaQQxQxRMGSIKKgIBiViBIlFUutMlUg0Sh4JB7BlBqPsmIsjcSjKhWNwVS0CEQjooFEJAVRY5SAiogQYLnZWXaO7n75o7tnunt6enp6ZglW7aOmtqen+/3e+/T7vd/r368boFd6pVd65espZH5OdKnITiHseVOmDIn89Mn5bQDkSgzoYTDyjx6c0TpiBJSwvoY5iQDIc+45708t38TOR1dde9MJCIkAyA++NvfmwZNrdt/49NxXwtoYNoIi3ZmuURpriNV3//y7t84eWoGunhBhzsIpQ2PN2kM6q+hKHRsJQAmlKKQBvOnPHUuYARYF4cwZdc8BiOLEiCIBQHTCxQ0vMOtgBtb9cdcdAPh4AWIAmZcff2ft3i+OrQYAJcFn3/HsvDn4/3c1AiDd8tSlVyi1mAAAu7YkV/5l2fsbAGTCQBJDGsIAeN/W1L+/Ma1tviALUqwvTVP3ic9t27rnWABDCMbFEUwbrI9k/hVsf8sZhcRJ00e1TJjT/DoJkNNprXvZze9+L3kk/V8A6TCApJCAdADdmz/a/sWHbw9/YPzMlntlCTXTbhj78OoVH1wPIOUyhmwfsa2tTbpy4cR+TYPrhkXjNAyKPkSQqJnACR0A6dyl6ejQ0rw91Slu273t8KdP3/vqXgCa+dFtF8rehjJzwamPCrIeBzM2rtx33+4dR74C0G07pyyppDsQgGgkEhlw9+9nr+jTqowCCJ+8nTzvyTte+zsA1TxOACBe8f0pfc+aPXBqvEGaK8S0GTqpis5agZ/scZFFiNAhptWj0qrUkezL697YuWbVs2uPuGBJCx+cOWnY5NgagHFgV9e/ll7xxmUAdntcsOMCyLAdqDtn1mkTr7rrtFUCAVpG2PHINSvG7dhxJAlAuv+F+RP7DJJvk2rUGaqWtoGwiz8gAGDO75cEGakuaUVyq/bQPT94cRMAbdCg+pofvnD+R0IUA/SsysuWfDBt83s7NgHoNEGGkkoBEYzE3LToiZn3jzizz7UA4+CXeKDzIK9vHxN9RFfSIxgMcDEk5QOyjicQOBvZ/PkH2Vvrm9TJje1YAgCf/G3/U88sXrsUwH4A2UodrFQEAPGGk1raf/z85DXRWrkJJADMYHu3ZzuEAvdDAbK2CACIwKyj62BqzwNXrZ6aTGZ2AAgyYJR0rlJhAJnUgc7DB/Zo/zD26GXYVRpOEA0WwMMd4oZkMnMUFUaOJdXoYsLU2ROaZt00+g+R2sw5lsMFzhaNoGCA/CLIuZ+Q7lTWLL/rn1du3LjlEIwkHjqKKh3FxOtuv2TI6bMa3hGlzACnqTabeiD/lNSVkbetfmX/1JW/fGsXjCQdClLYQpEAiDcsnTPy9GmJDaKsNheF43Y2ZIPl6iJBa2gflZhf39i6/OP1248eb0DSNUu+PWz8jNh6QeJ6XzjsZ1fl+ccTl9kmiXq87eToNTGl9XdbNn2ZRIhiMUySFi+6fHzzxNm1awShPDjVjJ7ADkrcd/JVze+MmzS8ASEColxAQmNjY3zGgjGvkaS12cGUFznBcfnln8BGR9TB8+4+YzmMGYeyfC7nYAIg3/yr6beL8cyZDPYGUwSOf0cr3/Fyz1AS2XPvefGyhShzxqGckJNuvO/SsYPHK7/xNY8ZiliDMc3TMbrxAjTHhyKZPYC0lizbPfaJwoZIf4xruQQj+56LeqUVh1JfQeOsb+TGGoQL6hMNL3+84auDCJiPgpIUmpqaEncvv/A9IaadUgwMAESlOsxsvxM1SmPuJ1XP4M0vH0fHsU9RODCXX/v0S4zEtMG3QBLyk4RHMx1Y+flSdKtHfXVnu/D+4umvfgsBq+ygXUy8/mdTLxGi6ilg877K/THltOaLHXAAQBIUTGibVwEcp0zsd7UDDgDUKS0Y2zy7pCNyAmfc9ItZFyHgVE8QQMLgwfU1/UfTw0EUtsSHee7vGxkIkfJOlZd37HfyETTEBnq3nRgeSNvAMdJjCJiwgwAS59429XxRRv8gbqTVpOdvqp6CnpsiKqHHJ3o0zkLljOdvabXTZktxHWKUBy14bPq5CJCDSwEiAJHWEfKd7ubY4wMAWw/91VPR1oPrwKwXH/mK43J801nH1oPebWw++HZgrQNHJ34CY6XDNw+XIiheet23hp58dt39QRs+kt6DjN6F1vjJEEgCg/HZoXXY2LEc+RlEHxxc+jZl97HNiEt90Dc2CARCVk9hw+7fYtvh9QFwG1rECAak94vPb9+y7wh8krUfPQKgLF522aKBpyoPBcOTb0cUFNTJzehSDyOjdQWKmkI4/jemESGBuNyAzkwHVN0+W+kHOf/bF+8fW/TEgjefhrHi4SmlupjSPEie5+xEbpPdncwQTc/gUHoX0lqyB+AYkta7cCi90wHHtw2XlpYhsatRopv5AaKRIwfWRutoXBAgQQyqHpxgEVKiUST60FktQ1pqwgISJs4aPbzcot73FiQEHK8W3I4Gtctr76QZ7UP8OPgBEvucJI0u5Sy7/pUFM4Bz7K8gAAT/8/oNS4zy41CsmiQAYl2TNKrsxkPDCdKRwnUtv9/ifegUGKM5eTXgV26LYo3WXlUqKBY1IeAE7FoeBjidTIjt8Cl3/AAJoiz061kw1YETNnoYgBwXW0MDkogSPQem5+F4GFOwSya9BiFzEHUe1tV9Hd2dpOtERGAwdBLYfpRbswgGmNjzmhDlDTU3hfymudBIOatYY5AIaDqZk8w2B70s10qUB/alJ0FnhQBWdd/1M99b/ngNInGSa9PHCEx6/hENy1HLX2sp2DyAyFqhInMfG4cyA0RGNqQ8L3NpFGS5RPbCxDgHpp6cDvKaA7dvlq6tBAIy3ekE8k+elJWkQSIjVitAlBjplAsObGpNd6zNooBIcIDNqckBMo0nzrVjLSsTMZjICCKyfCl+p+S1oJj7ZukgBqX9u6gvIFEhnQRCtI4gSoRMSi+IngJHc4DsUUV5R2FGjxcg6zwiGyA2dZJxHpvukh8cFzz23ASIICqC79SrLyCGwNZFVxIEEgVku/PdwOmlLZnYGOagmTuI8xDtcAww7IhCwPY9p9eIrmJ82Dont4N9Ao19YjAAIMPQPHMlZuzLpti7m9i6l2B1O2s/Fx5nfWcwBLKBR75dsunnAoDlwfHslCVu18t+BE+JGk1kbX2X3NGU5wuAbXmj0CEHFbs+IJ+0UXo4L3/RMlh5EOoZRS9IlpsOu9xRBiP/gIo+plBwosGWjfOqJsFrp5KAiqmSTUhqxtkVjO386JXXwzl99m5jr4IK2jaH9Jxe2xDvtLGCJW/dH1ZFD1DJUYIccV1tx9U35o3co5bbPSLdIuuEE0B6qmtZEvYx4LyCiJExs2k9Z7AjodpNcyRX7/xjwaFyc04IOEFuU0oACjZzKCnGgKmmuaBr2Wsfu6587cM5KMZ+cp5LJlgukbB7AE4AQMFFVozu5chJuYrXDsXtl+32gkpkYiIn/B6GA1T5DR1JAWSFc9WvVYixq24hsnzyLmIJAZNxCDhl++T/swZweUEmykZGVrO2Yd3tJhsYbESKRg8TANbBBbTCzUC6IeslaumqdTEHJMnoWmpupZlz/rhzj1us6LE75CwLqgcnyKxk1V+CsybvBQkQJaehnnCKRQ8xdGJHcj7ecIBSEcSVTdaLkkFFy7KrKMzDcX41ayZ3dJE5V+SVgnoQTmlAVRBRNFzXVctX1+1JLnp02MY/59DucUfuf6tSDhz/B816HJAFiRjQ9cKuxXCWBVbdkwfjNZpVB06QOPLNQaxRZWOkvSEJEEQrOoyIyMNxJmYwg3S9oHAqXraGh8M6+YZQMUAMQE+lMruqBYjBINGYhLcnZTscMhO8PTHn5vmLmllYBJYTOam0vgM+73P4RZC6fVPypUqAeC1JCyIg5Kam8znGStB2MtbkW+ioKQEHAD5bf+Al+LwZ5PsA1Ydrd3aMndJ/RKIhMqKa7zIbixRszZ47VjesKQ13LekHJgwcZmDvtq5Xf734vafg81ain9sCgDiA1ssXjbuwfXzjd6JRpb8gVjB15TZYA5h0c8FDYMNE086gT3AXeWhNL0ifxnddFTmVSe38z7uHl7/+zEdvAdgL45Fgz1xUylkRQAxALYAa5B82OhH+A4EgUuxBgAyAJIzI6YbPO61BHLXeYZeRf4/96ywMI1qycL41HRpQmGO/DlLd53l6pVd6pVd6QP4HJMZ+Y6H56sgAAAAldEVYdGRhdGU6Y3JlYXRlADIwMTQtMDktMDVUMTA6NTc6NTgtMDc6MDC8x0HuAAAAJXRFWHRkYXRlOm1vZGlmeQAyMDE0LTA5LTA1VDEwOjU3OjU4LTA3OjAwzZr5UgAAAABJRU5ErkJggg==";
        String myword = "我最牛逼！";
        Statement statement = null;
        try {
            statement = conn.createStatement();
            String sql = "insert into UserData2(name,password,headImage,myword)  values('" + name + "','" + password
                    + "','" + imgdata + "','" + myword + "')";
            statement.execute(sql);
        } catch (Exception e) {
            // TODO: handle exceptionStatement
            e.printStackTrace();
            System.out.println("出现异常");
        }
        System.out.println("已经成功注册");
        String sqlquery = "select * from UserData2 where name = '" + name + "';";
        try {
            ResultSet result = statement.executeQuery(sqlquery);
            while (result.next()) {
                String headimaeg = result.getString("headImage");

                System.out.println("" + headimaeg);
                GetImageData.writeImgStr(headimaeg);

            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
