import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    //pls run

    public static Squad[] squads = new Squad[32];

    public static void main(String[] args){
        // Read in the data from the Players.csv file
        ArrayList<Player> players = readPlayers();
        
        // Read in the data from the Managers.csv file
        ArrayList<Manager> managers = readManagers();

        // Create a Squad object for each team and fill it with the corresponding Manager and Players
        for (int i = 0; i < 32; i++) {
            Squad squad = new Squad(managers.get(i).getTeam(), managers.get(i));
            for (Player p : players) {
                if (p.getTeam().equals(managers.get(i).getTeam())) {
                    squad.addPlayer(p);
                }
            }
            squads[i] = squad;
        }

        // Run the tournament
        runTournament();
    }

    public static Team getTeam(Squad s){
        Team t = new Team(s.getTeamName(), s.getManager());

        // Create lists for each player position
        ArrayList<Player> defenders = new ArrayList<Player>();
        ArrayList<Player> midfielders = new ArrayList<Player>();
        ArrayList<Player> forwards = new ArrayList<Player>();
        ArrayList<Player> goalKeepers = new ArrayList<Player>();

        // Add players to the appropriate list based on their position
        int index = 0 ;
        boolean loopCondition = true ;
        while(loopCondition){
        	Player p = s.getPlayer(index);
        	if (p.getPosition().equals(" Defender")) {
        		defenders.add(p);
        	} else if (p.getPosition().equals(" Midfielder")) {
        		midfielders.add(p);
        	} else if (p.getPosition().equals(" Forward")) {
        		forwards.add(p);
        	} else if (p.getPosition().equals(" Goal Keeper")) {
        		goalKeepers.add(p);
        	}
            index ++;
            try {
            	s.getPlayer(index);
            }catch(Exception e) {
            	loopCondition = false;
            }
        }
        // Sort each list of players by their combined attribute score
        // Sort each list of players by their combined attribute score, from highest to lowest
        Collections.sort(defenders, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return Double.compare(p2.getCombinedAttributeScore(), p1.getCombinedAttributeScore());
            }
        });
        Collections.sort(midfielders, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return Double.compare(p2.getCombinedAttributeScore(), p1.getCombinedAttributeScore());
            }
        });
        Collections.sort(forwards, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return Double.compare(p2.getCombinedAttributeScore(), p1.getCombinedAttributeScore());
            }
        });
        Collections.sort(goalKeepers, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return Double.compare(p2.getCombinedAttributeScore(), p1.getCombinedAttributeScore());
            }
        });
        
        // Get the preferred formation for the team
        String formation = s.getManager().getFavouredFormation();
        String[] positions = formation.split("-");
        int numDefenders = Integer.parseInt(positions[0]);
        int numMidfielders = Integer.parseInt(positions[1]);
        int numForwards = Integer.parseInt(positions[2]);

        // Add the appropriate number of players to the team based on the preferred formation
        
        for (int i = 0; i < numDefenders; i++) {
            t.addPlayer(defenders.get(i));
        }
        for (int i = 0; i < numMidfielders; i++) {
            t.addPlayer(midfielders.get(i));
        }
        for (int i = 0; i < numForwards ; i++) {
        	t.addPlayer(forwards.get(i));
        }
        
        //Add goalkeeper
        t.addPlayer(goalKeepers.get(0));

        return t;
    }
    
    public static ArrayList<Player> readPlayers(){
        ArrayList<Player> players = new ArrayList<Player>();
        

    	// Read in the data from the Players.csv file
        //Had to use the BufferedReader since the csv file was too big
        try (BufferedReader reader = new BufferedReader(new FileReader("src/Players.csv"))) {
        	// Skip the first line (header row)
        	reader.readLine();

        	// Read the rest of the file
        	String line;
        	while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");  
                String firstName = data[0];
                String surName = data[1];
                String team = data[2];
                String position = data[3];
                double fitness = Double.parseDouble(data[4]);
                double passingAccuracy = Double.parseDouble(data[5]);
                double shotAccuracy = Double.parseDouble(data[6]);
                double shotFrequency = Double.parseDouble(data[7]);
                double defensiveness = Double.parseDouble(data[8]);
                double aggression = Double.parseDouble(data[9]);
                double positioning = Double.parseDouble(data[10]);
                double dribbling = Double.parseDouble(data[11]);
                double chanceCreation = Double.parseDouble(data[12]);
                double offsideAdherence = Double.parseDouble(data[13]);

                // Creates and adds new player
                players.add(new Player(firstName, surName, team, position, fitness, passingAccuracy, shotAccuracy, 
                                          shotFrequency, defensiveness, aggression, positioning, dribbling, 
                                          chanceCreation, offsideAdherence));
            }
        } catch (IOException e) {
			e.printStackTrace();
		}
        return players;
    }
    
    public static ArrayList<Manager> readManagers(){
    	ArrayList<Manager> managers = new ArrayList<Manager>();
    	try (Scanner scanner = new Scanner(new File("src/Managers.csv"))) {
    	        // Skip the header row
    	        scanner.nextLine();

    	        // Read in the rest of the file
    	        while (scanner.hasNextLine()) {
    	            String line = scanner.nextLine();
    	            String[] data = line.split(",");

    	            String firstName = data[0];
    	            String surName = data[1];
    	            String team = data[2];
    	            String favouredFormation = data[3];
    	            double respect = Double.parseDouble(data[4]);
    	            double ability = Double.parseDouble(data[5]);
    	            double knowledge = Double.parseDouble(data[6]);
    	            double belief = Double.parseDouble(data[7]);

    	            // Create a new manager
    	            Manager manager = new Manager(firstName, surName, team, favouredFormation, respect, ability, knowledge, belief);
    	            managers.add(manager);
    	        }
    	   } catch (FileNotFoundException e) {
			e.printStackTrace();
    	   }
    	return managers;
    }
    
    public static float getTeamAttributeScore(Team t) {
    	int index = 0 ;
    	float teamScore = 0f; 
        boolean loopCondition = true ;
        while(loopCondition){
        	Player p = t.getPlayer(index);
        	teamScore += p.getCombinedAttributeScore();
            index ++;
            try {
            	t.getPlayer(index);
            }catch(Exception e) {
            	loopCondition = false;
            }
        }
        // Add the manager's attribute score to the total
        teamScore += t.getManager().getCombinedAttributeScore();
        
        return teamScore;
    }
    
    public static float playMatch(Team team1, Team team2) {
        // Calculate the combined attribute scores for each team
    	System.out.println("Match : "+team1.getTeamName()+" VS "+team2.getTeamName());
        float team1Score = getTeamAttributeScore(team1);
        
        float team2Score = getTeamAttributeScore(team2);

        // Calculate the probability that team1 will win the match
        float probability = team1Score / (team1Score + team2Score);

        // Generate a random number between 0 and 1
        float random = (float) Math.random();

        // Determine the winner of the match
        float winner;
        if (random < probability) {
            winner = 1.0f;
            System.out.println("Winner : "+team1.getTeamName()+" "+probability);
        } else {
            winner = 0.0f;
            System.out.println("Winner : "+team2.getTeamName()+" "+probability);

        }
        
        return winner;
    }

    public static void runTournament(){
    	
    	ArrayList<Team> teams = new ArrayList<Team>();
    	for(Squad s: squads) {
    		teams.add(getTeam(s));
    		System.out.println(teams);
    	}
    	
    	// Create a list to store the groups in the group stage
        ArrayList<Group> groups = new ArrayList<Group>();

        // Divide the teams into 8 groups of 4 teams each
        for (int i = 0; i < 8; i++) {
            groups.add(new Group());
        }
        
        int groupIndex = 0;
        for (Team t : teams) {
            groups.get(groupIndex).addTeam(t);
            groupIndex = (groupIndex + 1) % 8;
        }

        // Print the groups
        for (int i = 0; i < 8; i++) {
            System.out.println("Group " + (i + 1));
            for (Team t : groups.get(i).getTeams()) {
                System.out.println(t.getTeamName());
            }
            System.out.println();
        }
        ArrayList<Team> knockoutStage = new ArrayList<Team>();
     // Run the group stage
        for (Group g : groups) {
        // Play the matches in the group
        	g.initialiseStandings();
	        for (int i = 0; i < g.getTeams().size(); i++) {
	        	for (int j = i+1; j < g.getTeams().size(); j++) {
			        Team team1 = g.getTeams().get(i);
			        Team team2 = g.getTeams().get(j);
			        // Play a match between team1 and team2 and determine the winner
			        float winner = playMatch(team1, team2);
			        // Update the group standings
			        g.updateStandings(winner, team1, team2);
	        	}
        }

            // Determine the top two teams in the group
            ArrayList<Team> topTwoTeams = g.getTopTwoTeams();

            // Add the top two teams to the knockout stage
            knockoutStage.add(topTwoTeams.get(0));
            knockoutStage.add(topTwoTeams.get(1));
            System.out.println("------------------------------------------------");
            System.out.println(topTwoTeams.get(0).getTeamName());
            System.out.println(topTwoTeams.get(1).getTeamName());
            System.out.println("------------------------------------------------");

        }
        System.out.println("-------- KNOCKOUT STAGE --------");
     // Run the knockout stage
        while (knockoutStage.size() > 1) {
            // Play the matches in the knockout stage
            for (int i = 0; i < knockoutStage.size(); i += 2) {
                Team team1 = knockoutStage.get(i);
                Team team2 = knockoutStage.get((i + 1) % knockoutStage.size());
                System.out.println("+++++++++++++ Match "+(knockoutStage.size()-1)+" +++++++++++++");
                float winner = playMatch(team1, team2);

                // Add the winner to the next round
                if (winner == 1) {
                    knockoutStage.remove(team2);
                } else {
                    knockoutStage.remove(team1);
                }

            }
        }

        // Determine the winner of the tournament
        Team winner = knockoutStage.get(0);
        System.out.println("The winner of the tournament is: " + winner.getTeamName());


         //saudi arabia won on mine :/
    }

}