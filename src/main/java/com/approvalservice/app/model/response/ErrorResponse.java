package com.approvalservice.app.model.response;

import com.approvalservice.app.enums.ErrorMessages;
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

    public ErrorResponse(ErrorMessages code)
    {
        super(code.getResult());
        this.success = code.isSuccess();
    }
}
