package com.cseazeem.rule_engine.Service;

import com.cseazeem.rule_engine.Database.RulesDaoImpl;
import com.cseazeem.rule_engine.Model.ASTNode;
import com.cseazeem.rule_engine.Model.Rule;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EvaluationService {

    @Autowired
    private RulesDaoImpl rulesDaoImpl;

    @Autowired
    private ObjectMapper objectMapper;

    public boolean evaluateRule(Long ruleId, Map<String, Object> data) {
        try {
            Rule rule = rulesDaoImpl.getRuleByIds(ruleId);
            if (rule == null) {
                throw new IllegalArgumentException("Rule not found");
            }

            ASTNode astNode = objectMapper.readValue(
                    rule.getAstJson(),
                    ASTNode.class
            );
            return astNode.evaluate(data);
        } catch (Exception e) {
            throw new RuntimeException("Failed to evaluate rule", e);
        }
    }

}

