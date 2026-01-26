package com.nttdata.finance_api.dto;

public class UserUploadResponse {

    private int total;
    private int success;
    private int failed;

    public UserUploadResponse(int total, int success, int failed) {
        this.total = total;
        this.success = success;
        this.failed = failed;
    }

    public int getTotal() {
        return total;
    }

    public int getSuccess() {
        return success;
    }

    public int getFailed() {
        return failed;
    }
}