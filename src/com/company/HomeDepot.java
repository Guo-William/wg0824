package com.company;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import static java.time.temporal.TemporalAdjusters.firstInMonth;

/**
 * Created by William Guo on 8/4/2024.
 */

public class HomeDepot {
  private Map<String, Tool> tools = new HashMap<>();
  private Map<String, ToolType> toolTypes = new HashMap<>();

  // ideally we'd have all the tool info stored into a database
  public HomeDepot() {
    toolTypes.put("Ladder", new ToolType("Ladder", new BigDecimal("1.99"), true, true, false));
    toolTypes.put("Chainsaw", new ToolType("Chainsaw", new BigDecimal("1.49"), true, false, true));
    toolTypes.put("Jackhammer", new ToolType("Jackhammer", new BigDecimal("2.99"), true, false, false));

    tools.put("CHNS", new Tool("CHNS", toolTypes.get("Chainsaw"), "Stihl"));
    tools.put("LADW", new Tool("LADW", toolTypes.get("Ladder"), "Werner"));
    tools.put("JAKD", new Tool("JAKD", toolTypes.get("Jackhammer"), "DeWalt"));
    tools.put("JAKR", new Tool("JAKR", toolTypes.get("Jackhammer"), "Ridgid"));
  }

  public RentalAgreement checkout(String toolCode, int rentalDays, LocalDate checkoutDate, int discountPercent) throws IllegalArgumentException {
    if (rentalDays < 1) {
      throw new IllegalArgumentException("Invalid number of rental days. Rental day count must be 1 or greater.");
    }
    if (discountPercent < 0 || discountPercent > 100) {
      throw new IllegalArgumentException("Invalid discount percentage. Discount percent must be in the range 0-100.");
    }

    Tool tool = tools.get(toolCode);

    // Prepare all numbers for rental agreement
    ToolType toolType = tool.getToolType();
    BigDecimal dailyCharge = toolType.getDailyCharge();
    LocalDate dueDate = checkoutDate.plusDays(rentalDays);
    int chargeDays = calculateChargeDays(toolType, rentalDays, checkoutDate, dueDate);
    BigDecimal preDiscountCharge = dailyCharge.multiply(new BigDecimal(chargeDays));
    // get discount amount by dividing by 100 and multiplying by discounter int. Finally round the number up.
    BigDecimal discountAmount = preDiscountCharge.multiply(new BigDecimal(discountPercent)).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
    BigDecimal finalCharge = preDiscountCharge.subtract(discountAmount).setScale(2, BigDecimal.ROUND_HALF_UP);

    return new RentalAgreement(tool, rentalDays, checkoutDate, dueDate, chargeDays, preDiscountCharge, discountPercent, discountAmount, finalCharge);
  }

  // loops through the days and calls getIsChargeableAsNumberOfDays
  // adds what is returned to however many chargeable days we have so far
  // makes sure we never go negative on chargeable days
  private int calculateChargeDays(ToolType toolType, int rentalDays, LocalDate checkoutDate, LocalDate dueDate) {
    int chargeDays = 0;
    LocalDate date = checkoutDate.plusDays(1);
    for (int i = 0; i < rentalDays; i++) {
      chargeDays = chargeDays + getIsChargeableAsNumberOfDays(toolType, date, checkoutDate, dueDate);
      date = date.plusDays(1);
    }
    return Math.max(0, chargeDays);
  }

  // checks if saturday or sunday for july 4th and checks with due dates to make sure if we
  // should or shouldnt apply holiday discounts
  // also grabs the first monday in september to for labor day as a holiday
  private int getIsChargeableAsNumberOfDays(ToolType toolType, LocalDate date, LocalDate checkoutDate, LocalDate dueDate) {
    Month month = date.getMonth();
    int day = date.getDayOfMonth();
    DayOfWeek dayOfWeek = date.getDayOfWeek();
    int nonHolidayCount = chargeableDaysNotCountingHoliday(toolType, date);

    if (!toolType.isHolidayCharge()) {
      // July 4th logic
      if (month == Month.JULY && day == 4) {
        if (dayOfWeek == DayOfWeek.SATURDAY && date.minusDays(1).isEqual(checkoutDate)) {
          return nonHolidayCount;
        }
        if (dayOfWeek == DayOfWeek.SUNDAY && date.isEqual(dueDate)) {
          return nonHolidayCount;
        }
        return nonHolidayCount - 1;
      }
      // Labor day logic
      if (month == Month.SEPTEMBER && date.isEqual(date.with(firstInMonth(DayOfWeek.MONDAY)))) {
        return 0;
      }
    }
    return nonHolidayCount;
  }

  // counts chargeable days with no logic for holidays
  private int chargeableDaysNotCountingHoliday(ToolType toolType, LocalDate date) {
    DayOfWeek dayOfWeek = date.getDayOfWeek();
    if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
      if (toolType.isWeekendCharge()) {
        return 1;
      }
    } else {
      if (toolType.isWeekdayCharge()) {
        return 1;
      }
    }
    return 0;
  }
}