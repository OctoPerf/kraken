package com.kraken.gatling.runner.command;

import com.kraken.gatling.runner.GatlingTestConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {RunToCommandTest.class, GatlingTestConfiguration.class},
    initializers = {ConfigFileApplicationContextInitializer.class})
public class RunToCommandTest {
//  TODO
}
