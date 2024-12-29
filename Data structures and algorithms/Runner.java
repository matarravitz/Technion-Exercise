public class Runner {

    /**
     * Comparator that compares two floats which represent a runner's running times
     */
    private static class FloatComparator implements Comparator<Float> {
        @Override
        public int compare(Float obj1, Float obj2) {
            if (obj1.equals(obj2)) {
                return 0;
            }
            if (obj1 < obj2) {
                return -1;
            }
            else {
                return 1;
            }
        }
    }

    private RunnerID id;
    private float fastestTime;
    private float runSum;
    private int numOfRuns;
    private Tree_2_3<Float> runningTimes;


    public Runner(RunnerID id) {
        fastestTime = Float.MAX_VALUE;
        runningTimes = new Tree_2_3();
        runningTimes.init(new FloatComparator(), Float.MIN_VALUE, Float.MAX_VALUE);
        this.id = id;
    }

    public RunnerID getID() {
        return id;
    }

    public Tree_2_3 getRunningTimes() {
        return runningTimes;
    }

    public float getAverageRunningTime() {
        //if the runner doesn't have any runs yet or all his runnings were removed
        if (fastestTime == Float.MAX_VALUE) {
            return Float.MAX_VALUE;
        }
        return runSum/numOfRuns;
    }

    public float getMinimumRunningTime() {
        return fastestTime;
    }

    public void insertingRunningTime(float time) {
        runningTimes.insert(time);
        if (time < fastestTime) {
            fastestTime = time;
        }
        numOfRuns++;
        runSum += time;
    }


    public void deletingRunningTime(float time) {
        runningTimes.delete(time);
        if(time == fastestTime) {
            fastestTime = runningTimes.minimum();
        }
        runSum -= time;
        numOfRuns--;
    }


}
