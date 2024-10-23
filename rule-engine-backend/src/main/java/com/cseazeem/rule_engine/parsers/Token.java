package com.cseazeem.rule_engine.parsers;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Token {

    private TokenType type;
    private String value;

    public enum TokenType {
        OPERATOR,
        COMPARATOR,
        PARENTHESIS,
        ATTRIBUTE,
        VALUE,
        LOGICAL,
    }
}

