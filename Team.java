import java.util.ArrayList;

public class Team implements Comparable
{
    private ArrayList<Score> myScores;
    private String myTeamName;

    public Team (ArrayList<Score> scores, String name)
    {
        myScores = score;
        myTeamName = name;
    }

    public Team (String name)
    {
        myScores = new ArrayList<Score>();
        myTeamName = name;
    }

    public Team (String name, Score starting)
    {
        myScores = new ArrayList<Score>();
        myScores.add(starting);
        myTeamName = name;
    }

    public void addScore(Score toAdd)
    {
        scores.add(toAdd);
    }

    public ArrayList<Score> getScores()
    {
        return myScores;
    }

    public String getTeamName()
    {
        return myTeamName;
    }

    public Score getLatestScore()
    {
        return myScores.get(myScores.size()-1);
    }

    public String toString()
    {
        return myTeamName + this.getLatestScore();
    }

    public int compareTo(Team other)
    {
        Score otherTeam = other.getLatestScore();
        if (otherTeam.getScore() < this.getScore())
        {
            return 1;
        }
        else if (otherTeam.getScore() == this.getScore())
        {
            return 0;
        }
        else
        {
            return -1;
        }
    }
}
