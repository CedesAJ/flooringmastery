package com.sg.flooringmastery.ui;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class UserIOConsoleImpl implements UserIO{
    private Scanner input = new Scanner(System.in);
    LocalDate dateEntered = null;

    @Override
    public void print(String prompt) {
        System.out.println(prompt);
    }

    @Override
    public String readString(String prompt) {
        System.out.println(prompt);
        return input.nextLine();
    }

    @Override
    public String readString(String prompt, int max) {
        boolean valid = false;
        String result = "";
        do {
            result = readString(prompt);
            if (result.length() <= max) {
                valid = true;
            } else {
                System.out.printf("The entry must be %s letters or less.\n", max);
            }
        } while (!valid);
        return result;
    }

    @Override
    public String formatCurrency(BigDecimal amount) {
        return NumberFormat.getCurrencyInstance().format(amount);
    }

    @Override
    public BigDecimal readBigDecimal(String prompt, int scale) {
        boolean valid = false;
        BigDecimal result = null;
        do {
            String value = null;
            try {
                value = readString(prompt);
                result = new BigDecimal(value).setScale(scale, RoundingMode.HALF_UP);
                valid = true;
            } catch (NumberFormatException ex) {
                System.out.printf("The value '%s' is not a number.\n", value);
            }
        } while (!valid);
        return result;
    }

    @Override
    public BigDecimal readBigDecimal(String prompt, int scale, BigDecimal min) {
        boolean valid = false;
        BigDecimal result = null;
        do {
            result = readBigDecimal(prompt, scale);
            if (result.compareTo(min) >= 0) {
                valid = true;
            } else {
                String minString = String.valueOf(min);
                System.out.printf("The value must be greater than %s.\n", minString);
            }
        } while (!valid);

        return result;
    }

    @Override
    public int readInt(String msgPrompt) {
        boolean invalidInput = true;
        int num = 0;
        while (invalidInput) {
            try {
                String stringValue = this.readString(msgPrompt);
                num = Integer.parseInt(stringValue);
                invalidInput = false;
            } catch (NumberFormatException e) {
                this.print("Input error. Please try again.");
            }
        }
        return num;
    }

    @Override
    public int readInt(String msgPrompt, int min, int max) {
        int result;
        do {
            result = readInt(msgPrompt);
        } while (result < min || result > max);

        return result;
    }

    @Override
    public LocalDate readDate(String prompt) {
        boolean valid = false;
        LocalDate dateEntered = null;
        do {
            String date = null;
            try {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                date = readString(prompt);
                dateEntered = LocalDate.parse(date, dateTimeFormatter);
                valid = true;
            } catch (DateTimeParseException ex) {
                System.out.printf("The value '%s' is not a valid date. (MM-DD-YYYY)\n", date);
            }
        } while (!valid);

        return dateEntered;
    }

    @Override
    public LocalDate readValidDate(String prompt){
        boolean valid = false;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Date:");
        String date = scanner.nextLine();

        do {
            try {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("M-d-u");
                LocalDate today = LocalDate.now(ZoneId.systemDefault());
                LocalDate dateEntered = LocalDate.parse(date, dateTimeFormatter);

                while (dateEntered.isBefore(today)) {
                    System.out.println("Please enter a future date.");
                    date = scanner.nextLine();
                    dateEntered = LocalDate.parse(date, dateTimeFormatter);
                }
                valid = true;
            } catch (DateTimeParseException ex) {
                System.out.println("Please enter a date.");
                date = scanner.nextLine();
            }
        } while (!valid);

        return dateEntered;
    }

    @Override
    public LocalDate readDate(String prompt, LocalDate min, LocalDate max) {
        boolean valid = false;
        LocalDate result = null;
        do {
            result = readDate(prompt);
            if (result.isAfter(min) && result.isBefore(max)) {
                valid = true;
            } else {
                System.out.printf("Please choose a date within bounds. (%s to %s)\n", min, max);
            }
        } while (!valid);

        return result;
    }
}
