package com.cseazeem.rule_engine.parsers;

import com.cseazeem.rule_engine.Exceptions.RuleCreationException;
import com.cseazeem.rule_engine.Model.ASTNode;
import com.cseazeem.rule_engine.Model.OperandNode;
import com.cseazeem.rule_engine.Model.OperatorNode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleParser {

    private List<Token> tokens;
    private int currentTokenIndex;

    public ASTNode parse(String ruleString) throws RuleCreationException {
        this.tokens = tokenize(ruleString);
        this.currentTokenIndex = 0;
        return parseExpression();
    }

    private List<Token> tokenize(String ruleString) throws RuleCreationException {
        List<Token> tokens = new ArrayList<>();
        String regex = "\\s*(\\(|\\)|AND|OR|[><=!]=?|\\w+|'[^']*'|\"[^\"]*\")\\s*";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ruleString);

        while (matcher.find()) {
            String tokenValue = matcher.group(1).trim();
            Token token = createToken(tokenValue);
            tokens.add(token);
        }

        return tokens;
    }

    private Token createToken(String tokenValue) throws RuleCreationException {
        if (
                tokenValue.equalsIgnoreCase("AND") || tokenValue.equalsIgnoreCase("OR")
        ) {
            return new Token(Token.TokenType.OPERATOR, tokenValue.toUpperCase());
        } else if (tokenValue.equals("(") || tokenValue.equals(")")) {
            return new Token(Token.TokenType.PARENTHESIS, tokenValue);
        } else if (tokenValue.matches("[><=!]=?")) {
            return new Token(Token.TokenType.COMPARATOR, tokenValue);
        } else if (
                tokenValue.matches("'[^']*'") || tokenValue.matches("\"[^\"]*\"")
        ) {
            return new Token(
                    Token.TokenType.VALUE,
                    tokenValue.substring(1, tokenValue.length() - 1)
            );
        } else if (tokenValue.matches("\\w+")) {
            return new Token(Token.TokenType.ATTRIBUTE, tokenValue);
        } else {
            throw new RuleCreationException("Unknown token: " + tokenValue);
        }
    }

    private ASTNode parseExpression() throws RuleCreationException {
        ASTNode node = parseTerm();

        while (
                currentTokenIndex < tokens.size() &&
                        tokens.get(currentTokenIndex).getType() == Token.TokenType.OPERATOR &&
                        tokens.get(currentTokenIndex).getValue().equals("OR")
        ) {
            Token operatorToken = tokens.get(currentTokenIndex++);
            ASTNode rightNode = parseTerm();
            node = new OperatorNode(operatorToken.getValue(), node, rightNode);
        }

        return node;
    }

    private ASTNode parseTerm() throws RuleCreationException {
        ASTNode node = parseFactor();

        while (
                currentTokenIndex < tokens.size() &&
                        tokens.get(currentTokenIndex).getType() == Token.TokenType.OPERATOR &&
                        tokens.get(currentTokenIndex).getValue().equals("AND")
        ) {
            Token operatorToken = tokens.get(currentTokenIndex++);
            ASTNode rightNode = parseFactor();
            node = new OperatorNode(operatorToken.getValue(), node, rightNode);
        }

        return node;
    }

    private ASTNode parseFactor() throws RuleCreationException {
        Token token = tokens.get(currentTokenIndex);

        if (
                token.getType() == Token.TokenType.PARENTHESIS &&
                        token.getValue().equals("(")
        ) {
            currentTokenIndex++;
            ASTNode node = parseExpression();
            if (
                    currentTokenIndex >= tokens.size() ||
                            !tokens.get(currentTokenIndex).getValue().equals(")")
            ) {
                throw new RuleCreationException("Missing closing parenthesis");
            }
            currentTokenIndex++;
            return node;
        } else if (token.getType() == Token.TokenType.ATTRIBUTE) {
            return parseCondition();
        } else {
            throw new RuleCreationException("Unexpected token: " + token.getValue());
        }
    }

    private ASTNode parseCondition() throws RuleCreationException {
        Token attributeToken = tokens.get(currentTokenIndex++);
        if (currentTokenIndex >= tokens.size()) {
            throw new RuleCreationException("Expected comparator after attribute");
        }

        Token comparatorToken = tokens.get(currentTokenIndex++);
        if (comparatorToken.getType() != Token.TokenType.COMPARATOR) {
            throw new RuleCreationException(
                    "Expected comparator, found: " + comparatorToken.getValue()
            );
        }

        if (currentTokenIndex >= tokens.size()) {
            throw new RuleCreationException("Expected value after comparator");
        }

        Token valueToken = tokens.get(currentTokenIndex++);
        if (
                valueToken.getType() != Token.TokenType.VALUE &&
                        valueToken.getType() != Token.TokenType.ATTRIBUTE
        ) {
            throw new RuleCreationException(
                    "Expected value, found: " + valueToken.getValue()
            );
        }

        String attribute = attributeToken.getValue();
        String comparator = comparatorToken.getValue();
        String value = valueToken.getValue();

        Object valueObject;
        try {
            valueObject = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            valueObject = value;
        }

        return new OperandNode(attribute, comparator, valueObject);
    }
}

