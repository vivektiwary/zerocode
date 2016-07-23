package org.jsmart.zerocode.core.domain.reports.builders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsmart.zerocode.core.di.ObjectMapperProvider;
import org.jsmart.zerocode.core.domain.reports.ZeroCodeReport;
import org.jsmart.zerocode.core.domain.reports.ZeroCodeResult;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.jsmart.zerocode.core.utils.SmartUtils.prettyPrintJson;
import static org.slf4j.LoggerFactory.getLogger;

public class ZeroCodeReportBuilder {
    private static final org.slf4j.Logger LOGGER = getLogger(ZeroCodeReportBuilder.class);

    public static final String TARGET_REPORT_DIR = "target/test-reports/";
    private LocalDateTime timeStamp;
    private List<ZeroCodeResult> results = new ArrayList<ZeroCodeResult>();
    private ZeroCodeReport built;

    public static ZeroCodeReportBuilder newInstance() {
        return new ZeroCodeReportBuilder();
    }

    public ZeroCodeReport build() {
        ZeroCodeReport built = new ZeroCodeReport(timeStamp, results);
        this.built = built;

        return built;
    }

    public ZeroCodeReportBuilder timeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public ZeroCodeReportBuilder results(List<ZeroCodeResult> results) {
        this.results = results;
        return this;
    }

    public ZeroCodeReportBuilder result(ZeroCodeResult result) {
        this.results.add(result);
        return this;
    }

    public void printToFile(String fileName){
        try {
            this.build();

            final ObjectMapper mapper = new ObjectMapperProvider().get();

            File file = new File(TARGET_REPORT_DIR + fileName);
            file.getParentFile().mkdirs();
            mapper.writeValue(file, built);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            LOGGER.warn("### There was a problem during JSON parsing. Details: " + e);

        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.warn("### There was a problem during writing the report. Details: " + e);
        }
    }
}