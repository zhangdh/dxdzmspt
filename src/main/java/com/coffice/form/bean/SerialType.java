package com.coffice.form.bean;

import java.util.Calendar;

import com.coffice.util.Db;

public class SerialType
{
  public static final String AUTO = "0";
  public static final String YEAR = "1";
  public static final String MONTH = "2";
  public static final String DAY = "3";
  private String id;
  private int places;
  private String serialReg;
  private double currentSerialNo;
  private String type;

  public String getId()
  {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getPlaces() {
    return this.places;
  }

  public void setPlaces(int places) {
    this.places = places;
  }

  public String getSerialReg() {
    return this.serialReg;
  }

  public void setSerialReg(String serialReg) {
    this.serialReg = serialReg;
  }

  public double getCurrentSerialNo() {
    return this.currentSerialNo;
  }

  public void setCurrentSerialNo(double currentSerialNo) {
    this.currentSerialNo = currentSerialNo;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }
/*
  private String generateSemiSerial() {
    Calendar now = Calendar.getInstance();
    String year = String.valueOf(now.get(1));
    String month = 
      now.get(2) + 1;

    String day = String.valueOf(now.get(5));
    String result = "";
    result = getSerialReg().replace("YYYY", year);
    result = result.replace("MM", month);
    result = result.replace("DD", day);
    result = result.replace("YY", year.substring(2));
    return result;
  */
  private String generateSemiSerial() {
		Calendar now = Calendar.getInstance();
		String year = String.valueOf(now.get(Calendar.YEAR));
		String month = (now.get(Calendar.MONTH) + 1 < 10) ? "0"
				+ (now.get(Calendar.MONTH) + 1) : (now.get(Calendar.MONTH) + 1)
				+ "";
		// String day = String.valueOf(now.get(Calendar.DATE));
	    String day = (now.get(Calendar.DATE) < 10) ? "0"
						+ (now.get(Calendar.DATE) ) : (now.get(Calendar.DATE))
						+ "";
		String result = "";
		result = getSerialReg().replace("YYYY", year);
		result = result.replace("MM", month);
		result = result.replace("DD", day);
		result = result.replace("YY", year.substring(2));
		return result;
	}
  public String generateSerial(int count) {
    String semi = generateSemiSerial();
    if ("0".equalsIgnoreCase(getType())) {
      setCurrentSerialNo(getCurrentSerialNo());
    } else {
      String update_bz = "";
      update_bz = (String)Db.getJtA().queryForObject("select update_bz from t_form_customserial where id=?", new Object[] { this.id }, String.class);
      if ((count == 0) && (!("1".equals(update_bz))))
        setCurrentSerialNo(1D);
      else
        setCurrentSerialNo(getCurrentSerialNo());

    }

    String fixed = String.valueOf((int)this.currentSerialNo);
    int l = getPlaces() - fixed.length();
    if (l > 0)
      for (int i = 0; i < l; ++i)
        fixed = "0" + fixed;


    semi = semi.replace("##", fixed);
    return semi;
  }
}