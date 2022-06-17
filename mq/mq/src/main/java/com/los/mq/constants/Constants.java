package com.los.mq.constants;

public class Constants {

	private Constants() {
		throw new IllegalStateException("Utility class");
	}

	public static final String EMPTY_STRING = "";

	public static final String[] EMPTY_ARRAY = new String[0];

	public static final String SPACE = " ";

	public static final String SLASH = "/";

	public static final String COMMA = ",";

	public static final String DASH = "-";

	public static final Integer ZERO = 0;

	public static final String NEW_LINE = System.lineSeparator();

	public static final String LOG_SEPARATOR = "---------------------------------------------------------------------------------------";

	public static final String RESP_SUCCESS = "S";

	public static final String RESP_FAILED = "F";

}
