package src;

import com.company.HomeDepot;
import com.company.RentalAgreement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HomeDepotTest {
  private HomeDepot homeDepot;
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

  @org.junit.jupiter.api.BeforeEach
  void setUp() {
    homeDepot = new HomeDepot();
    System.setOut(new PrintStream(outContent));
  }

  @Test
  void generalCheckOutTest() {
    LocalDate checkoutDate = LocalDate.of(2024, 8, 4);
    RentalAgreement agreement = homeDepot.checkout("LADW", 5, checkoutDate, 10);
    agreement.printAgreement();
    String printedRentalAgreement = outContent.toString();
    Assertions.assertTrue(printedRentalAgreement.contains("Tool code: LADW"));
    Assertions.assertTrue(printedRentalAgreement.contains("Tool type: Ladder"));
    Assertions.assertTrue(printedRentalAgreement.contains("Tool brand: Werner"));
    Assertions.assertTrue(printedRentalAgreement.contains("Rental days: 5"));
    Assertions.assertTrue(printedRentalAgreement.contains("Check out date: 08/04/24"));
    Assertions.assertTrue(printedRentalAgreement.contains("Due date: 08/09/24"));
    Assertions.assertTrue(printedRentalAgreement.contains("Daily rental charge: $1.99"));
    Assertions.assertTrue(printedRentalAgreement.contains("Charge days: 5"));
    Assertions.assertTrue(printedRentalAgreement.contains("Pre-discount charge: $9.95"));
    Assertions.assertTrue(printedRentalAgreement.contains("Discount percent: 10%"));
    Assertions.assertTrue(printedRentalAgreement.contains("Discount amount: $1.00"));
    Assertions.assertTrue(printedRentalAgreement.contains("Final charge: $8.95"));
  }

  @Test
  void badOver100DiscountCode() {
    LocalDate checkoutDate = LocalDate.of(2015, 9, 3);
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> homeDepot.checkout("JAKR", 5, checkoutDate, 101));
    assertEquals("Invalid discount percentage. Discount percent must be in the range 0-100.", exception.getMessage());
  }
  @Test
  void badUnder0DiscountCode() {
    LocalDate checkoutDate = LocalDate.of(2015, 9, 3);
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> homeDepot.checkout("JAKR", 5, checkoutDate, -1));
    assertEquals("Invalid discount percentage. Discount percent must be in the range 0-100.", exception.getMessage());
  }

  @Test
  void badRentalDays() {
    LocalDate checkoutDate = LocalDate.of(2015, 9, 3);
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> homeDepot.checkout("JAKR", 0, checkoutDate, 15));
    assertEquals("Invalid number of rental days. Rental day count must be 1 or greater.", exception.getMessage());
  }

  @Test
  void july4OnSaturdayLadderYesWeekendCharge() {
    LocalDate checkoutDate = LocalDate.of(2020, 7, 2);
    RentalAgreement agreement = homeDepot.checkout("LADW", 3, checkoutDate, 10);
    agreement.printAgreement();
    String printedRentalAgreement = outContent.toString();
    assertEquals("Tool code: LADW\r\n" +
            "Tool type: Ladder\r\n" +
            "Tool brand: Werner\r\n" +
            "Rental days: 3\r\n" +
            "Check out date: 07/02/20\r\n" +
            "Due date: 07/05/20\r\n" +
            "Daily rental charge: $1.99\r\n" +
            "Charge days: 2\r\n" +
            "Pre-discount charge: $3.98\r\n" +
            "Discount percent: 10%\r\n" +
            "Discount amount: $0.40\r\n" +
            "Final charge: $3.58\r\n", printedRentalAgreement
    );
  }

  @Test
  void july4OnSaturdayChainsawNoWeekendYesHolidayCharge() {
    LocalDate checkoutDate = LocalDate.of(2015, 7, 2);
    RentalAgreement agreement = homeDepot.checkout("CHNS", 5, checkoutDate, 25);
    agreement.printAgreement();
    String printedRentalAgreement = outContent.toString();
    assertEquals("Tool code: CHNS\r\n" +
            "Tool type: Chainsaw\r\n" +
            "Tool brand: Stihl\r\n" +
            "Rental days: 5\r\n" +
            "Check out date: 07/02/15\r\n" +
            "Due date: 07/07/15\r\n" +
            "Daily rental charge: $1.49\r\n" +
            "Charge days: 3\r\n" +
            "Pre-discount charge: $4.47\r\n" +
            "Discount percent: 25%\r\n" +
            "Discount amount: $1.12\r\n" +
            "Final charge: $3.35\r\n", printedRentalAgreement
    );
  }

  @Test
  void laborDayJackhammerNoHolidayNoWeekendCharge() {
    LocalDate checkoutDate = LocalDate.of(2015, 9, 3);
    RentalAgreement agreement = homeDepot.checkout("JAKD", 6, checkoutDate, 0);
    agreement.printAgreement();
    String printedRentalAgreement = outContent.toString();
    assertEquals("Tool code: JAKD\r\n" +
            "Tool type: Jackhammer\r\n" +
            "Tool brand: DeWalt\r\n" +
            "Rental days: 6\r\n" +
            "Check out date: 09/03/15\r\n" +
            "Due date: 09/09/15\r\n" +
            "Daily rental charge: $2.99\r\n" +
            "Charge days: 3\r\n" +
            "Pre-discount charge: $8.97\r\n" +
            "Discount percent: 0%\r\n" +
            "Discount amount: $0.00\r\n" +
            "Final charge: $8.97\r\n", printedRentalAgreement
    );
  }

  @Test
  void july4OnSaturdayJackhammerNoWeekendNoHolidayChargeLongerThanAWeek() {
    LocalDate checkoutDate = LocalDate.of(2015, 7, 2);
    RentalAgreement agreement = homeDepot.checkout("JAKR", 9, checkoutDate, 0);
    agreement.printAgreement();
    String printedRentalAgreement = outContent.toString();
    assertEquals("Tool code: JAKR\r\n" +
            "Tool type: Jackhammer\r\n" +
            "Tool brand: Ridgid\r\n" +
            "Rental days: 9\r\n" +
            "Check out date: 07/02/15\r\n" +
            "Due date: 07/11/15\r\n" +
            "Daily rental charge: $2.99\r\n" +
            "Charge days: 5\r\n" +
            "Pre-discount charge: $14.95\r\n" +
            "Discount percent: 0%\r\n" +
            "Discount amount: $0.00\r\n" +
            "Final charge: $14.95\r\n", printedRentalAgreement
    );
  }

  @Test
  void july4OnSaturdayJackhammerNoWeekendNoHolidayCharge() {
    LocalDate checkoutDate = LocalDate.of(2015, 7, 2);
    RentalAgreement agreement = homeDepot.checkout("JAKR", 4, checkoutDate, 50);
    agreement.printAgreement();
    String printedRentalAgreement = outContent.toString();
    assertEquals("Tool code: JAKR\r\n" +
            "Tool type: Jackhammer\r\n" +
            "Tool brand: Ridgid\r\n" +
            "Rental days: 4\r\n" +
            "Check out date: 07/02/15\r\n" +
            "Due date: 07/06/15\r\n" +
            "Daily rental charge: $2.99\r\n" +
            "Charge days: 1\r\n" +
            "Pre-discount charge: $2.99\r\n" +
            "Discount percent: 50%\r\n" +
            "Discount amount: $1.50\r\n" +
            "Final charge: $1.49\r\n", printedRentalAgreement
    );
  }

  @Test
  void july4OnSundayJackhammerNoWeekendNoHolidayCharge() {
    LocalDate checkoutDate = LocalDate.of(2021, 7, 2);
    RentalAgreement agreement = homeDepot.checkout("JAKR", 4, checkoutDate, 50);
    agreement.printAgreement();
    String printedRentalAgreement = outContent.toString();
    assertEquals("Tool code: JAKR\r\n" +
            "Tool type: Jackhammer\r\n" +
            "Tool brand: Ridgid\r\n" +
            "Rental days: 4\r\n" +
            "Check out date: 07/02/21\r\n" +
            "Due date: 07/06/21\r\n" +
            "Daily rental charge: $2.99\r\n" +
            "Charge days: 1\r\n" +
            "Pre-discount charge: $2.99\r\n" +
            "Discount percent: 50%\r\n" +
            "Discount amount: $1.50\r\n" +
            "Final charge: $1.49\r\n", printedRentalAgreement
    );
  }

  @Test
  void july4OnSundayJackhammerNoWeekendNoHolidayChargeAvoidNegativeCharge() {
    LocalDate checkoutDate = LocalDate.of(2021, 7, 2);
    RentalAgreement agreement = homeDepot.checkout("JAKR", 3, checkoutDate, 50);
    agreement.printAgreement();
    String printedRentalAgreement = outContent.toString();
    assertEquals("Tool code: JAKR\r\n" +
            "Tool type: Jackhammer\r\n" +
            "Tool brand: Ridgid\r\n" +
            "Rental days: 3\r\n" +
            "Check out date: 07/02/21\r\n" +
            "Due date: 07/05/21\r\n" +
            "Daily rental charge: $2.99\r\n" +
            "Charge days: 0\r\n" +
            "Pre-discount charge: $0.00\r\n" +
            "Discount percent: 50%\r\n" +
            "Discount amount: $0.00\r\n" +
            "Final charge: $0.00\r\n", printedRentalAgreement
    );
  }

  @Test
  void singleDayRental() {
    LocalDate checkoutDate = LocalDate.of(2024, 8, 1);
    RentalAgreement agreement = homeDepot.checkout("JAKR", 1, checkoutDate, 50);
    agreement.printAgreement();
    String printedRentalAgreement = outContent.toString();
    assertEquals("Tool code: JAKR\r\n" +
            "Tool type: Jackhammer\r\n" +
            "Tool brand: Ridgid\r\n" +
            "Rental days: 1\r\n" +
            "Check out date: 08/01/24\r\n" +
            "Due date: 08/02/24\r\n" +
            "Daily rental charge: $2.99\r\n" +
            "Charge days: 1\r\n" +
            "Pre-discount charge: $2.99\r\n" +
            "Discount percent: 50%\r\n" +
            "Discount amount: $1.50\r\n" +
            "Final charge: $1.49\r\n", printedRentalAgreement
    );
  }

  @Test
  void hundredPercentDiscount() {
    LocalDate checkoutDate = LocalDate.of(2024, 8, 1);
    RentalAgreement agreement = homeDepot.checkout("JAKR", 1, checkoutDate, 100);
    agreement.printAgreement();
    String printedRentalAgreement = outContent.toString();
    assertEquals("Tool code: JAKR\r\n" +
            "Tool type: Jackhammer\r\n" +
            "Tool brand: Ridgid\r\n" +
            "Rental days: 1\r\n" +
            "Check out date: 08/01/24\r\n" +
            "Due date: 08/02/24\r\n" +
            "Daily rental charge: $2.99\r\n" +
            "Charge days: 1\r\n" +
            "Pre-discount charge: $2.99\r\n" +
            "Discount percent: 100%\r\n" +
            "Discount amount: $2.99\r\n" +
            "Final charge: $0.00\r\n", printedRentalAgreement
    );
  }

  @Test
  void spansBothHolidays() {
    LocalDate checkoutDate = LocalDate.of(2024, 7, 1);
    RentalAgreement agreement = homeDepot.checkout("JAKR", 90, checkoutDate, 0);
    agreement.printAgreement();
    String printedRentalAgreement = outContent.toString();
    assertEquals("Tool code: JAKR\r\n" +
            "Tool type: Jackhammer\r\n" +
            "Tool brand: Ridgid\r\n" +
            "Rental days: 90\r\n" +
            "Check out date: 07/01/24\r\n" +
            "Due date: 09/29/24\r\n" +
            "Daily rental charge: $2.99\r\n" +
            "Charge days: 62\r\n" +
            "Pre-discount charge: $185.38\r\n" +
            "Discount percent: 0%\r\n" +
            "Discount amount: $0.00\r\n" +
            "Final charge: $185.38\r\n", printedRentalAgreement
    );
  }
}