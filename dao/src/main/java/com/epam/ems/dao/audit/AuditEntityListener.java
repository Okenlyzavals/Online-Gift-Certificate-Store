package com.epam.ems.dao.audit;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class AuditEntityListener {

    private AuditLogger logger = new AuditLogger();

    @PrePersist
    private void onPersist(Object object){
        logger.log("INSERTING: "+object);
    }

    @PreUpdate
    private void onUpdate(Object object){
        logger.log("UPDATING: "+object);
    }

    @PreRemove
    private void onRemove(Object object){
        logger.log("REMOVING: "+object);
    }

}
