package com.company;

/**
 * Created by William Guo on 8/4/2024.
 */
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RentalAgreement {
  private Tool tool;
  private int rentalDays;
  private LocalDate checkoutDate;
  private LocalDate dueDate;
  private int chargeDays;
  private BigDecimal preDiscountCharge;
  private int discountPercent;
  private BigDecimal discountAmount;
  private BigDecimal finalCharge;

  public RentalAgreement(Tool tool, int rentalDays, LocalDate checkoutDate, LocalDate dueDate, int chargeDays, BigDecimal preDiscountCharge, int discountPercent, BigDecimal discountAmount, BigDecimal finalCharge) {
    this.tool = tool;
    this.rentalDays = rentalDays;
    this.checkoutDate = checkoutDate;
    this.dueDate = dueDate;
    this.chargeDays = chargeDays;
    this.preDiscountCharge = preDiscountCharge;
    this.discountPercent = discountPercent;
    this.discountAmount = discountAmount;
    this.finalCharge = finalCharge;
  }

  public void printAgreement() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
    NumberFormat numberFormatter = NumberFormat.getCurrencyInstance();

    System.out.println("Tool code: " + tool.getCode());
    System.out.println("Tool type: " + tool.getToolType().getType());
    System.out.println("Tool brand: " + tool.getBrand());

    System.out.println("Rental days: " + rentalDays);
    System.out.println("Check out date: " + checkoutDate.format(formatter));
    System.out.println("Due date: " + dueDate.format(formatter));
    System.out.println("Daily rental charge: " + numberFormatter.format(tool.getToolType().getDailyCharge()));
    System.out.println("Charge days: " + chargeDays);

    System.out.println("Pre-discount charge: " + numberFormatter.format(preDiscountCharge));
    System.out.println("Discount percent: " + discountPercent + "%");
    System.out.println("Discount amount: " + numberFormatter.format(discountAmount));

    System.out.println("Final charge: " + numberFormatter.format(finalCharge));
  }
}
