package ru.netology.diploma.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {
    private static final Faker faker = new Faker(new Locale("en"));

    @Value
    public static class CardInfo {
        String number;
        String month;
        String year;
        String holder;
        String cvc;
    }

    public static CardInfo getApprovedCard() {
        return new CardInfo("1111 2222 3333 4444", getCurrentMonth(), getCurrentYear(), faker.name().fullName(), "123");
    }

    public static CardInfo getDeclinedCard() {
        return new CardInfo("5555 6666 7777 8888", getCurrentMonth(), getCurrentYear(), faker.name().fullName(), "123");
    }

    public static CardInfo withNumber(CardInfo card, String number) {
        return new CardInfo(number, card.getMonth(), card.getYear(), card.getHolder(), card.getCvc());
    }

    public static CardInfo withMonth(CardInfo card, String month) {
        return new CardInfo(card.getNumber(), month, card.getYear(), card.getHolder(), card.getCvc());
    }

    public static CardInfo withYear(CardInfo card, String year) {
        return new CardInfo(card.getNumber(), card.getMonth(), year, card.getHolder(), card.getCvc());
    }

    public static CardInfo withHolder(CardInfo card, String holder) {
        return new CardInfo(card.getNumber(), card.getMonth(), card.getYear(), holder, card.getCvc());
    }

    public static CardInfo withCvc(CardInfo card, String cvc) {
        return new CardInfo(card.getNumber(), card.getMonth(), card.getYear(), card.getHolder(), cvc);
    }

    public static String getCurrentMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getCurrentYear() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String pastYear() {
        return LocalDate.now().minusYears(2).format(DateTimeFormatter.ofPattern("yy"));
    }
}