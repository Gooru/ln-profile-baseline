package org.gooru.profilebaseline.infra.services.r0applicable;

import java.util.UUID;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class Route0ApplicableServiceImpl implements Route0ApplicableService {

  private final DBI dbi;

  Route0ApplicableServiceImpl(DBI dbi) {
    this.dbi = dbi;
  }

  @Override
  public boolean isRoute0ApplicableToClass(UUID classId) {
    // TODO: Implement this
    return false;
  }

  @Override
  public boolean isRoute0ApplicableToCourseInIL(UUID courseId) {
    // TODO: Implement this
    return false;
  }
}
