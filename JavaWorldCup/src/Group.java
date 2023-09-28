import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Group {
	
	private ArrayList<Team> teams;
	private HashMap<String,Float> standings;
	
	
	public Group() {
	    teams = new ArrayList<Team>();
	    standings = new HashMap<String,Float>();
	}
	
	public void initialiseStandings() {
		for(Team t: teams) {
	    	standings.put(t.getTeamName(), 0.0f);
	    }
	}
	
	public void addTeam(Team team) {
	    teams.add(team);
	}
	
	public ArrayList<Team> getTeams() {
	    return teams;
	}

	public void updateStandings(float winner, Team team1, Team team2) {
	    // Get the current scores of the teams
	    float score1 = standings.get(team1.getTeamName());
	    float score2 = standings.get(team2.getTeamName());

	    // Update the scores based on the result of the match
	    if (winner==1.0) {
	        // Team 1 wins
	        score1 += 3;
	        score2 -= 1;
	    } else if (winner==0.0) {
	        // Team 2 wins
	        score1 -= 1;
	        score2 += 3;
	    } 
	    // Update the scores in the hashmap
	    standings.put(team1.getTeamName(), score1);
	    standings.put(team2.getTeamName(), score2);
	    System.out.println(standings);
	}
	
	public ArrayList<Team> getTopTwoTeams() {
	    // Sort the teams by their scores
		Collections.sort(teams, new Comparator<Team>() {
		    public int compare(Team t1, Team t2) {
		        return Float.compare(standings.get(t2.getTeamName()), standings.get(t1.getTeamName()));
		    }
		});

	    // Return the top two teams
	    ArrayList<Team> topTwoTeams = new ArrayList<>();
	    topTwoTeams.add(teams.get(0));
	    topTwoTeams.add(teams.get(1));
	    return topTwoTeams;
	}


}