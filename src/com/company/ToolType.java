package com.company;

/**
 * Created by William Guo on 8/4/2024.
 */
import java.math.BigDecimal;

public class ToolType {
  private String type;
  private BigDecimal dailyCharge;
  private boolean weekdayCharge;
  private boolean weekendCharge;
  private boolean holidayCharge;

  public ToolType(String type, BigDecimal dailyCharge, boolean weekdayCharge, boolean weekendCharge, boolean holidayCharge) {
    this.type = type;
    this.dailyCharge = dailyCharge;
    this.weekdayCharge = weekdayCharge;
    this.weekendCharge = weekendCharge;
    this.holidayCharge = holidayCharge;
  }

  public String getType() {
    return type;
  }

  public BigDecimal getDailyCharge() {
    return dailyCharge;
  }

  public boolean isWeekdayCharge() {
    return weekdayCharge;
  }

  public boolean isWeekendCharge() {
    return weekendCharge;
  }

  public boolean isHolidayCharge() {
    return holidayCharge;
  }
}
