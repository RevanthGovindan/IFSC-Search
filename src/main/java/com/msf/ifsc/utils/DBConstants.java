package com.msf.ifsc.utils;

public class DBConstants {
	public static final String GET_BANK_BY_IFSC = "SELECT * FROM IFSC_DATA WHERE IFSC = ?";
	public static final String GET_ALL_BANKS = "SELECT DISTINCT(BANK) FROM IFSC_DATA";
	public static final String SEARCH_BANK = "SELECT DISTINCT(BANK) FROM IFSC_DATA WHERE BANK LIKE ?";
}
