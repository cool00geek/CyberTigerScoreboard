package cpscorereport;

public class Score implements Comparable {

    private String myTime;
    private final int myTimeSec;
    private int myScore;

    /*
    * @param time: The running time of the image
    * @param score: The current score
    */
    public Score(String time, int score) {
        myTime = time;
        myScore = score;
        myTimeSec = Integer.parseInt(time);
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

    public int getTimeInt() {
        return myTimeSec;
    }

    public int getScore() {
        return myScore;
    }

    @Override
    public int compareTo(Object other) {
        return this.getScore() - ((Score) other).getScore();
    }

    @Override
    public String toString() {
        return "" + myTime + myScore;
    }
}
