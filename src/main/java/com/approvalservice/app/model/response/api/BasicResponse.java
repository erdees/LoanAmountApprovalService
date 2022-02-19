package com.approvalservice.app.model.response.api;

import com.approvalservice.app.enums.APIBusinessMessages;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Basic response body
 */

@Getter
@Setter
@AllArgsConstructor
public class BasicResponse
{
    private String message;

    public BasicResponse(APIBusinessMessages messages)
    {
        this.message = messages.getResult();
    }
}
