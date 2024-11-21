package com.musinsa.demo.config.error.exception;

import com.musinsa.demo.config.error.ErrorCode;

public class DuplicateException extends BusinessException {
    public DuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DuplicateException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
} 