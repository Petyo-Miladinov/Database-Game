package com.bazidanni.project;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Data;

@Entity 
@Table(name = "Equipment") 
@EntityListeners(EquipmentListener.class)
@Data
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "damage_boost")
    private int damageBoost;

    @Column(name = "defense_boost")
    private int defenseBoost;

    @Column(name = "level_required")
    private int levelRequired;

}
