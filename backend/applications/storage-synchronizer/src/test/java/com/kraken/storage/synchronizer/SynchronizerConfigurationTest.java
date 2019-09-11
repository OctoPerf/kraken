package com.kraken.storage.synchronizer;

import com.kraken.tools.configuration.properties.ApplicationPropertiesTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {ApplicationPropertiesTestConfiguration.class, SynchronizerConfiguration.class, StringToFileTransfer.class},
    initializers = {ConfigFileApplicationContextInitializer.class})
public class SynchronizerConfigurationTest {

  @Autowired
  SynchronizerProperties synchronizerProperties;

  @Test
  public void shouldInit() {
    assertThat(synchronizerProperties).isNotNull();
    assertThat(synchronizerProperties.getFileDownloads().size()).isEqualTo(2);
    assertThat(synchronizerProperties.getFolderDownloads().size()).isEqualTo(1);
    assertThat(synchronizerProperties.getFileUploads().size()).isEqualTo(1);
  }
}
