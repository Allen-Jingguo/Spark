package com.ssc.ssgm.fx.ifx.integration.api;

import lombok.Data;

@Data
public class JDBCExeContext implements ExeContext {

    String exeSql;
    Long period;
    String exeTimer;

}
