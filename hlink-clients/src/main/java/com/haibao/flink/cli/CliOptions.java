package com.haibao.flink.cli;

/**
 * Command line options to configure the SQL client.
 * Arguments that have not been specified
 * by the user are null.
 */
public class CliOptions {

    private final String sqlFilePath;
    private final String workingSpace;

    public CliOptions(String sqlFilePath, String workingSpace) {
        this.sqlFilePath = sqlFilePath;
        this.workingSpace = workingSpace;
    }

    public String getSqlFilePath() {
        return sqlFilePath;
    }

    public String getWorkingSpace() {
        return workingSpace;
    }
}
