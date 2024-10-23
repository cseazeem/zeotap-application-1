package com.cseazeem.rule_engine.Database;

import com.cseazeem.rule_engine.Model.Rule;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RulesDaoImpl {

    private final RulesDao rulesDao;

    public RulesDaoImpl(RulesDao rulesDao) {
        this.rulesDao = rulesDao;
    }

    public Rule insertRule(Rule rule) {
        return rulesDao.insertRule(
                rule.getName(),
                rule.getRuleString(),
                rule.getAstJson(),
                rule.getCreatedAt(),
                rule.getUpdatedAt()
        );
    }

    public List<Rule> getRuleByIds(List<Long> id) {
        return rulesDao.getRuleByIds(id);
    }

    public Rule getRuleByIds(Long id) {
        return rulesDao.getRuleById(id);
    }
}

