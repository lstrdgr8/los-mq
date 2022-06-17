package com.los.mq.utilities;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.List;
import com.los.mq.constants.Constants;

public class Utility {

	private Utility() {
		throw new IllegalStateException("Utility class");
	}

	public static String getStr(Object obj) {
		if (obj != null) {
			return obj.toString().trim();
		} else {
			return Constants.EMPTY_STRING;
		}
	}

	/**
	 * This method will return a trim string, if object passing is null it will
	 * return null. This function needed as JMS functions need to maintain the null
	 * values rather than empty string
	 *
	 * @param obj Object that are passed in
	 * @return a string representation of the object
	 */
	public static String getStrWithNull(Object obj) {
		if (obj != null) {
			return obj.toString().trim();
		} else {
			return null;
		}
	}

	/**
	 * * This method will return a trim UPPERCASE string, if object passing is null
	 * it will return EMPTY STRING("").
	 *
	 * @param obj Object that are passed in
	 * @return a string representation of the object
	 */
	public static String getStrUpper(Object obj) {
		if (obj != null) {
			return obj.toString().trim().toUpperCase();
		} else {
			return Constants.EMPTY_STRING;
		}
	}

	/**
	 * This method is same with getStrWithNull() The returned string will be either
	 * in UPPERCASE or null values.
	 *
	 * @param obj Object that are passed in
	 * @return a string representation of the object with UPPERCASE
	 */
	public static String getStrUpperWithNull(Object obj) {
		if (obj != null && !getStr(obj).equalsIgnoreCase("NULL")) {
			return obj.toString().trim().toUpperCase();
		} else {
			return null;
		}
	}

	/**
	 * Gets the str lower.
	 *
	 * @param obj the obj
	 * @return the str lower
	 */
	public static String getStrLower(Object obj) {
		if (obj != null) {
			return obj.toString().trim().toLowerCase();
		} else {
			return Constants.EMPTY_STRING;
		}
	}

	/**
	 * This method will return string to Title Case.
	 *
	 * @param obj the obj
	 * @return String in Title Case
	 */
	public static String getStrTitle(Object obj) {
		String title = Constants.EMPTY_STRING;
		if (obj != null) {
			String modStr = obj.toString().trim().toLowerCase();
			String[] strArr = modStr.split(Constants.SPACE);
			StringBuilder newStr = new StringBuilder();
			for (int i = 0; i < strArr.length; i++) {
				strArr[i] = Character.toUpperCase(strArr[i].charAt(0)) + strArr[i].substring(1);
				newStr.append(strArr[i] + Constants.SPACE);
			}
			title = newStr.toString();
		}
		return title;
	}

	/**
	 * Gets the int.
	 *
	 * @param obj the obj
	 * @return the int
	 */
	public static Integer getInt(Object obj) {
		if (obj != null) {
			try {
				return Integer.parseInt(obj.toString().trim());
			} catch (Exception e) {
				return Constants.ZERO;
			}
		} else {
			return Constants.ZERO;
		}
	}

	/**
	 * This method will return a double value.
	 *
	 * @param obj the number to obtain double value
	 * @return the double
	 */
	public static Double getDouble(Object obj) {
		return getDouble(obj, null);
	}

	/**
	 * This method will return a double value based on decimal format pattern.
	 *
	 * @param obj     the number to obtain double value
	 * @param pattern decimal format pattern. eg. ##.##
	 * @return the double
	 */
	public static Double getDouble(Object obj, String pattern) {
		if (obj != null) {
			try {
				double d = Double.parseDouble(obj.toString().trim());

				if (!isObjNull(pattern)) {
					DecimalFormat df = new DecimalFormat(pattern);
					return Double.parseDouble(df.format(d));
				} else {
					return d;
				}
			} catch (Exception e) {
				return (double) 0;
			}
		} else {
			return (double) 0;
		}
	}

	/**
	 * Convert Object value to BigDecimal value. Object can be any String, Double,
	 * Integer, Long, etc
	 *
	 * @param input Object which need to be convert to BigDecimal
	 * @return A BigDecimal value for specified Object
	 */
	public static BigDecimal getBigDecimal(Object input) {
		try {
			if (input instanceof String) {
				return new BigDecimal((String) input);
			} else if (input instanceof Double) {
				return BigDecimal.valueOf((Double) input);
			} else if (input instanceof Integer) {
				return new BigDecimal((Integer) input);
			} else if (input instanceof Long) {
				return new BigDecimal((Long) input);
			} else if (input instanceof BigInteger) {
				return new BigDecimal((BigInteger) input);
			} else if (input instanceof Float) {
				return BigDecimal.valueOf((Float) input);
			} else if (input instanceof Short) {
				return new BigDecimal((Short) input);
			} else if (input instanceof BigDecimal) {
				return (BigDecimal) input;
			} else {
				return new BigDecimal(Constants.ZERO);
			}
		} catch (Exception e) {
			return new BigDecimal(Constants.ZERO);
		}
	}

	/**
	 * Gets the list size.
	 *
	 * @param o the o
	 * @return the list size
	 */
	public static Integer getListSize(List<?> o) {
		Integer size = Constants.ZERO;
		if (!o.isEmpty()) {
			size = o.size();
		}
		return size;
	}

	/**
	 * Checks if is list null.
	 *
	 * @param o the o
	 * @return the boolean
	 */
	public static Boolean isListNull(List<?> o) {
		boolean isListNull = true;
		if (o != null && getListSize(o) > 0) {
			isListNull = false;
		}
		return isListNull;
	}

	/**
	 * Checks if is list null zero.
	 *
	 * @param o the o
	 * @return the boolean
	 */
	public static Boolean isListNullZero(List<?> o) {
		boolean isListNullZero = false;
		if (o == null || (o != null && o.isEmpty())) {
			isListNullZero = true;
		}
		return isListNullZero;
	}

	/**
	 * Checks if is list zero.
	 *
	 * @param o the o
	 * @return the boolean
	 */
	public static Boolean isListZero(List<?> o) {
		boolean isListZero = false;
		if (o != null && o.isEmpty()) {
			isListZero = true;
		}
		return isListZero;
	}

	/**
	 * Checks if is equals.
	 *
	 * @param oriSrc     the ori src
	 * @param compareSrc the compare src
	 * @return true, if is equals
	 */
	public static boolean isEquals(String oriSrc, String compareSrc) {
		boolean isEquals = false;
		if (oriSrc != null && getStr(oriSrc).equals(getStr(compareSrc))) {
			isEquals = true;
		}
		return isEquals;
	}

	/**
	 * Checks if is equals case ignore.
	 *
	 * @param oriSrc     the ori src
	 * @param compareSrc the compare src
	 * @return true, if is equals case ignore
	 */
	public static boolean isEqualsCaseIgnore(String oriSrc, String compareSrc) {
		boolean isEqualsCaseIgnore = false;
		if (oriSrc != null && getStr(oriSrc).equalsIgnoreCase(getStr(compareSrc))) {
			isEqualsCaseIgnore = true;
		}
		return isEqualsCaseIgnore;
	}

	/**
	 * Checks if is equals any.
	 *
	 * @param oriSrc     the ori src
	 * @param compareSrc the compare src
	 * @return true, if is equals any
	 */
	public static boolean isEqualsAny(String oriSrc, String compareSrc) {
		boolean isEqual = false;
		String[] compareSrcArr = compareSrc.split(",");
		for (String element : compareSrcArr) {
			if (oriSrc != null && getStr(oriSrc).equals(getStr(element))) {
				isEqual = true;
				break;
			}
		}

		return isEqual;
	}

	/**
	 * Checks if is equals case ignore any.
	 *
	 * @param oriSrc     the ori src
	 * @param compareSrc the compare src
	 * @return true, if is equals case ignore any
	 */
	public static boolean isEqualsCaseIgnoreAny(String oriSrc, String compareSrc) {
		boolean isEqual = false;
		String[] compareSrcArr = compareSrc.split(",");
		for (String element : compareSrcArr) {
			if (oriSrc != null && getStr(oriSrc).equalsIgnoreCase(getStr(element))) {
				isEqual = true;
				break;
			}
		}

		return isEqual;
	}

	/**
	 * Checks if is obj null.
	 *
	 * @param obj the obj
	 * @return true, if is obj null
	 */
	public static boolean isObjNull(Object obj) {
		boolean isObjNull = true;
		if (obj != null && getStr(obj).length() > 0) {
			isObjNull = false;
		}
		return isObjNull;
	}

	/**
	 * Checks if is max no reached.
	 *
	 * @param supplyNo  the supply no
	 * @param compareNo the compare no
	 * @return the boolean
	 */
	public static Boolean isMaxNoReached(Integer supplyNo, Integer compareNo) {
		return (supplyNo > compareNo);
	}

	/**
	 * Copies a range of characters into a new String.
	 *
	 * @param input String need to be sub string
	 * @param start the offset of the first character
	 * @param end   the offset one past the last character
	 * @return a new String containing the characters from start to end - 1
	 */
	public static String subString(String input, int start, int end) {
		if (input != null) {
			return getStr(input).substring(start, end);
		} else {
			return Constants.EMPTY_STRING;
		}
	}

	/**
	 * Copies a range of characters into a new String.
	 *
	 * @param input String need to be sub string
	 * @param start the offset of the first character
	 * @return a new String containing the characters from start to the end of the
	 *         string
	 */
	public static String subString(String input, int start) {
		if (input != null) {
			return getStr(input).substring(start);
		} else {
			return Constants.EMPTY_STRING;
		}
	}

	/**
	 * Check if the email address is valid (Required JavaMail class).
	 *
	 * @param email The email address to be checked
	 * @return True is valid email address, False otherwise
	 */
	public static boolean isValidEmailAddress(String email) {
		boolean result = false;
		if (!isObjNull(email)) {
			result = email.matches("/[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}/");
		}
		return result;
	}

	/**
	 * checking string null empty and.
	 *
	 * @param obj the obj
	 * @return boolean
	 */
	public static boolean isStringNull(String obj) {
		boolean isObjNull = true;
		if ((obj != null && getStr(obj).length() > 0) && !obj.equalsIgnoreCase("NULL")) {
			isObjNull = false;
		}
		return isObjNull;
	}
}
