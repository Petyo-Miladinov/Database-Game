package com.bazidanni.project;

import java.sql.Date;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "audit_log")
@RequiredArgsConstructor
@Data
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "entity_id")
    private int entityId;

    @Column(name = "entity_class")
    private String entityClass;

    @Column(name = "action")
    private String action;

    @Column(name = "action_date")
    private Date actionDate;

}
