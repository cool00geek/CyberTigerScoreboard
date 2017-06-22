package cpscorereport;

import java.util.ArrayList;

public class Team implements Comparable {

    private final String myTeamName; // Keep the teamname here since there can only be one
    private ArrayList<Score> myWindows1Scores; // Keep the score of the first windows image
    private String myWin1Name; // Keep the name of the first win image
    private ArrayList<Score> myWindows2Scores; // Keep score of 2nd win image
    private String myWin2Name; // 2nd win image name
    private ArrayList<Score> myWindows3Scores; // 3rd win image scores
    private String myWin3Name; // 3rd win image name
    private ArrayList<Score> myLinux1Scores; // 1st linux image scores
    private String myLin1Name; // 1st Linux image name
    private ArrayList<Score> myLinux2Scores; // 2nd Linux image scores
    private String myLin2Name; // 2nd Linux image name
    private ArrayList<Score> myLinux3Scores; // 3rd linux image scores
    private String myLin3Name; //3rd linux image name
    private int myOSCount; // Total number of OSes for this team

    public Team(ArrayList<Score> scores, String name, String OS) {
        if (OS.toLowerCase().contains("win")) {
            myWindows1Scores = scores; // Create a new win-based team, with the score
            myWin1Name = OS; // and os
        } else {
            myLinux1Scores = scores; // otherwise it's linux
            myLin1Name = OS; // And create teh name
        }
        myTeamName = name; // This is the new teamname
        myOSCount = 1; // Only one OS in the constructor
    }

    public Team(String name) {
        myTeamName = name; // Empty contructor
        myOSCount = 0; // No OSes passed
    }

    public Team(String name, Score starting, String OS) { // Same as first except not an array of scores
        if (OS.toLowerCase().contains("win")) {
            myWindows1Scores = new ArrayList<>();
            myWindows1Scores.add(starting);
            myWin1Name = OS;
        } else {
            myLinux1Scores = new ArrayList<>();
            myLinux1Scores.add(starting);
            myLin1Name = OS;
        }
        myTeamName = name;
        myOSCount = 1;
    }

    public void addScore(Score toAdd, String OS) {
        if (OS.toLowerCase().contains("win")) { // Detect the windows os
            if (myWin1Name != null && OS.equals(myWin1Name)) { // Check if 1st is what we want
                myWindows1Scores.add(toAdd); // Then add the score
            } else if (myWin2Name != null && OS.equals(myWin2Name)) { // Cont the process
                myWindows2Scores.add(toAdd);
            } else if (myWin3Name != null && OS.equals(myWin3Name)) {
                myWindows3Scores.add(toAdd);
            } else if (myWin1Name == null) { // If we didn't find it
                myWindows1Scores = new ArrayList<>(); // Check if the first one is empty
                myWindows1Scores.add(toAdd);
                myWin1Name = OS;
                myOSCount++;
            } else if (myWin2Name == null) { // Check the 2nd 
                myWindows2Scores = new ArrayList<>();
                myWindows2Scores.add(toAdd);
                myWin2Name = OS;
                myOSCount++;
            } else if (myWin3Name == null) { // And 3rd
                myWindows3Scores = new ArrayList<>();
                myWindows3Scores.add(toAdd);
                myWin3Name = OS;
                myOSCount++;
            }
        } else if (OS.toLowerCase().contains("lin")) { // In the case of linux, do the same as windows
            if (myLin1Name != null && OS.equals(myLin1Name)) {
                myLinux1Scores.add(toAdd);
            } else if (myLin2Name != null && OS.equals(myLin2Name)) {
                myLinux2Scores.add(toAdd);
            } else if (myLin3Name != null && OS.equals(myLin3Name)) {
                myLinux3Scores.add(toAdd);
            } else if (myLin1Name == null) {
                myLinux1Scores = new ArrayList<>();
                myLinux1Scores.add(toAdd);
                myLin1Name = OS;
                myOSCount++;
            } else if (myLin2Name == null) {
                myLinux2Scores = new ArrayList<>();
                myLinux2Scores.add(toAdd);
                myLin2Name = OS;
                myOSCount++;
            } else if (myLin3Name == null) {
                myLinux3Scores = new ArrayList<>();
                myLinux3Scores.add(toAdd);
                myLin3Name = OS;
                myOSCount++;
            }
        }
    }

    public ArrayList<Score> getWin1Scores() {
        return myWindows1Scores; // Get the first win OS score
    }

    public ArrayList<Score> getWin2Scores() {
        return myWindows2Scores; // Get the 2nd win OS score
    }

    public ArrayList<Score> getWin3Scores() {
        return myWindows3Scores; // Get the 3rd win OS score
    }

    public ArrayList<Score> getLin1Scores() {
        return myLinux1Scores; // Get the 1st linux OS score
    }

    public ArrayList<Score> getLin2Scores() {
        return myLinux2Scores; // Get the 2nd linux OS score
    }

    public ArrayList<Score> getLin3Scores() {
        return myLinux3Scores; // Get the 3rd linux OS score
    }

    public ArrayList<ArrayList<Score>> getScores() { // GET ALL THE SCORES
        ArrayList<ArrayList<Score>> allScores = new ArrayList<>(this.getTotalOS());
        allScores.add(myWindows1Scores);
        allScores.add(myWindows2Scores);
        allScores.add(myWindows3Scores);
        allScores.add(myLinux1Scores);
        allScores.add(myLinux2Scores);
        allScores.add(myLinux3Scores);
        for (int i = 0; i < allScores.size(); i++) { // Remove the null ones
            if (allScores.get(i) == null) {
                allScores.remove(i);
                i--;
            }
        }
        return allScores; // Send it away
    }

    public ArrayList<String> getOSes() { // Just list all the non-null strings
        ArrayList<String> allScores = new ArrayList<>(this.getTotalOS());
        allScores.add(myWin1Name);
        allScores.add(myWin2Name);
        allScores.add(myWin3Name);
        allScores.add(myLin1Name);
        allScores.add(myLin2Name);
        allScores.add(myLin3Name);
        for (int i = 0; i < allScores.size(); i++) {
            if (allScores.get(i) == null) {
                allScores.remove(i);
                i--;
            }
        }
        return allScores; // Give it
    }

    public String getTeamName() {
        return myTeamName; // Tell the teamname
    }

    public Score getLatestWin1Score() {
        if (myWindows1Scores == null) {
            return null;
        }
        return myWindows1Scores.get(myWindows1Scores.size() - 1); // Give just the very last one for win1
    }

    public Score getLatestWin2Score() {
        if (myWindows2Scores == null) {
            return null;
        }
        return myWindows2Scores.get(myWindows2Scores.size() - 1); // Give just the very last one for win1
    }

    public Score getLatestWin3Score() {
        if (myWindows3Scores == null) {
            return null;
        }
        return myWindows3Scores.get(myWindows3Scores.size() - 1); // Give just the very last one for win3
    }

    public Score getLatestLin1Score() {
        if (myLinux1Scores == null) {
            return null;
        }
        return myLinux1Scores.get(myLinux1Scores.size() - 1); // Give just the very last one for lin1
    }

    public Score getLatestLin2Score() {
        if (myLinux2Scores == null) {
            return null;
        }
        return myLinux2Scores.get(myLinux2Scores.size() - 1); // Give just the very last one for lin2
    }

    public Score getLatestLin3Score() {
        if (myLinux3Scores == null) {
            return null;
        }
        return myLinux3Scores.get(myLinux3Scores.size() - 1); // Give just the very last one for lin3
    }

    public int getTotalOS() {
        return myOSCount; // Give the num of OSes
    }

    @Override
    public String toString() {
        return myTeamName; // Give just the teamname... Potential redundency
    }

    @Override
    public int compareTo(Object other) {
        Score otherTeam = ((Team) other).getLatestWin1Score(); // Compare just the win1 score
        return this.getLatestWin1Score().getScore() - otherTeam.getScore(); // IDK why
    }
}
