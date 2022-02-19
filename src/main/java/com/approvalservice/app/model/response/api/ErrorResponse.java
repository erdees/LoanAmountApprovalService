package com.approvalservice.app.model.response.api;

import com.approvalservice.app.enums.APIResponseCodes;
import lombok.Getter;
import lombok.Setter;

/**
 * Error response body
 */

@Getter
@Setter
public class ErrorResponse extends BasicResponse
{
    private boolean success;

    public ErrorResponse(APIResponseCodes code)
    {
        super(code.getResult());
        this.success = code.isPositive();
    }
}
