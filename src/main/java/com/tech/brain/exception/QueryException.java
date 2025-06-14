package com.tech.brain.exception;

import lombok.Getter;

import java.io.Serial;
import static com.tech.brain.utils.QueryConstants.HTTP_CODE_500;
import static com.tech.brain.utils.QueryConstants.HYPHEN;

public class QueryException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4711404730678356597L;

    @Getter
    private final String code;
    private final String message;

    public QueryException(Exception exception) {
        super(exception);
        String builder = ErrorCode.HTTP_CODE_500.getErrorCode() + HYPHEN +
                ErrorSeverity.FATAL + HYPHEN + ErrorCode.HTTP_CODE_500.getErrorMessage();
        this.code = HTTP_CODE_500;
        this.message = builder;
    }

    public QueryException(String code, String message) {
        super(message);
        String builder = code + HYPHEN + message;
        this.code = code;
        this.message = builder;
    }

    public QueryException(String code, ErrorSeverity severity, String message) {
        super(message);
        String builder = code + HYPHEN + severity + HYPHEN + message;
        this.code = code;
        this.message = builder;
    }

    public QueryException(String code, ErrorSeverity severity, String message, Exception exception) {
        super(exception);
        String builder = code + HYPHEN + severity + HYPHEN + message;
        this.code = code;
        this.message = builder;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
