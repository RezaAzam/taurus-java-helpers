package com.blazemeter.taurus.junit.generator;

import com.blazemeter.taurus.junit.CustomRunner;
import com.blazemeter.taurus.junit.JUnitRunner;
import com.blazemeter.taurus.junit.TaurusReporter;
import com.blazemeter.taurus.junit4.JUnit4Runner;
import com.blazemeter.taurus.junit5.JUnit5Runner;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.blazemeter.taurus.junit.CustomRunner.*;

public class Worker extends Thread {
    private static final Logger log = Logger.getLogger(CustomRunner.class.getName());

    private final Properties props = new Properties();
    private final List<Class> classes = new ArrayList<>();
    private final TaurusReporter reporter;

    private long iterations;
    private long workingTime;
    private long startDelay;

    public Worker(List<Class> classes, Properties properties, TaurusReporter reporter, long startDelay) {
        this.props.putAll(properties);
        this.classes.addAll(classes);
        this.reporter = reporter;

        this.startDelay = startDelay * 1000;
        float rampUp = Float.valueOf(props.getProperty(RAMP_UP, "0"));
        float hold = Float.valueOf(props.getProperty(HOLD, "0"));
        workingTime = (long) (rampUp + hold) * 1000;

        iterations = Long.valueOf(props.getProperty(ITERATIONS, "0"));
        if (iterations == 0) {
            if (hold > 0) {
                iterations = Long.MAX_VALUE;
            } else {
                iterations = 1;
            }
        }

    }

    @Override
    public void run() {
        long endTime = System.currentTimeMillis() + workingTime;
        makeDelay();

        JUnitRunner runner = getJUnitRunner(props.getProperty(JUNIT_VERSION));
        Object request = runner.createRequest(classes, props);

        int iter = 0;
        while (true) {
            iter++;
            runner.executeRequest(request, reporter);
            long currTime = System.currentTimeMillis();
            if (endTime <= currTime) {
                log.info(String.format("[%s] Duration limit reached, stopping", getName()));
                break;
            }

            if (iter >= iterations) {
                log.info(String.format("[%s] Iteration limit reached, stopping", getName()));
                break;
            }
        }
    }

    protected void makeDelay() {
        if (startDelay > 0) {
            try {
                sleep(startDelay);
            } catch (InterruptedException e) {
                log.log(Level.SEVERE, "Worker was interrupted", e);
                throw new RuntimeException("Worker was interrupted", e);
            }
        }
    }

    protected static JUnitRunner getJUnitRunner(String junitVersion) {
        log.fine("Set JUnit version = " + junitVersion);
        if (junitVersion == null || junitVersion.isEmpty() || junitVersion.equals("4")) {
            log.fine("Will use JUnit 4 version");
            return new JUnit4Runner();
        } else if (junitVersion.equals("5")) {
            log.fine("Will use JUnit 5 version");
            return new JUnit5Runner();
        } else {
            log.warning("Cannot detect JUnit version=" + junitVersion + ". Will use JUnit 4 version");
            return new JUnit4Runner();
        }
    }
}
