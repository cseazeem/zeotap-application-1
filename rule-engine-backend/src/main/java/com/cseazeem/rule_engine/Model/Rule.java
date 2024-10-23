package com.cseazeem.rule_engine.Model;

import java.time.LocalDateTime;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rule {

    private Long id;

    private String name;

    private String ruleString;

    private String astJson;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

