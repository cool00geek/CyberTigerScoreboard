package cpscorereport;

public class Score implements Comparable {

    private final String myTime;
    private final int myTimeSec;
    private final int myScore;

    /*
    * @param time: The running time of the image
    * @param score: The current score
    */
    public Score(String time, int score) { // Just create the new score
        myTime = time;
        myScore = score;
        myTimeSec = Integer.parseInt(time);
    }

    public String getTime() {
        return myTime; // Return the running time
    }

    public int getTimeInt() {
        return myTimeSec; // Convert the time to seconds and return it
    }

    public int getScore() {
        return myScore; // Give the score
    }

    @Override
    public int compareTo(Object other) {
        return this.getScore() - ((Score) other).getScore(); // Compare a couple scores
    }

    @Override
    public String toString() {
        return "" + myTime + myScore; // Tell the time and score
    }
}
