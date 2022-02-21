package com.approvalservice.app;

import com.approvalservice.app.enums.BusinessMessages;
import com.approvalservice.app.model.approval.Approval;
import com.approvalservice.app.model.approval.PendingLoan;
import com.approvalservice.app.util.ObjectToJsonConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * API tests, for loan approval request endpoint. Should run on a working and configured application.
 */

public class LoanAmountApproveTest
{
    private static final String CREATE_REQUEST = "/api/loan/request";
    private static final String APPROVE_REQUEST = "/api/loan/approval";

    public LoanAmountApproveTest()
    {
        RestAssured.baseURI = "http://localhost:8080";
    }

    private static final String CUSTOMER_ID = "21-Q2A1-XX9";
    private static final long LOAN_AMOUNT = 1000;
    private static final ArrayList<String> LOAN_APPROVERS =
            new ArrayList<>(Arrays.asList("Mamertas Juronis", "Gaile Minderyte", "Nina Nugariene"));
    private static final String BODY_READ_PROPERTY = "message";
    private static final Header API_HEADER = new Header("Content-Type", "application/json");

    @BeforeEach
    void createLoanAmountRequest() throws JsonProcessingException
    {
        RequestSpecification reqSpec = given().header(API_HEADER);
        PendingLoan loan = new PendingLoan(CUSTOMER_ID, LOAN_AMOUNT, LOAN_APPROVERS);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(loan);

        reqSpec.body(json);
        reqSpec.post(CREATE_REQUEST);
    }

    @Test
    public void approveLoanRequestOKTest() throws JsonProcessingException
    {
        RequestSpecification reqSpec = given().header(API_HEADER);
        Approval approval = new Approval(CUSTOMER_ID, "Mamertas Juronis", true);

        ObjectToJsonConverter.formRequestJson(reqSpec, approval);

        Response response = reqSpec.post(APPROVE_REQUEST);

        response.then().assertThat().statusCode(200);
        response.then().assertThat().body(BODY_READ_PROPERTY, equalTo(BusinessMessages.SUCCESSFULLY_SENT.getResult()));
    }

    @Test
    public void approveLoanRequestNoQueueTest() throws JsonProcessingException
    {
        RequestSpecification reqSpec = given().header(API_HEADER);
        Approval approval = new Approval(CUSTOMER_ID, "Mamertas Juronis", true);

        ObjectToJsonConverter.formRequestJson(reqSpec, approval);

        reqSpec.post(APPROVE_REQUEST);
        Response response = reqSpec.post(APPROVE_REQUEST);

        response.then().assertThat().statusCode(200);
        response.then().assertThat().body(BODY_READ_PROPERTY, equalTo(BusinessMessages.NO_PENDING_CONTRACTS.getResult()));
    }

    /**
     * In order to not keep anything in queue, accept all loan contracts .
     */
    @AfterAll
    static void approveAllLoans() throws JsonProcessingException
    {
        RequestSpecification reqSpec = given().header(API_HEADER);
        Approval approval = new Approval(CUSTOMER_ID, "Mamertas Juronis", true);

        ObjectToJsonConverter.formRequestJson(reqSpec, approval);
        reqSpec.post(APPROVE_REQUEST);
    }
}
