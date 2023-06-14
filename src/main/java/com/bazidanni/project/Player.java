package com.bazidanni.project;

import java.sql.*; 

public class Player {

    String name;
    int level;
    int life;
    int damage;
    int defense;
    String ultimateSkill;
    int energy;
    int ultimateSkillDamage;
    String classType;
    int trophies; 
    int id; 
    int numberOfBattles = 0; 
    String[] battleHistory = new String[10];

    public Player() {
        this.level = 1;
        this.energy = 0;
        this.trophies = 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(int level) {
        this.level = level;
        this.life = (int) (this.life * (1 + (level - 1) * 0.05));
        this.damage = (int) (this.damage * (1 + (level - 1) * 0.05));
        this.defense = (int) (this.defense * (1 + (level - 1) * 0.05));
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setUltimateSkillName(String ultimateSkill) {
        this.ultimateSkill = ultimateSkill;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void setUltimateSkillDamage(int ultimateSkillDamage) {
        this.ultimateSkillDamage = ultimateSkillDamage;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public void setTrophies(int trophies) {
        this.trophies = trophies;
    }

    public String getName() {
        return this.name;
    }

    public int getLevel() {
        return this.level;
    }

    public int getLife() {
        return this.life;
    }

    public int getDamage() {
        return this.damage;
    }

    public int getDefense() {
        return this.defense;
    }

    public String getUltimateSkillName() {
        return this.ultimateSkill;
    }

    public int getEnergy() {
        return this.energy;
    }

    public int getUltimateSkillDamage() {
        return this.ultimateSkillDamage;
    }

    public String getClassType() {
        return this.classType;
    }

    public int getTrophies() {
        return this.trophies;
    }

    public void battle(Player opponent) {
        if (this.classType.equals("Wizard")) {
            this.damage *= 3;
        } else if (this.classType.equals("Defender")) {
            this.life *= 2;
        } else if (this.classType.equals("Warrior")) {
            this.defense *= 2;
        }
        while (this.life > 0 && opponent.life > 0) {
            opponent.life -= this.damage;
            this.energy += 100;
            if (this.energy >= 1000) {
                opponent.life -= this.ultimateSkillDamage;
                this.energy -= 1000;
            }
            if (opponent.life > 0) {
                this.life -= opponent.damage;
                opponent.energy += 100;
                if (opponent.energy >= 1000) {
                    this.life -= opponent.ultimateSkillDamage;
                    opponent.energy -= 1000;
                }
            }
        }
        if (this.life > 0) {
            System.out.println(this.name + " wins!");
            this.trophies += 100;
        } else {
            System.out.println(opponent.name + " wins!");
            opponent.trophies += 100;
        }
        if (numberOfBattles < 10) {
            battleHistory[numberOfBattles] = this.name + " vs " + opponent.name + ": " + this.name + " - " + this.life + ", " + opponent.name + " - " + opponent.life;
            numberOfBattles++;
        } 
        updatePlayer();
        opponent.updatePlayer();
    } 
    
    public void refresh() {
        this.setLevel(this.getLevel());
        this.setEnergy(0);
    } 

    public void setId(int id) {
        this.id = id;
    }

    public void updatePlayer() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://host:port/game", "username", "password")) {
            String updateSql = "UPDATE player SET level = ?, life = ?, damage = ?, defense = ?, main_skill = ?, energy = ?, ultimate_skill_damage = ?, class_type = ?, trophies = ? WHERE id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setInt(1, this.level);
            updateStmt.setInt(2, this.life);
            updateStmt.setInt(3, this.damage);
            updateStmt.setInt(4, this.defense);
            updateStmt.setString(5, this.ultimateSkill);
            updateStmt.setInt(6, this.energy);
            updateStmt.setInt(7, this.ultimateSkillDamage);
            updateStmt.setString(8, this.classType);
            updateStmt.setInt(9, this.trophies);
            updateStmt.setInt(10, this.id);
            updateStmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    } 

    public static Player getPlayer(int id) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://host:port/game", "username", "password")) {
            String selectSql = "SELECT * FROM player WHERE id = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectSql);
            selectStmt.setInt(1, id);
            ResultSet result = selectStmt.executeQuery();
            if (result.next()) {
                Player player = new Player();
                player.setId(result.getInt("id"));
                player.setName(result.getString("name"));
                player.setLevel(result.getInt("level"));
                player.setLife(result.getInt("life"));
                player.setDamage(result.getInt("damage"));
                player.setDefense(result.getInt("defense"));
                player.setUltimateSkillName(result.getString("main_skill"));
                player.setEnergy(result.getInt("energy"));
                player.setUltimateSkillDamage(result.getInt("ultimate_skill_damage"));
                player.setClassType(result.getString("class_type"));
                player.setTrophies(result.getInt("trophies"));
                return player;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void insertPlayer() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://host:port/game", "username", "password")) {
            String insertSql = "INSERT INTO player (name, level, life, damage, defense, main_skill, energy, ultimate_skill_damage, class_type) "
                              + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setString(1, this.getName());
            insertStmt.setInt(2, this.getLevel());
            insertStmt.setInt(3, this.getLife());
            insertStmt.setInt(4, this.getDamage());
            insertStmt.setInt(5, this.getDefense());
            insertStmt.setString(6, this.getUltimateSkillName());
            insertStmt.setInt(7, this.getEnergy());
            insertStmt.setInt(8, this.getUltimateSkillDamage());
            insertStmt.setString(9, this.getClassType());
            insertStmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
