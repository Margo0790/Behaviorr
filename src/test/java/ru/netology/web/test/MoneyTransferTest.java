package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.*;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;


class MoneyTransferTest {
    DashboardPage dashboardPage;

    @BeforeEach
    void setup() {
        var loginPage = open("http://localhost:7777", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsV1() {
        var fistCardInfo = getFirstCard();
        var secondCardInfo = getSecondCard();
        var firstCardBalance = dashboardPage.getCardBalance(DataHelper.getFirstCard().getCardId());
        var secondCardBalance = dashboardPage.getCardBalance(DataHelper.getSecondCard().getCardId());
        var amount = validAmount(firstCardBalance);
        var expectedFirstCardBalance = firstCardBalance - amount;
        var expectedSecondCardBalance = secondCardBalance + amount;
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), fistCardInfo);
        var actualFirstCardBalance = dashboardPage.getCardBalance(DataHelper.getFirstCard().getCardId());
        var actualSecondCardBalance = dashboardPage.getCardBalance(DataHelper.getSecondCard().getCardId());

        assertEquals(expectedFirstCardBalance, actualFirstCardBalance);
        assertEquals(expectedSecondCardBalance, actualSecondCardBalance);
    }
}
