package com.kraken.tools.event.bus;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReactorEventBusTest {

  EventBus eventBus;
  List<TestOneEvent> one;
  List<TestTwoEvent> two;

  class TestOneEventListener extends EventBusListener<TestOneEvent> {
    TestOneEventListener(final EventBus eventBus) {
      super(eventBus, TestOneEvent.class);
    }

    @Override
    protected void handleEvent(TestOneEvent event) {
      one.add(event);
    }
  }

  class TestTwoEventListener extends EventBusListener<TestTwoEvent> {
    TestTwoEventListener(final EventBus eventBus) {
      super(eventBus, TestTwoEvent.class);
    }

    @Override
    protected void handleEvent(TestTwoEvent event) {
      two.add(event);
    }
  }

  static class TestFailEventListener extends EventBusListener<TestFailEvent> {
    TestFailEventListener(final EventBus eventBus) {
      super(eventBus, TestFailEvent.class);
    }

    @Override
    protected void handleEvent(TestFailEvent event) {
      throw new RuntimeException("fail");
    }
  }

  @Before
  public void before() {
    eventBus = new ReactorEventBus();
    one = new ArrayList<>();
    two = new ArrayList<>();
  }

  @Test
  public void shouldPublishEvents() {
    new TestOneEventListener(eventBus);
    new TestTwoEventListener(eventBus);
    new TestFailEventListener(eventBus);

    eventBus.publish(new TestOneEvent());
    eventBus.publish(new TestTwoEvent());
    eventBus.publish(new TestOneEvent());
    eventBus.publish(new TestFailEvent());
    eventBus.publish(new TestTwoEvent());
    eventBus.publish(new TestOneEvent());

    Assertions.assertThat(one.size()).isEqualTo(3);
    Assertions.assertThat(two.size()).isEqualTo(2);
  }

}
