package com.bazidanni.project;

import java.sql.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;

@Entity
@Table(name = "Guild_audit")
@Data
public class GuildAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "guild_id")
    private int guildId;

    @Column(name = "action")
    private String action;

    @Column(name = "action_date")
    private Date actionDate;
}
