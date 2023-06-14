package com.bazidanni.project;

import java.sql.Date;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

public class EquipmentListener {
    @PostPersist
    void afterInsert(Equipment equipment) {
        createAuditLog(equipment, "INSERT");
    }

    @PostUpdate
    void afterUpdate(Equipment equipment) {
        createAuditLog(equipment, "UPDATE");
    }

    @PostRemove
    void afterRemove(Equipment equipment) {
        createAuditLog(equipment, "DELETE");
    }

    private void createAuditLog(Equipment equipment, String actionType) {
        AuditLog auditLog = new AuditLog();
        auditLog.setId(equipment.getId());
        auditLog.setAction(actionType);
        auditLog.setActionDate(new Date(0));
        
        // Persist auditLog to the database...
    }
}
