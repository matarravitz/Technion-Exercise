/**
 * a class that represents three trees that represent all the runners in the competition. In each tree the
 * nodes are runners and their key field is different in each tree.
 * in one tree the key is the ID, in another tree it is the average time and in another it is the minimum time.
 * each tree will have a comparator that would compare differently between 2 runners in the tree.
 */
public class Race {

    /**
     * A comparator that compares runners by their ID.
     */
    private static class RunnerByIdComparator implements Comparator<Runner> {
        //the method assumes that the ids of obj1 and obj2 are different
        @Override
        public int compare(Runner obj1, Runner obj2) {
            if (obj1 == MIN_RUNNER) {
                return -1;
            }
            if (obj1 == MAX_RUNNER) {
                return 1;
            }
            if (obj2 == MIN_RUNNER) {
                return 1;
            }
            if (obj2 == MAX_RUNNER) {
                return -1;
            }

            if (obj1.getID().isSmaller(obj2.getID())) {
                return -1;
            }
            if (obj2.getID().isSmaller(obj1.getID())) {
                return 1;
            }
            return 0;
        }
    }


    /**
     * Comparator that compares Runners by their minimum running time
     */
    private static class RunnerByMinimumComparator implements Comparator<Runner> {
        //the method assumes that the ids of obj1 and obj2 are different
        @Override
        public int compare(Runner obj1, Runner obj2) {
            if (obj1 == MIN_RUNNER) {
                return -1;
            }
            if (obj1 == MAX_RUNNER) {
                return 1;
            }
            if (obj2 == MIN_RUNNER) {
                return 1;
            }
            if (obj2 == MAX_RUNNER) {
                return -1;
            }

            if (obj1.getMinimumRunningTime() == obj2.getMinimumRunningTime()) {
                return RUNNER_BY_ID_COMPARATOR.compare(obj1, obj2);
            }
            if (obj1.getMinimumRunningTime() < obj2.getMinimumRunningTime()) {
                return -1;
            }
            else {
                return 1;
            }
        }
    }

    /**
     * Comparator that compares Runners by their average running time
     */
    private static class RunnerByAverageComparator implements Comparator<Runner> {
        //the method assumes that the ids of obj1 and obj2 are different
        @Override
        public int compare(Runner obj1, Runner obj2) {
            if (obj1 == MIN_RUNNER) {
                return -1;
            }
            if (obj1 == MAX_RUNNER) {
                return 1;
            }
            if (obj2 == MIN_RUNNER) {
                return 1;
            }
            if (obj2 == MAX_RUNNER) {
                return -1;
            }

            if (obj1.getAverageRunningTime() == obj2.getAverageRunningTime()) {
                return RUNNER_BY_ID_COMPARATOR.compare(obj1,obj2);
            }
            if (obj1.getAverageRunningTime() < obj2.getAverageRunningTime()) {
                return -1;
            }
            else {
                return 1;
            }
        }
    }

    //This object will be used as sentinel in the trees. If the comparator
    //identifies that one of the objects to compare points to this constant,
    //then it returns that the other object is bigger, without actually comparing
    private static final Runner MIN_RUNNER = new Runner(null);

    //Same as MIN_RUNNER, but if the comparator identifies that either of the runners to compare points to this
    //constant, it returns that the other is smaller (without comparing)
    private static final Runner MAX_RUNNER = new Runner(null);

    private Tree_2_3<Runner> treeRunnersByID;
    private Tree_2_3<Runner> treeRunnersByMinimum;
    private Tree_2_3<Runner> treeRunnersByAverage;
    private Runner runnerWithMinimalMinimumTime;
    private Runner runnerWithMinimalAverageTime;



    //initialize the comparators for each tree
    private static final Comparator<Runner> RUNNER_BY_ID_COMPARATOR = new RunnerByIdComparator();
    private static final Comparator<Runner> RUNNER_BY_MINIMUM_COMPARATOR = new RunnerByMinimumComparator();
    private static final Comparator<Runner> RUNNER_BY_AVERAGE_COMPARATOR = new RunnerByAverageComparator();

    public Race() {
        init();
    }
    public void init()
    {
        //initial every tree with its comparator and sentinels
        treeRunnersByID = new Tree_2_3();
        treeRunnersByID.init(RUNNER_BY_ID_COMPARATOR, MIN_RUNNER, MAX_RUNNER);

        treeRunnersByMinimum = new Tree_2_3();
        treeRunnersByMinimum.init(RUNNER_BY_MINIMUM_COMPARATOR, MIN_RUNNER, MAX_RUNNER);

        treeRunnersByAverage = new Tree_2_3();
        treeRunnersByAverage.init(RUNNER_BY_AVERAGE_COMPARATOR, MIN_RUNNER, MAX_RUNNER);

    }

    public void addRunner(RunnerID id)
    {
        if(id == null) {
            throw new IllegalArgumentException();
        }

        //create a new runner with the given id and insert it into a new node in each tree -
        //minimum tree, average tree and id tree
        Runner newRunner = new Runner(id);


        treeRunnersByID.insert(newRunner);
        treeRunnersByMinimum.insert(newRunner);
        treeRunnersByAverage.insert(newRunner);

        //check if there is a need to update the fields of the fastest runners
        updateFastestRunnersInCaseOfInserting();
    }

    /**
     * check if there is a need to update the fields of runner with minimal run time and runner with minimal average run time
     * in case of inserting new runner to the race
     */
    private void updateFastestRunnersInCaseOfInserting() {
        runnerWithMinimalMinimumTime = treeRunnersByMinimum.minimum();
        runnerWithMinimalAverageTime = treeRunnersByAverage.minimum();
    }

    /**
     * check if  there is a need to update the fields of runner with minimal run time and runner with minimal average run time
     * in case of removing a runner from the race
     */
    private void updateFastestRunnersInCaseOfRemoving() {
        runnerWithMinimalMinimumTime = treeRunnersByMinimum.minimum();
        runnerWithMinimalAverageTime = treeRunnersByAverage.minimum();

        //the tree's minimum method returns the left sentinel's right sibling. if the tree is empty, this sibling
        //is the right sentinel.
        if (runnerWithMinimalAverageTime == MAX_RUNNER) {
            runnerWithMinimalAverageTime = null;
        }
        if (runnerWithMinimalMinimumTime == MAX_RUNNER) {
            runnerWithMinimalMinimumTime = null;
        }
    }


    public void removeRunner(RunnerID id)
    {
        if(id == null) {
            throw new IllegalArgumentException();
        }
        //create a new temp runner with the given id
        Runner runnerToBeRemoved = new Runner(id);
        //find the runner which we need to remove by its id
        runnerToBeRemoved = treeRunnersByID.search(runnerToBeRemoved);

        if (runnerToBeRemoved == null) { //if there is no such runner in the race
            throw new IllegalArgumentException();
        }

        treeRunnersByID.delete(runnerToBeRemoved);
        treeRunnersByMinimum.delete(runnerToBeRemoved);
        treeRunnersByAverage.delete(runnerToBeRemoved);

        updateFastestRunnersInCaseOfRemoving();
    }

    public void addRunToRunner(RunnerID id, float time)
    {
        if(id == null || time <= 0) {
            throw new IllegalArgumentException();
        }
        //find the existing runner which has the given id
        Runner runner = findRunner(id);

        treeRunnersByMinimum.delete(runner);
        treeRunnersByAverage.delete(runner);
        //insert the running time to the runner
        runner.insertingRunningTime(time);

        treeRunnersByMinimum.insert(runner);
        treeRunnersByAverage.insert(runner);
        //update the runner's fields in case he is the fastest comparing to other runners
        updateFastestRunnersInCaseOfInserting();
    }

    public Runner findRunner(RunnerID id) {
        if(id == null) {
            throw new IllegalArgumentException();
        }
        //create a temp runner which only has the given id for comparison
        //between the runners in the tree
        Runner tempRunner = new Runner(id);
        //return the wanted runner with the corresponding id
        Runner runner = treeRunnersByID.search(tempRunner);
        if (runner == null) { //if there is no such runner
            throw new IllegalArgumentException();
        }
        return runner;
    }

    public void removeRunFromRunner(RunnerID id, float time)
    {
        if(id == null) {
            throw new IllegalArgumentException();
        }
        //find the existing runner which has the given id
        Runner runner = findRunner(id);
        if (runner == null) { //if there is no such runner
            throw new IllegalArgumentException();
        }

        if (runner.getRunningTimes().search(time) == null) { //if the runner does not have the given run time
            throw new IllegalArgumentException();
        }

        treeRunnersByMinimum.delete(runner);
        treeRunnersByAverage.delete(runner);

        runner.deletingRunningTime(time);

        //insert the running time to the runner
        treeRunnersByMinimum.insert(runner);
        treeRunnersByAverage.insert(runner);

        //update the runner's fields in case he was the fastest comparing to other runners
        updateFastestRunnersInCaseOfRemoving();
    }

    public RunnerID getFastestRunnerAvg()
    {
        if (runnerWithMinimalAverageTime == null) { //if the tree is empty
            throw new IllegalArgumentException();
        }
        return runnerWithMinimalAverageTime.getID();
    }

    public RunnerID getFastestRunnerMin()
    {
        if (runnerWithMinimalMinimumTime == null) { //if the tree is empty
            throw new IllegalArgumentException();
        }
        return runnerWithMinimalMinimumTime.getID();
    }

    public float getMinRun(RunnerID id)
    {
        //find the runner with the given id in the tree of runners by minimum and return its
        //minimal running time
        if (id == null || findRunner(id) == null) { //if no such runner exist
            throw new IllegalArgumentException();
        }
        float minRun = findRunner(id).getMinimumRunningTime();
        if(minRun == Float.MAX_VALUE) { //then the runner has no runs
            throw new IllegalArgumentException();
        }
        return minRun;
    }
    public float getAvgRun(RunnerID id){
        //find the runner with the given id in the tree of runners by average and return its
        //average running time
        if (id == null || findRunner(id) == null) { //if no such runner exist
            throw new IllegalArgumentException();
        }
        float avgRun = findRunner(id).getAverageRunningTime();
        if(avgRun == Float.MAX_VALUE) {
            throw new IllegalArgumentException();
        }
        return avgRun;
    }

    public int getRankAvg(RunnerID id)
    {
        if (id == null || findRunner(id) == null) { //if no such runner exist
            throw new IllegalArgumentException();
        }
        return treeRunnersByAverage.rank(findRunner(id));
    }

    public int getRankMin(RunnerID id)
    {
        if (id == null || findRunner(id) == null) { //if no such runner exist
            throw new IllegalArgumentException();
        }
        return treeRunnersByMinimum.rank(findRunner(id));
    }
}