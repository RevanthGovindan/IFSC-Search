package com.msf.ifsc.utils;

public class DBConstants {
	public static final String GET_BANK_BY_IFSC = "SELECT * FROM IFSC_DATA WHERE IFSC = ?";
	public static final String GET_ALL_BANKS = "SELECT DISTINCT(BANK) FROM IFSC_DATA";
	public static final String SEARCH_BANK = "SELECT DISTINCT(BANK) FROM IFSC_DATA WHERE BANK LIKE ? LIMIT 30";
	public static final String SEARCH_BRANCH = "SELECT BRANCH,CITY2,STATE FROM IFSC_DATA WHERE BANK = ? and BRANCH LIKE ? LIMIT 30";
	public static final String GET_IFSC = "SELECT IFSC FROM IFSC_DATA WHERE BANK = ? and BRANCH = ?";
}
