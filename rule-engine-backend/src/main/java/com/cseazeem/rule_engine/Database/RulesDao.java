package com.cseazeem.rule_engine.Database;

import com.cseazeem.rule_engine.Model.Rule;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.LocalDateTime;
import java.util.List;

public interface RulesDao {

    @SqlUpdate("INSERT INTO rule (name, rule_string, ast_json, created_at, updated_at) " +
            "VALUES (:name, :ruleString, :astJson, :createdAt, :updatedAt)")
    @RegisterBeanMapper(Rule.class)
    @GetGeneratedKeys
    Rule insertRule(
            @Bind("name") String name,
            @Bind("ruleString") String ruleString,
            @Bind("astJson") String astJson,
            @Bind("createdAt") LocalDateTime createdAt,
            @Bind("updatedAt") LocalDateTime updatedAt
    );

    @SqlQuery("SELECT * FROM rule WHERE id IN (<ruleIds>)")
    @RegisterBeanMapper(Rule.class)
    List<Rule> getRuleByIds(@BindList("ruleIds") List<Long> ruleIds);


    @SqlQuery("SELECT * FROM rule WHERE id = :id")
    @RegisterBeanMapper(Rule.class)
    Rule getRuleById(@Bind("id") Long id);
}

