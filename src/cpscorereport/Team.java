package cpscorereport;


import java.util.ArrayList;

public class Team implements Comparable {

    private ArrayList<Score> myScores;
    private String myTeamName;

    public Team(ArrayList<Score> scores, String name) {
        myScores = scores;
        myTeamName = name;
    }

    public Team(String name) {
        myScores = new ArrayList<>();
        myTeamName = name;
    }

    public Team(String name, Score starting) {
        myScores = new ArrayList<>();
        myScores.add(starting);
        myTeamName = name;
    }

    public void addScore(Score toAdd) {
        myScores.add(toAdd);
    }

    public ArrayList<Score> getScores() {
        return myScores;
    }

    public String getTeamName() {
        return myTeamName;
    }

    public Score getLatestScore() {
        return myScores.get(myScores.size() - 1);
    }

    @Override
    public String toString() {
        return myTeamName + this.getLatestScore();
    }

    @Override
    public int compareTo(Object other) {
        Score otherTeam = ((Team)other).getLatestScore();
        if (otherTeam.getScore() < this.getLatestScore().getScore()) {
            return 1;
        } else if (otherTeam.getScore() == this.getLatestScore().getScore()) {
            return 0;
        } else {
            return -1;
        }
    }
}
