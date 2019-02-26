package com.taihehospital.triagecallcenter.config.db;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author ydd
 * @create 2018/8/11
 */
@PropertySource("classpath:SqlStatement.properties")
@Component
@Getter
@Setter
public class SqlStatementConfig {
    @Value("${mzVisitTable}")
    private String mzVisitTable;
}
