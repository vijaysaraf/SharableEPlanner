package com.twosri.dev.util;

import java.util.Calendar;
import java.util.Date;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class BasicUtility {

	public static boolean assertNotNull(String... args) {
		boolean isNull = false;
		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				isNull = isNull || (args[i] == null || args[i].trim().length() <= 0) ? true : false;
			}
		}
		return !isNull;
	}

	public static Date getDate(Cell cell) {
		Date date = null;
		if(cell != null && cell.getType() == CellType.DATE) {
			DateCell dCell=(DateCell)cell;
			date = dCell.getDate();
			log.info("CellType is Date and with "
					+ "value .. {} .. at Row:Column .. {}:{} .. ",cell.getContents(),
					cell.getRow(),cell.getColumn());
		}else{
			log.info("CellType is expected to be Date but found .. {} .. with "
					+ "value .. {} .. at Row:Column .. {}:{} .. ",cell.getType(),cell.getContents(),
					cell.getRow(),cell.getColumn());
		}
		return date;
	}
	public static Date getDate(String strDate) {
		if (strDate == null || strDate.trim().length()<=0)
			return null;
		else {
			System.out.println("Date found as [{"+strDate+"}]");
			String dateParts[] = strDate.trim().split("-");
			Calendar calender = Calendar.getInstance();
			int year = getYear(dateParts[2].trim());
			int month = getMonth(dateParts[2].trim().toUpperCase());
			int day = Integer.valueOf(dateParts[0].trim());
			calender.set(year, month, day, 0, 0, 0);
			return calender.getTime();
		}
	}

	private static int getYear(String yearStr) {
		int year = 1900;
		if (yearStr.length() == 2)
			year = 100 + Integer.valueOf(yearStr);
		else if (yearStr.length() == 4)
			year = Integer.valueOf(yearStr) - 1900;
		return year;
	}

	private static int getMonth(String monthStr) {
		String months[] = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };
		for (int i = 0; i < months.length; i++) {
			if (months[i].equals(monthStr))
				return i;
		}
		return -1;
	}

	public static void main(String... args) {
		System.out.println(assertNotNull(new String[] { "s", "w", null, "sss" }));
	}
}
