package com.kraken.command.zt.logs;

import com.kraken.command.entity.CommandLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.FluxSink;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

@RunWith(MockitoJUnitRunner.class)
public class CommandsListenerTest {

  @Mock
  FluxSink<CommandLog> sink;

  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(new CommandsListener("app", sink));
  }

}
