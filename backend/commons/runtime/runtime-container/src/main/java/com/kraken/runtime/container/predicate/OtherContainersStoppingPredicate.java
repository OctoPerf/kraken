package com.kraken.runtime.container.predicate;

import com.kraken.runtime.container.properties.RuntimeContainerProperties;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.Task;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
final class OtherContainersStoppingPredicate implements TaskPredicate {

  @NonNull RuntimeContainerProperties containerProperties;

  @Override
  public boolean test(Task task) {
    return task.getId().equals(containerProperties.getTaskId())
        && task.getContainers()
        .stream()
        .allMatch(container -> container.getHostId().equals(containerProperties.getHostId()) // Same host
            && (container.getName().equals(containerProperties.getContainerName()) // Me
            || container.getStatus().ordinal() > ContainerStatus.RUNNING.ordinal()) // Or finished running
        );
  }

//  Tasks list changed [Task(id=tq1xteqhh8, startDate=1574438209000, status=STARTING, type=RUN, containers=[Container(id=shell-run-tq1xteqhh8-0qshskweou, name=shell-run-tq1xteqhh8-gatling-telegraf, hostId=s5tjfkwumw, label=Telegraf, startDate=1574438209000, status=RUNNING), Container(id=shell-run-tq1xteqhh8-0qshskweou, name=shell-run-tq1xteqhh8-gatling-runner, hostId=s5tjfkwumw, label=Gatling Runner, startDate=1574438209000, status=READY), Container(id=shell-run-tq1xteqhh8-qf7ksdtbhl, name=shell-run-tq1xteqhh8-gatling-telegraf, hostId=fcqgvtcpol, label=Telegraf, startDate=1574438209000, status=STARTING), Container(id=shell-run-tq1xteqhh8-qf7ksdtbhl, name=shell-run-tq1xteqhh8-gatling-runner, hostId=fcqgvtcpol, label=Gatling Runner, startDate=1574438209000, status=STARTING)], expectedCount=4, description=description)]
}
