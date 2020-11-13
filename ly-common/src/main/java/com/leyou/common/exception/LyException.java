package com.leyou.common.exception;


import lombok.Data;

@Data
public class LyException extends RuntimeException {
    private static final long serialVersionUID = -3412628638460853438L;

    private int status;


    public LyException() {
        super();
    }


    public LyException(int status) {
        this.status = status;
    }

    public LyException(int status ,String message) {
        super(message);
        this.status = status;
    }


    public LyException(int status,String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public LyException(Throwable cause) {
        super(cause);
        this.status = status;
    }

    public LyException(int status,Throwable cause) {
        super(cause);
        this.status = status;
    }

}