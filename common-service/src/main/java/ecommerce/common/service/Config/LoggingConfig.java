package ecommerce.common.service.Config;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import org.slf4j.LoggerFactory;

import java.io.File;

public class LoggingConfig {

    public static void setupFileLogging() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        // Choose the log directory based on the conditions
        String chosenLogDirectory = chooseLogDirectory();

        // Create loggers using the chosen directory
        createRollingFileAppender(loggerContext, "node", chosenLogDirectory + "/auth-service.%d{yyyy-MM-dd}.log");
    }

    private static void createRollingFileAppender(LoggerContext loggerContext, String nodeName, String fileNamePattern) {
        // Ensure directory exists
        ensureDirectoryExists(fileNamePattern);

        // Set up rolling file appender
        RollingFileAppender rollingFileAppender = new RollingFileAppender();
        rollingFileAppender.setContext(loggerContext);
        rollingFileAppender.setName("rollingFileAppender" + nodeName);

        // Set up encoder
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern("%date [%thread] %-5level %logger{36} - %msg%n");
        encoder.start();

        // Set up time-based rolling policy
        TimeBasedRollingPolicy rollingPolicy = new TimeBasedRollingPolicy();
        rollingPolicy.setContext(loggerContext);
        rollingPolicy.setParent(rollingFileAppender);
        rollingPolicy.setFileNamePattern(fileNamePattern);
        rollingPolicy.setMaxHistory(30);
        rollingPolicy.start();

        // Attach encoder and rolling policy to the appender
        rollingFileAppender.setEncoder(encoder);
        rollingFileAppender.setRollingPolicy(rollingPolicy);
        rollingFileAppender.start();

        // Attach appender to logger
        Logger logger = (Logger) LoggerFactory.getLogger("auth.service.audit");
        logger.addAppender(rollingFileAppender);
    }

    private static void ensureDirectoryExists(String fileNamePattern) {
        // Extract the directory path from the fileNamePattern
        File directory = new File(fileNamePattern).getParentFile();
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it doesn't exist
        }
    }

    private static String chooseLogDirectory() {
        File node1Dir = new File("./auth-server/logs/node1");
        File node2Dir = new File("./auth-server/logs/node2");

        // Define your conditions
        boolean node1Condition = node1Dir.exists() || node1Dir.mkdirs();
        boolean node2Condition = node2Dir.exists() || node2Dir.mkdirs();

        // Choose based on OR condition
        if (node1Condition && node1Dir.canWrite()) {
            return node1Dir.getPath();
        } else if (node2Condition && node2Dir.canWrite()) {
            return node2Dir.getPath();
        } else {
            // Fallback to a default directory
            String fallbackDir = "./auth-server/logs/default";
            File fallbackDirFile = new File(fallbackDir);
            if (fallbackDirFile.mkdirs() || fallbackDirFile.exists()) {
                return fallbackDir;
            }
            // Handle the case where none of the directories meet the conditions
            throw new RuntimeException("No suitable log directory found");
        }
    }
}