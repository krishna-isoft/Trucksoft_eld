package com.trucksoft.isoft.isoft_elog.Isoft_adapter;

public class ReportDetailsBean {
	
	private String ondutty;
	private String offdutty;
	private String sleep;
	private String drive;


	private String date;
	private String certify;
	private String certifydate;
	private String oddrvtime;

	private String trip_num;

	
	
	
	
	
	
	
	

	public void setOffdutye(String on_line) {
		this.offdutty = on_line;
	}
	public String getOffduty() {
		return offdutty;
	}



	public void setdrivee(String on_line) {
		this.drive = on_line;
	}
	public String getDrive() {
		return drive;
	}

	public void setOndutye(String on_line) {
		this.ondutty = on_line;
	}
	public String getOOnduty() {
		return ondutty;
	}

	public void setsleep(String sleep ) {
		this.sleep = sleep;
	}
	public String getsleep() {
		return sleep;
	}


	public void setdate(String sleep ) {
		this.date = sleep;
	}
	public String getdate() {
		return date;
	}

	public String getcertify()
	{
		return certify;
	}
	public void setcertify(String certify ) {
		this.certify = certify;
	}
	public String getCertifydate()
	{
		return certifydate;
	}
	public void setCertifydate(String certify ) {
		this.certifydate = certify;
	}

	public String getOddrvtime()
	{
		return oddrvtime;
	}
	public void setOddrvtime(String odr ) {
		this.oddrvtime = odr;
	}

	public String getTrip_num()
	{
		return trip_num;

	}
	public void setTrip_num(String trip)
	{
		this.trip_num=trip;
	}
}
