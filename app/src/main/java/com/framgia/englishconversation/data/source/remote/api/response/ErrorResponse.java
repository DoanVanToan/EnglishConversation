package com.framgia.englishconversation.data.source.remote.api.response;

import com.google.gson.annotations.Expose;

/**
 *
 */

public class ErrorResponse extends BaseRespone {
    @Expose
    private int status;
    @Expose
    private String message;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
