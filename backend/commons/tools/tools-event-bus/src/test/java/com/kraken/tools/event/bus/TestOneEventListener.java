package com.kraken.tools.event.bus;

final class TestOneEventListener extends EventBusListener<TestOneEvent> {

  TestOneEventListener(final EventBus eventBus){
    super(eventBus, TestOneEvent.class);
  }

  @Override
  protected void handleEvent(TestOneEvent event) {

  }
}
