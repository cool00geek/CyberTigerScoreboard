package cpscorereport;


public class Score implements Comparable {

    private String myTime;
    private int myScore;

    public Score(String time, int score) {
        myTime = time;
        myScore = score;
    }

    public void setTime(String time) {
        myTime = time;
    }

    public void setScore(int score) {
        myScore = score;
    }

    public String getTime() {
        return myTime;
    }

    public int getScore() {
        return myScore;
    }

    @Override
    public int compareTo(Object other) {
        return this.getScore() - ((Score)other).getScore();
    }

    @Override
    public String toString() {
        return "" + myTime + myScore;
    }
}
