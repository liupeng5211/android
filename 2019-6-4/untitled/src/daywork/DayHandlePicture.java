package daywork;


import util.Db;
import util.NewsUrl;

import java.sql.Connection;

public class DayHandlePicture {
	public static void dayOfPictureWork() {
		Connection connection = Db.getConnect();
		PicturePage picturePage = new PicturePage(connection);
		for (int i = 0; i < NewsUrl.PICTURE_URL.length; i++) {
			picturePage.getUrl(NewsUrl.PICTURE_URL[i]);
		}
//		try {
//			connection.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}

	}

	public static void main(String[] args) {
		DayHandlePicture.dayOfPictureWork();
	}
}
