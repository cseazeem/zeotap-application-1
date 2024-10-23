package com.cseazeem.rule_engine.Controller;

import java.util.List;
import java.util.Map;
import com.cseazeem.rule_engine.Model.Rule;
import com.cseazeem.rule_engine.Service.EvaluationService;
import com.cseazeem.rule_engine.Service.RulesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rules")
public class RulesController {

    private static final Logger log = LoggerFactory.getLogger(RulesController.class);
    @Autowired
    private RulesService ruleService;

    @Autowired
    private EvaluationService evaluationService;

    @PostMapping("/create_rule")
    public Rule makeRule(@RequestBody Map<String, String> request) {
        String ruleString = request.get("ruleString");
        Rule rule = ruleService.makeRule(ruleString);
        log.info("Making rule: " + rule);
        return  rule;
    }

    @PostMapping("/combine_rules")
    public Rule combineRules(@RequestBody Map<String, Object> request) {
        @SuppressWarnings("unchecked")
        List<String> ruleIdsStr = (List<String>) request.get("ruleIds");
        List<Long> ruleIds = ruleIdsStr.stream().map(Long::parseLong).toList();
        return ruleService.mergeRules(ruleIds);
    }


    @PostMapping("/{id}/evaluate_rule")
    public ResponseEntity<Map<String, Boolean>> evaluateRule(@PathVariable Long id, @RequestBody Map<String, Object> data) {
        boolean result = evaluationService.evaluateRule(id, data);
        return ResponseEntity.ok(Map.of("result", result));
    }
}

