package com.kraken.analysis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
class AnalysisConfiguration {

  @Autowired
  @Bean
  AnalysisProperties analysisProperties(@Value("${kraken.analysis.runner.root:#{environment.KRAKEN_ANALYSIS_RUNNER_ROOT}}") final String runnerRoot,
                                        @Value("${kraken.analysis.runner.script:#{environment.KRAKEN_ANALYSIS_RUNNER_SCRIPT}}") final String runnerScript,
                                        @Value("${kraken.analysis.runner.cancel:#{environment.KRAKEN_ANALYSIS_RUNNER_CANCEL}}") final String runnerCancelScript,
                                        @Value("${kraken.analysis.debugger.root:#{environment.KRAKEN_ANALYSIS_DEBUGGER_ROOT}}") final String debuggerRoot,
                                        @Value("${kraken.analysis.debugger.script:#{environment.KRAKEN_ANALYSIS_DEBUGGER_SCRIPT}}") final String debuggerScript,
                                        @Value("${kraken.analysis.debugger.cancel:#{environment.KRAKEN_ANALYSIS_DEBUGGER_CANCEL}}") final String debuggerCancelScript,
                                        @Value("${kraken.analysis.recorder.root:#{environment.KRAKEN_ANALYSIS_RECORDER_ROOT}}") final String recorderRoot,
                                        @Value("${kraken.analysis.recorder.script:#{environment.KRAKEN_ANALYSIS_RECORDER_SCRIPT}}") final String recorderScript,
                                        @Value("${kraken.analysis.url:#{environment.KRAKEN_ANALYSIS_URL}}") final String analysisUrl) {
    log.info("Runner script is set to " + runnerRoot + "/" + runnerScript);
    log.info("Runner cancel script is set to " + runnerRoot + "/" + runnerCancelScript);
    log.info("Debugger script is set to " + debuggerRoot + "/" + debuggerScript);
    log.info("Debugger cancel script is set to " + debuggerRoot + "/" + debuggerCancelScript);
    log.info("Recorder script is set to " + recorderRoot + "/" + recorderScript);
    log.info("Analysis URL is set to " + analysisUrl);

    return AnalysisProperties.builder()
        .runProperties(
            RunProperties.builder()
                .root(runnerRoot)
                .script(runnerScript)
                .cancelScript(runnerCancelScript)
                .build()
        )
        .debugProperties(
            RunProperties.builder()
                .root(debuggerRoot)
                .script(debuggerScript)
                .cancelScript(debuggerCancelScript)
                .build()
        )
        .recordProperties(
            RunProperties.builder()
                .root(recorderRoot)
                .script(recorderScript)
                .cancelScript("")
                .build()
        )
        .analysisUrl(analysisUrl)
        .build();
  }

}
