package com.cseazeem.rule_engine.Exceptions;

public class RuleCreationException extends RuntimeException {

    public RuleCreationException(String message) {
        super(message);
    }

    public RuleCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
