package com.developer.unittest.models;

import com.developer.unittest.exceptions.InsufficientMoneyException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class AccountTest {

    private User victoria;
    private Account victoriaAccount;
    private User selena;
    private Account selenaAccount;

    //BeforeEach and AfterEach execute for each method
    @BeforeEach
    void setup(TestInfo testInfo, TestReporter testReporter){
        victoria = new User("Victoria", 22,"Justice","Dawn");
        victoriaAccount = new Account(victoria, new BigDecimal("1000"));

        selena = new User("Selena", 22,"Gomez","Marie");
        selenaAccount = new Account(selena, new BigDecimal("1000"));

        testReporter.publishEntry("Tag: "+testInfo.getTags().toString());
    }

    @AfterEach
    void tearDown() {
        System.out.println("Finish the methods");
    }

    //BeforeAll and AfterAll execute only once
    @BeforeAll
    @Disabled
    static void beforeALl(){
        System.out.println("do something");
    }

    @AfterAll
    @Disabled
    static void afterAll(){
        System.out.println("do something");
    }

    @Test
    @DisplayName("Validating if the user name is equal to Taylor")//a description
    @Disabled//it ignores the test
    void account_name_is_equal_to_taylor(){
        User user = new User("Taylor", 22,"Swift","Alison");
        Account account = new Account(user, new BigDecimal("1000.1324"));
        String accountName = account.getUser().getName();
        assertEquals("Taylor", accountName);
    }

    @Test
    void user_balance_is_equal_to_a_specific_amount_and_greater_than_zero(){
        User user = new User("Sabrina Ann", 22,"Carpenter","Lynn");
        Account account = new Account(user, new BigDecimal("10.13"));
        //Dynamic message if the test fails
        assertNotNull(account.getBalance(), ()-> "The balance cannot be null");
        assertEquals(10.13, account.getBalance().doubleValue());
        assertFalse(account.getBalance().compareTo(BigDecimal.ZERO) < 0);
    }

    @Test
    @DisplayName("Test to compare two accounts")
    void old_account_equal_to_the_new_account(){
        User user = new User("Selena", 22,"Gomez","Marie");
        Account account = new Account(user, new BigDecimal("1000.21"));
        Account oldAccount = new Account(user, new BigDecimal("1000.21"));
        assertEquals(oldAccount, account);
    }

    @Test
    void validate_debit_account(){
        victoriaAccount.debit(new BigDecimal(200));
        assertNotNull(victoriaAccount.getBalance());
        assertEquals(800, victoriaAccount.getBalance().intValue());
        assertEquals("800", victoriaAccount.getBalance().toPlainString());
    }

    @Test
    void validate_credit_account(){
        victoriaAccount.credit(new BigDecimal(200));
        assertNotNull(victoriaAccount.getBalance());
        assertEquals(1200, victoriaAccount.getBalance().intValue());
        assertEquals("1200", victoriaAccount.getBalance().toPlainString());
    }

    @Test
    void validate_insufficient_money_exception(){
        Exception exception = assertThrows(InsufficientMoneyException.class, ()-> {
            victoriaAccount.debit(new BigDecimal(2000));
        });
        String actual = exception.getMessage();
        String expected = "Insufficient Money";
        assertEquals(expected, actual);

    }

    @Test
    void transfer_money_between_account(){
        Bank bank = new Bank();
        bank.transfer(victoriaAccount, selenaAccount, new BigDecimal(500));
        assertEquals(500, victoriaAccount.getBalance().doubleValue());
        assertEquals(1500, selenaAccount.getBalance().doubleValue());
    }

    @Test
    void validate_relation_between_bank_accounts(){
        Bank bank = new Bank();
        bank.addAccount(victoriaAccount);
        bank.addAccount(selenaAccount);
        bank.setName("Bank Status");
        bank.transfer(victoriaAccount, selenaAccount, new BigDecimal(500));

        //it shows all the detail of each test if they fail
        assertAll(
                ()-> assertEquals(500, victoriaAccount.getBalance().doubleValue()),
                ()-> assertEquals(1500, selenaAccount.getBalance().doubleValue()),
                ()-> assertEquals(2, bank.getAccounts().size()),
                ()-> assertEquals("Bank Status", victoriaAccount.getBank().getName()),
                ()-> assertEquals("Victoria",
                        bank.getAccounts().stream()
                                .filter(account -> account.getUser().getName().equals("Victoria"))
                                .findFirst()
                                .get().getUser().getName()),
                ()->assertTrue(bank.getAccounts().stream().anyMatch(account -> account.getUser().getName().equals("Selena")))
                );
    }

    @Tag("so")
    @Nested
    class SystemOperative{
        @Test
        @EnabledOnOs({OS.WINDOWS, OS.LINUX})
        void execute_this_test_in_windows_and_linux(){
            System.out.println("Do something");
        }

        @Test
        @DisabledOnOs({OS.WINDOWS, OS.LINUX})
        void not_execute_this_test_in_windows_and_linux(){
            System.out.println("Do something");
        }

        @Test
        @DisabledOnJre(JRE.JAVA_8)
        void execute_only_jdk8(){
            System.out.println("Do something");
        }
    }

    @Tag("properties")
    @Nested
    @DisplayName("Class to execute some property tests")
    class SystemEnvironment{
        @Test
        void print_system_properties(){
            Properties properties = System.getProperties();
            properties.forEach((k, v)-> System.out.println(k +" : "+ v));
        }

        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = ".*8.*")
        void print_system_property(){}

        @Test
        @EnabledIfSystemProperty(named = "user.name", matches = "armando")
        void print_if_userName(){}

        @Test
        void printEnvironmentVariables(){
            Map<String, String> getEnv = System.getenv();
            getEnv.forEach((k, v)-> System.out.println(k +" : "+ v));
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk1.8.0_202.*")
        void printEnvironmentVariable(){
            Map<String, String> getEnv = System.getenv();
            getEnv.forEach((k, v)-> System.out.println(k +" : "+ v));
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "DEV")
        void test_environment(){}

        @Test
        @DisabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "PROD")
        void prod_environment(){}
    }

    @Test
    @DisplayName("Validating if the user name is equal to Taylor only in dev environment")//a description
    void account_name_is_equal_to_taylor_in_dev(){
        boolean isDev = "DEV".equals(System.getProperty("ENV"));
        assumingThat(isDev, () -> {
            User user = new User("Taylor", 22,"Swift","Alison");
            Account account = new Account(user, new BigDecimal("1000.1324"));
            String accountName = account.getUser().getName();
            assertEquals("Taylor", accountName);
        });//if this test is passed, execute the next code
    }

    @Tag("repeat")
    @RepeatedTest(value = 2, name ="Repetition number {currentRepetition} of {totalRepetitions}")//repeat the test twice
    @DisplayName("Validating if the user name is equal to Taylor")//a description
    void account_name_is_equal_to_taylor_repeat_example(RepetitionInfo info){
        if(info.getCurrentRepetition() == 2){
            System.out.println("Do something in this test");
        }
        User user = new User("Taylor", 22,"Swift","Alison");
        Account account = new Account(user, new BigDecimal("1000.1324"));
        String accountName = account.getUser().getName();
        assertEquals("Taylor", accountName);
    }

    @Nested
    class ParameterizedExamples{
        @ParameterizedTest(name = "number {index} execute with value {0} - {argumentsWithNames}")
        @ValueSource(strings = {"100", "200", "300", "500", "700", "900"})
        void validate_debit_account_using_value_source(String amount){
            victoriaAccount.debit(new BigDecimal(amount));
            assertNotNull(victoriaAccount.getBalance());
            assertTrue(victoriaAccount.getBalance().compareTo(BigDecimal.ZERO) > 0 );
        }

        @ParameterizedTest(name = "number {index} execute with value {0} - {argumentsWithNames}")
        @CsvSource({"1,100", "2,200", "3,300", "4,500", "5,700", "6,900"})
        void validate_debit_account_using_cvs_source(String index, String amount){
            if(Objects.equals(index, "2")){
                System.out.println("Do something!");
            }
            victoriaAccount.debit(new BigDecimal(amount));
            assertNotNull(victoriaAccount.getBalance());
            assertTrue(victoriaAccount.getBalance().compareTo(BigDecimal.ZERO) > 0 );
        }

        @ParameterizedTest(name = "number {index} execute with value {0} - {argumentsWithNames}")
        @CsvFileSource(resources = "/balances.csv")
        void validate_debit_account_using_cvs_file_source(String amount){
            victoriaAccount.debit(new BigDecimal(amount));
            assertNotNull(victoriaAccount.getBalance());
            assertTrue(victoriaAccount.getBalance().compareTo(BigDecimal.ZERO) > 0 );
        }

        @ParameterizedTest(name = "number {index} execute with value {0} - {argumentsWithNames}")
        @CsvFileSource(resources = "/data.csv")
        void validate_debit_account_using_cvs_file_source_2(String balance, String amount, String expectedName, String currentName){
            victoriaAccount.setBalance(new BigDecimal(balance));
            victoriaAccount.debit(new BigDecimal(amount));
            victoria.setName(currentName);
            victoriaAccount.setUser(victoria);

            assertNotNull(victoriaAccount.getBalance());
            assertNotNull(victoriaAccount.getUser().getName());
            assertEquals(expectedName, victoriaAccount.getUser().getName());
            assertTrue(victoriaAccount.getBalance().compareTo(BigDecimal.ZERO) > 0 );
        }

    }
    @ParameterizedTest(name = "number {index} execute with value {0} - {argumentsWithNames}")
    @MethodSource("getListOfBalances")
    void validate_debit_account_using_method_source(String amount){
        victoriaAccount.debit(new BigDecimal(amount));
        assertNotNull(victoriaAccount.getBalance());
        assertTrue(victoriaAccount.getBalance().compareTo(BigDecimal.ZERO) > 0 );
    }

    private static List<String> getListOfBalances(){
        return Arrays.asList("100","200","300","500","700","900");
    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void timeout_service() throws InterruptedException {
        TimeUnit.SECONDS.sleep(7);
    }

    @Test
    void timeout_assert() {
        assertTimeout(Duration.ofSeconds(5), ()->{
            TimeUnit.SECONDS.sleep(2);
        });
    }
}