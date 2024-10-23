package com.cseazeem.rule_engine.Service;


import com.cseazeem.rule_engine.Database.RulesDaoImpl;
import com.cseazeem.rule_engine.Exceptions.RuleCreationException;
import com.cseazeem.rule_engine.Model.ASTNode;
import com.cseazeem.rule_engine.Model.OperatorNode;
import com.cseazeem.rule_engine.Model.Rule;
import com.cseazeem.rule_engine.parsers.RuleParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RulesService {

    @Autowired
    private RulesDaoImpl rulesDaoImpl;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(RulesService.class);


    public Rule makeRule(String ruleString) {
        if (ruleString == null || ruleString.trim().isEmpty()) {
            throw new IllegalArgumentException("Rule string cannot be null or empty");
        }

        try {
            RuleParser parser = new RuleParser();
            ASTNode ast = parser.parse(ruleString);
            String astJson = objectMapper.writeValueAsString(ast);

            Rule rule = new Rule();
            rule.setName("Rule_" + UUID.randomUUID());
            rule.setRuleString(ruleString);
            rule.setAstJson(astJson);
            rule.setCreatedAt(LocalDateTime.now());
            rule.setUpdatedAt(LocalDateTime.now());

            Rule insertedRule = rulesDaoImpl.insertRule(rule);

            if (insertedRule == null) {
                throw new RuleCreationException("Failed to insert rule into the database");
            }

            return insertedRule;

        } catch (RuleCreationException e) {
            logger.error("Error while parsing the rule string: " + ruleString, e);
            throw e;
        } catch (Exception e) {
            logger.error("Failed to create rule: " + ruleString, e);
            throw new RuleCreationException("Failed to create rule", e);
        }
    }


    public Rule mergeRules(List<Long> ruleIds) {
        try {
            List<Rule> retrievedRules = rulesDaoImpl.getRuleByIds(ruleIds);
            if (retrievedRules.isEmpty()) {
                throw new IllegalArgumentException("No rules found for the given IDs");
            }

            List<ASTNode> nodeList = new ArrayList<>();
            for (Rule rule : retrievedRules) {
                ASTNode node = objectMapper.readValue(rule.getAstJson(), ASTNode.class);
                nodeList.add(node);
            }

            ASTNode unifiedAstNode = mergeAstNodes(nodeList);

            String serializedAst = objectMapper.writeValueAsString(unifiedAstNode);

            Rule mergedRule = new Rule();
            mergedRule.setName("MergedRule_" + UUID.randomUUID());
            mergedRule.setRuleString("Merged Rule Logic");
            mergedRule.setAstJson(serializedAst);
            mergedRule.setCreatedAt(LocalDateTime.now());
            mergedRule.setUpdatedAt(LocalDateTime.now());

            return rulesDaoImpl.insertRule(mergedRule);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while merging rules", e);
        }
    }

    private ASTNode mergeAstNodes(List<ASTNode> nodeList) {
        ASTNode unifiedNode = nodeList.get(0);
        for (int i = 1; i < nodeList.size(); i++) {
            unifiedNode = new OperatorNode("AND", unifiedNode, nodeList.get(i));
        }
        return unifiedNode;
    }

}

