package com.service.microservice.transaction.enums;

/**
 * @author Simon
 * @Date 2019-08-03 14:35
 * @Describe
 */
public enum  TransactionMessageStatusEnum {
    /**
     *
     */
   WATING(0) ;

    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    TransactionMessageStatusEnum(int status) {
        this.status = status;
    }
}
