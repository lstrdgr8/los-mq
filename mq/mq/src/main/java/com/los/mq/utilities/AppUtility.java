package com.los.mq.utilities;

import java.sql.Timestamp;
import java.util.Date;

public class AppUtility {

	/**
	 * Get current timestamp
	 * 
	 * @return
	 */
	public static Timestamp getCurrentTimeStamp() {
		Date dt = new Date();
		return new Timestamp(dt.getTime());
	}
}
