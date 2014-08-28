package com.radiatedpixel.www.fancyworkout;

public class Exercise {
    public String name;
    public int[] repetitions;
    public int[] pauses;
    boolean minutes;

    public Exercise(String name, int[] repetitions, int[] pauses, boolean minutes) {
        this.name = name;
        this.repetitions = repetitions;
        this.minutes = minutes;

        if (pauses != null) {
            this.pauses = pauses;
        } else {
            this.pauses = new int[] { 60 };
        }
    }

    public int getSets() {
        return repetitions.length;
    }

    public String getSetsString() {
        int sets = getSets();
        return Integer.toString(sets) + (sets == 1 ? " Set" : " Sets");
    }

    public int getTotalRepetitions() {
        int reps = 0;
        for (int repetition : repetitions) {
            reps += repetition;
        }
        return reps;
    }

    public String getTotalRepetitionsString() {
        int reps = getTotalRepetitions();
        String term = this.minutes ? " Minute" : " Repetition";
        return Integer.toString(reps) + term + (reps == 1 ? "" : "s");
    }
}
