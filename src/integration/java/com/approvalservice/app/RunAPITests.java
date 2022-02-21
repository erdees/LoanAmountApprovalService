package com.approvalservice.app;

import com.approvalservice.app.enums.BusinessMessages;
import com.approvalservice.app.model.approval.Approval;
import com.approvalservice.app.model.approval.PendingLoan;
import com.approvalservice.app.model.reports.ContractsReport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * API tests, should run on a working and configured application.
 */

public class RunAPITests
{
    private static final String CREATE_REQUEST = "/api/loan/request";
    private static final String APPROVE_REQUEST = "/api/loan/approval";
    private static final String REPORT_ENDPOINT = "/api/loan/report";

    public RunAPITests()
    {
        RestAssured.baseURI = "http://localhost:8080";
    }

    private static final String CUSTOMER_ID = "21-Q2A1-XX9";
    private static final long LOAN_AMOUNT = 1000;
    private static final ArrayList<String> LOAN_APPROVERS =
            new ArrayList<>(Arrays.asList("Mamertas Juronis", "Gaile Minderyte", "Nina Nugariene"));

    /**
     * Check that report is empty
     */
    @Test
    @Order(1)
    public void getSentContractsReportBeforeTest()
    {
        RequestSpecification reqSpec = given().header("Content-Type", "application/json");
        Response response = reqSpec.get(REPORT_ENDPOINT);

        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("message",equalTo(BusinessMessages.NO_CONTRACTS_FOR_STAT.getResult()));
    }

    /**
     * Create new approval request
     */
    @Test
    @Order(2)
    public void createLoanRequestTest() throws JsonProcessingException
    {
        RequestSpecification reqSpec = given().header("Content-Type", "application/json");
        PendingLoan loan = new PendingLoan(CUSTOMER_ID, LOAN_AMOUNT, LOAN_APPROVERS);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(loan);

        reqSpec.body(json);

        Response response = reqSpec.post(CREATE_REQUEST);

        response.then().assertThat().statusCode(201);
        response.then().assertThat().body("message",equalTo(BusinessMessages.CONTRACT_SENT_TO_APPROVAL.getResult()));
    }

    /**
     * Approve (or decline) requested in a previous step request
     */
    @Test
    @Order(3)
    public void approveLoanRequestTest() throws JsonProcessingException
    {
        RequestSpecification reqSpec = given().header("Content-Type", "application/json");
        Approval approval = new Approval(CUSTOMER_ID, "Mamertas Juronis", true);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(approval);

        reqSpec.body(json);

        Response response = reqSpec.post(APPROVE_REQUEST);

        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("message",equalTo(BusinessMessages.SUCCESSFULLY_SENT.getResult()));
    }

    /**
     * Check report after a contract approval
     */
    @Test
    @Order(4)
    public void getSentContractsReportAfterTest() throws JsonProcessingException, InterruptedException
    {
        RequestSpecification reqSpec = given().header("Content-Type", "application/json");
        Response response = reqSpec.get(REPORT_ENDPOINT);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = response.body().print();
        ContractsReport report = mapper.readValue(jsonInString, ContractsReport.class);

        response.then().assertThat().statusCode(200);

        assertThat(report.getContractsCount()).isEqualTo(1);
        assertThat(report.getLoanAmountsSum()).isEqualTo(1000);
        assertThat(report.getAvgLoanAmount()).isEqualTo(1000);
        assertThat(report.getMaxLoanAmount()).isEqualTo(1000);
        assertThat(report.getMinLoanAmount()).isEqualTo(1000);
    }
}
