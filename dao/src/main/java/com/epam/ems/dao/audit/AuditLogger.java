package com.epam.ems.dao.audit;

import lombok.extern.java.Log;

@Log
public class AuditLogger {

    public void log(String message){
        log.info(message);
    }

}
