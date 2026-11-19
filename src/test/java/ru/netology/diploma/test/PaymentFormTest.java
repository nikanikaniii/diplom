package ru.netology.diploma.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Link;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.diploma.data.DataHelper;
import ru.netology.diploma.data.SQLHelper;
import ru.netology.diploma.page.MainPage;
import ru.netology.diploma.page.PaymentPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentFormTest {
    private static final String WRONG_FORMAT = "Неверный формат";
    private PaymentPage paymentPage;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("AllureSelenide");
    }

    @BeforeEach
    void setUp() {
        io.github.bonigarcia.wdm.WebDriverManager.chromedriver().clearResolutionCache().setup();

        SQLHelper.cleanDatabase();
        open("http://localhost:8080");
        paymentPage = new MainPage().openPaymentForm();
    }

    @Test
    @DisplayName("TC-1 Покупка Approved-картой")
    void shouldPayWithApprovedCard() {
        paymentPage.fill(DataHelper.getApprovedCard());
        paymentPage.shouldShowSuccess();
        assertEquals("APPROVED", SQLHelper.getLastPaymentStatus());
    }

    @Test
    @DisplayName("TC-2 Покупка Declined-картой")
    @Link(name = "Баг: DECLINED-карта не отклоняется", url = "ISSUE_LINK_1")
    void shouldDeclineWithDeclinedCard() {
        paymentPage.fill(DataHelper.getDeclinedCard());
        paymentPage.shouldShowBankRefusal();
        assertEquals("DECLINED", SQLHelper.getLastPaymentStatus());
    }

    @Test
    @DisplayName("TC-3 Номер карты: пустое поле")
    void shouldRejectEmptyCardNumber() {
        paymentPage.fill(DataHelper.getApprovedCard().withNumber(""));
        paymentPage.shouldShowFieldError("Номер карты", "Неверный формат");
    }

    @Test
    @DisplayName("TC-4 Номер карты: 1 цифра")
    void shouldRejectOneDigitCardNumber() {
        paymentPage.fill(DataHelper.getApprovedCard().withNumber("1"));
        paymentPage.shouldShowFieldError("Номер карты", "Неверный формат");
    }

    @Test
    @DisplayName("TC-5 Номер карты: 15 цифр")
    void shouldRejectFifteenDigitCardNumber() {
        paymentPage.fill(DataHelper.getApprovedCard().withNumber("1111 2222 3333 444"));
        paymentPage.shouldShowFieldError("Номер карты", "Неверный формат");
    }

    @Test
    @DisplayName("TC-6 Месяц: 00")
    @Link(name = "Баг: месяц 00 принимается", url = "ISSUE_LINK_2")
    void shouldRejectZeroMonth() {
        paymentPage.fill(DataHelper.getApprovedCard().withMonth("00"));
        paymentPage.shouldShowFieldError("Месяц", "Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("TC-7 Месяц: 13")
    void shouldRejectThirteenthMonth() {
        paymentPage.fill(DataHelper.getApprovedCard().withMonth("13"));
        paymentPage.shouldShowFieldError("Месяц", "Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("TC-8 Месяц: пустое поле")
    void shouldRejectEmptyMonth() {
        paymentPage.fill(DataHelper.getApprovedCard().withMonth(""));
        paymentPage.shouldShowFieldError("Месяц", "Неверный формат");
    }

    @Test
    @DisplayName("TC-9 Год: прошедший год")
    void shouldRejectPastYear() {
        paymentPage.fill(DataHelper.getApprovedCard().withYear(DataHelper.pastYear()));
        paymentPage.shouldShowFieldError("Год", "Истёк срок действия карты");
    }

    @Test
    @DisplayName("TC-10 Год: пустое поле")
    void shouldRejectEmptyYear() {
        paymentPage.fill(DataHelper.getApprovedCard().withYear(""));
        paymentPage.shouldShowFieldError("Год", "Неверный формат");
    }

    @Test
    @DisplayName("TC-11 Владелец: спецсимволы")
    @Link(name = "Баг: владелец принимает спецсимволы", url = "ISSUE_LINK_3")
    void shouldRejectSpecialSymbolsInHolder() {
        paymentPage.fill(DataHelper.getApprovedCard().withHolder("@@@"));
        paymentPage.shouldShowFieldError("Владелец", "Неверный формат");
    }

    @Test
    @DisplayName("TC-12 Владелец: цифры")
    @Link(name = "Баг: владелец принимает цифры", url = "ISSUE_LINK_4")
    void shouldRejectDigitsInHolder() {
        paymentPage.fill(DataHelper.getApprovedCard().withHolder("12345"));
        paymentPage.shouldShowFieldError("Владелец", "Неверный формат");
    }

    @Test
    @DisplayName("TC-13 Владелец: пустое поле")
    void shouldRejectEmptyHolder() {
        paymentPage.fill(DataHelper.getApprovedCard().withHolder(""));
        paymentPage.shouldShowFieldError("Владелец", "Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("TC-14 CVC: две цифры")
    void shouldRejectShortCvc() {
        paymentPage.fill(DataHelper.getApprovedCard().withCvc("12"));
        paymentPage.shouldShowFieldError("CVC/CVV", "Неверный формат");
    }

    @Test
    @DisplayName("TC-15 CVC: пустое поле")
    void shouldRejectEmptyCvc() {
        paymentPage.fill(DataHelper.getApprovedCard().withCvc(""));
        paymentPage.shouldShowFieldError("CVC/CVV", "Неверный формат");
    }
}