package com.bazidanni.project;

import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;

public class Main {
    static Player[] players = new Player[100];

    //static int numberOfPlayers = 0;
    //static int numberOfBattles = 0;
    //static String[] battleHistory = new String[10];
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try(Connection conn = DriverManager.getConnection("jdbc:mysql://host:port/game", "username", "password")) {
            String player = "CREATE TABLE player (\n" + 
            "id INT NOT NULL AUTO_INCREMENT, \n" +
            "name VARCHAR(255) NOT NULL UNIQUE, \n"  + 
            "level INT NOT NULL DEFAULT 1 \n," +
            "life INT NOT NULL \n," + 
            "damage INT NOT NULL \n," + 
            "defense INT NOT NULL \n," +
            "main_skill VARCHAR(255) NOT NULL\n, " + 
            "energy INT NOT NULL DEFAULT 0 \n," + 
            "ultimate_skill_damage INT NOT NULL \n," + 
            "class_type VARCHAR(255) NOT NULL \n," + 
            "trophies INT NOT NULL DEFAULT 0 \n," + 
            "PRIMARY KEY (id) \n" + 
            ");"; 
            PreparedStatement playerSt =conn.prepareStatement(player); 
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try(Connection conn = DriverManager.getConnection("jdbc:mysql://host:port/game", "username", "password")) {
            String battle_history = "CREATE TABLE battle history (\n" + 
            "id INT NOT NULL AUTO_INCREMENT, \n" +
            "player1_id INT NOT NULL, \n"  + 
            "player2_id INT NOT NULL," +
            "battle_result VARCHAR(255) NOT NULL," + 
            "PRIMARY KEY (id)," + 
            "FOREIGN KEY (player1_id) REFERENCES player(id)," +
            "FOREIGN KEY (player2_id) REFERENCES player(id)" + 
            ");"; 
            PreparedStatement battle_historySt =conn.prepareStatement(battle_history); 
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        while (true) {
            if (getNumberOfPlayers() < 2) {
                System.out.println("1. Create a player");
                System.out.println("2. Exit");
                int choice = sc.nextInt();
                sc.nextLine();
                if (choice == 1) {
                    Player player = new Player();
                    System.out.println("Enter player name: ");
                    player.setName(sc.nextLine());
                    System.out.println("Enter player class (Wizard, Defender, Warrior, Classless): ");
                    player.setClassType(sc.nextLine());
                    System.out.println("Enter player main skill: ");
                    player.setUltimateSkillName(sc.nextLine());
                    System.out.println("Enter player life: ");
                    player.setLife(sc.nextInt());
                    sc.nextLine();
                    System.out.println("Enter player damage: ");
                    player.setDamage(sc.nextInt());
                    sc.nextLine();
                    System.out.println("Enter player defense: ");
                    player.setDefense(sc.nextInt());
                    sc.nextLine();
                    System.out.println("Enter player ultimate skill damage: ");
                    player.setUltimateSkillDamage(sc.nextInt());
                    sc.nextLine();
                    player.insertPlayer(); 
                } else if (choice == 2) {
                    break;
                }
            } else {
                System.out.println("1. Begin battle");
                System.out.println("2. Create a player");
                System.out.println("3. History of battles");
                System.out.println("4. Trophies");
                System.out.println("5. Display leaderboard");
                System.out.println("6. Display player record");
                System.out.println("7. Exit");
                int choice = sc.nextInt();
                sc.nextLine();
                if (choice == 1) {
                    for (int i = 0; i < players.length; i++) {
                        if (i + 1 < players.length) {
                            players[i].battle(players[i + 1]);
                            players[i].refresh();
                        } else {
                            players[i].battle(players[0]);
                            players[i].refresh();
                        }
                    }
                } else if (choice == 2) {
                    Player player = new Player();
                    System.out.println("Enter player name: ");
                    player.setName(sc.nextLine());
                    System.out.println("Enter player class (Wizard, Defender, Warrior, Classless): ");
                    player.setClassType(sc.nextLine());
                    System.out.println("Enter player main skill: ");
                    player.setUltimateSkillName(sc.nextLine());
                    System.out.println("Enter player life: ");
                    player.setLife(sc.nextInt());
                    sc.nextLine();
                    System.out.println("Enter player damage: ");
                    player.setDamage(sc.nextInt());
                    sc.nextLine();
                    System.out.println("Enter player defense: ");
                    player.setDefense(sc.nextInt());
                    sc.nextLine();
                    System.out.println("Enter player ultimate skill damage: ");
                    player.setUltimateSkillDamage(sc.nextInt());
                    sc.nextLine();
                    // players[numberOfPlayers] = player;
                    // numberOfPlayers++;
                    player.insertPlayer(); 
                } else if (choice == 3) {
                    String[] battleHistory = getBattleHistory();
                    for (int i = 0; i < battleHistory.length; i++) {
                        System.out.println(battleHistory[i]);
                    }
                } else if (choice == 4) {
                    Player[] players = getPlayers();
                    for (int i = 0; i < players.length; i++) {
                        System.out.println(players[i].getName() + ": " + players[i].getTrophies());
                    } 
                } else if (choice == 5) {
                    displayLeaderboard(); 
                } 
                else if (choice == 6) {
                    int playerId = sc.nextInt();  
                    displayPlayerRecord(playerId);
                } else if (choice == 6) {
                    break;
                }
            }
        }
    }

    public static int getNumberOfPlayers() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://host:port/game", "username", "password")) {
            String selectSql = "SELECT COUNT(*) FROM player";
            Statement selectStmt = conn.createStatement();
            ResultSet result = selectStmt.executeQuery(selectSql);
            if (result.next()) {
                return result.getInt(1);
            }
         } catch (SQLException ex) {
            ex.printStackTrace();
            }
        return 0;
    }

    public static Player[] getPlayers() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://host:port/game", "username", "password")) {
            String selectSql = "SELECT * FROM player";
            Statement selectStmt = conn.createStatement();
            ResultSet result = selectStmt.executeQuery(selectSql);
            ArrayList<Player> players = new ArrayList<>();
            while (result.next()) {
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
                players.add(player);
            }
            return players.toArray(new Player[players.size()]);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new Player[0];
    }
    
    public static String[] getBattleHistory() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://host:port/game", "username", "password")) {
            String selectSql = "SELECT * FROM battle_history";
            Statement selectStmt = conn.createStatement();
            ResultSet result = selectStmt.executeQuery(selectSql);
            ArrayList<String> battleHistory = new ArrayList<>();
            while (result.next()) {
                battleHistory.add(result.getString("battle_result"));
            }
            return battleHistory.toArray(new String[battleHistory.size()]);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new String[0];
    }

    public static void displayLeaderboard() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://host:port/game", "username", "password")) {
            String selectSql = "SELECT player.name, SUM(player.trophies) AS total_trophies "
                              + "FROM player "
                              + "JOIN battle_history "
                              + "ON player.id = battle_history.winner_id "
                              + "GROUP BY player.id "
                              + "ORDER BY total_trophies DESC";
            Statement selectStmt = conn.createStatement();
            ResultSet result = selectStmt.executeQuery(selectSql);
            System.out.println("Player name" + "\t" + "Total Trophies");
            while (result.next()) {
                System.out.println(result.getString("name") + "\t" + result.getInt("total_trophies"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void displayPlayerRecord(int playerId) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://host:port/game", "username", "password")) {
            String selectSql = "SELECT player.name,COUNT(CASE WHEN player.id = battle_history.winner_id THEN 1 END) AS wins, COUNT(CASE WHEN player.id != battle_history.winner_id THEN 1 END) AS losses "
            + "FROM player "
            + "JOIN battle_history "
            + "ON player.id = battle_history.player1_id OR player.id = battle_history.player2_id "
            + "WHERE player.id = ? "
            + "GROUP BY player.id";
            PreparedStatement selectStmt = conn.prepareStatement(selectSql);
            selectStmt.setInt(1, playerId);
            ResultSet result = selectStmt.executeQuery();
            if (result.next()) {
                System.out.println("Player name: " + result.getString("name"));
                System.out.println("Wins: " + result.getInt("wins"));
                System.out.println("Losses: " + result.getInt("losses"));
            } else {
                System.out.println("Player not found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
