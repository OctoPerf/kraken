//package com.kraken.gatling.runner.command;
//
//import com.google.common.collect.ImmutableList;
//import com.kraken.runtime.command.Command;
//import com.kraken.runtime.entity.TaskType;
//import lombok.AccessLevel;
//import lombok.experimental.FieldDefaults;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Nullable;
//import java.util.List;
//import java.util.Optional;
//import java.util.function.Supplier;
//
//@Component
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//final class CommandSupplier implements Supplier<Command> {
//
//  String harPath;
//  String simulationPackage;
//  String simulationClass;
//
//  @Autowired
//  public CommandSupplier(
//      @Nullable @Value("${kraken.gatling.har-path:#{environment.KRAKEN_GATLING_HAR_PATH}}") final String harPath,
//      @Nullable @Value("${kraken.gatling.simulation-package:#{environment.KRAKEN_GATLING_SIMULATION_PACKAGE}}") final String simulationPackage,
//      @Nullable @Value("${kraken.gatling.simulation-class:#{environment.KRAKEN_GATLING_SIMULATION_CLASS}}") final String simulationClass
//  ) {
//    this.harPath = Optional.ofNullable(harPath).orElse("");
//    this.simulationPackage = Optional.ofNullable(simulationPackage).orElse("");
//    this.simulationClass = Optional.ofNullable(simulationClass).orElse("");
//  }
//
//  @Override
//  public Command get() {
//    return ImmutableList.of(
//        "./gatling.sh",
//        "--headless", "true",
//        "--mode", "Har",
//        "--har-file", harPath,
//        "--package", simulationPackage,
//        "--class-name", simulationClass
//    );
//  }
//}
