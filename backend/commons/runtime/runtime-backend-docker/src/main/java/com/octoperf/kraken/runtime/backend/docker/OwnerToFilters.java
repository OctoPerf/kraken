package com.octoperf.kraken.runtime.backend.docker;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.security.entity.owner.Owner;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.octoperf.kraken.runtime.backend.api.EnvironmentLabel.*;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class OwnerToFilters implements Function<Owner, List<String>> {

  private static final String FILTER = "--filter";
  private static final String LABEL_FORMAT = "label=%s=%s";

  @Override
  public List<String> apply(final Owner owner) {
    final var listBuilder = ImmutableList.<String>builder();
    Optional.ofNullable(Strings.emptyToNull(owner.getApplicationId())).ifPresent(appId -> listBuilder.add(FILTER, String.format(LABEL_FORMAT, COM_OCTOPERF_APPLICATION_ID, appId)));
    Optional.ofNullable(Strings.emptyToNull(owner.getProjectId())).ifPresent(uId -> listBuilder.add(FILTER, String.format(LABEL_FORMAT, COM_OCTOPERF_PROJECT_ID, uId)));
    Optional.ofNullable(Strings.emptyToNull(owner.getUserId())).ifPresent(uId -> listBuilder.add(FILTER, String.format(LABEL_FORMAT, COM_OCTOPERF_USER_ID, uId)));
    return listBuilder.build();
  }

}
