package com.approvalservice.app.model.response;

import com.approvalservice.app.enums.BusinessMessages;
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

    public BasicResponse(BusinessMessages messages)
    {
        this.message = messages.getResult();
    }
}
