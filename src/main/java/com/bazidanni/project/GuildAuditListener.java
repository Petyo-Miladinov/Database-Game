package com.bazidanni.project;

import java.sql.Date;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

public class GuildAuditListener {
    @PersistenceContext
    EntityManager entityManager;

    @PostPersist
    void afterInsert(Guild guild) {
        createAuditLog(guild, "INSERT");
    }

    @PostUpdate
    void afterUpdate(Guild guild) {
        createAuditLog(guild, "UPDATE");
    }

    @PostRemove
    void afterRemove(Guild guild) {
        createAuditLog(guild, "DELETE");
    }

    private void createAuditLog(Guild guild, String actionType) {
        GuildAudit guildAudit = new GuildAudit();
        guildAudit.setGuildId(guild.getId());
        guildAudit.setAction(actionType);
        guildAudit.setActionDate(new Date(0));

        entityManager.persist(guildAudit);
    }
}
