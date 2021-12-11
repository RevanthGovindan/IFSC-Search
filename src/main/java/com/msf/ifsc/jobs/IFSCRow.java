package com.msf.ifsc.jobs;

public class IFSCRow {
	private String bank;
	private String ifsc;
	private String branch;
	private String address;
	private String city1;
	private String city2;
	private String state;
	private int stdCode;
	private String phone;

	public IFSCRow() {
	}

	public IFSCRow(String bank, String ifsc, String branch, String address, String city1, String city2, String state,
			int stdCode, String phone) {
		this.bank = bank;
		this.ifsc = ifsc;
		this.branch = branch;
		this.address = address;
		this.city1 = city1;
		this.city2 = city2;
		this.state = state;
		this.stdCode = stdCode;
		this.phone = phone;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity1() {
		return city1;
	}

	public void setCity1(String city1) {
		this.city1 = city1;
	}

	public String getCity2() {
		return city2;
	}

	public void setCity2(String city2) {
		this.city2 = city2;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getStdCode() {
		return stdCode;
	}

	public void setStdCode(int stdCode) {
		this.stdCode = stdCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
