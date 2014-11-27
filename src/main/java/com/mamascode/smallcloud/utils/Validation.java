package com.mamascode.smallcloud.utils;

import java.sql.Date;
import java.util.GregorianCalendar;

public class Validation {
	// String check /////////////////////////////////////////////////////////////////

	// checkStringLength
	public static boolean checkStringLength(String str, int minLen, int maxLen) {
		return str.getBytes().length <= maxLen
				&& str.getBytes().length >= minLen;
	}

	// notNullStringLength
	public static boolean notNullStringLength(String str, int maxLen) {
		return str != null && str.getBytes().length <= maxLen
				&& str.getBytes().length >= 1;
	}

	// notNullString
	public static boolean notNullString(String str) {
		return str != null;
	}

	// notEmptyString
	public static boolean notEmptyString(String str) {
		return str != null && !str.isEmpty();
	}

	// notNullStrings
	public static boolean notNullStrings(String[] strs) {
		for (String str : strs) {
			if (str == null)
				return false;
		}

		return true;
	}

	// Integer check //////////////////////////////////////////////////////////////////

	// checkIntegerSize
	public static boolean checkIntegerSize(long num, long minNum, int maxNum) {
		return num <= maxNum && num >= minNum;
	}

	// ProcrustesBed
	public static long ProcrustesBed(long num, long minNum, long maxNum) {
		if (num > maxNum)
			return maxNum;
		else if (num < minNum)
			return minNum;
		else
			return num;
	}

	// ProcrustesBed
	public static long ProcrustesBedHead(long num, long minNum) {
		if (num < minNum)
			return minNum;
		else
			return num;
	}

	// ProcrustesBed
	public static long ProcrustesBedFoot(long num, long maxNum) {
		if (num > maxNum)
			return maxNum;
		else
			return num;
	}
	
	// Object check /////////////////////////////////////////////////////////////////////

	// notNullObject
	public static boolean notNullObject(Object obj) {
		return obj != null;
	}

	// isThisObjectType
	public static boolean isThisObjectType(Object obj, String type) {
		return obj.getClass().getName().equals(type);
	}
	
	// filtering ////////////////////////////////////////////////////////////////////////

	// replaceHtmlCharacter
	public static String replaceHtmlCharacter(String str) {
		String retStr = str;
		retStr = retStr.replace("<", "&lt;");
		retStr = retStr.replace(">", "&gt;");

		return retStr;
	}
	
	// type transformation ////////////////////////////////////////////////////////////////

	// strToDate
	public static Date strToDate(String str, String discrimitor) {
		String[] hireDateStrs = str.split(discrimitor, 3);
		
		if(hireDateStrs.length == 3) {
			int year = Integer.parseInt(hireDateStrs[0]);
			int month = Integer.parseInt(hireDateStrs[1]) - 1;
			int day = Integer.parseInt(hireDateStrs[2]);
			
			return new Date(new GregorianCalendar(year, month, day).getTimeInMillis());
		} else {
			String exceptionMsg = new StringBuilder("can't cast string [")
					.append(str).append("] to date type").toString();
			
			throw new ClassCastException(exceptionMsg);
		}
	}
}
