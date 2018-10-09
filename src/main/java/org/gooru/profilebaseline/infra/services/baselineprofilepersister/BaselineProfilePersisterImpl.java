package org.gooru.profilebaseline.infra.services.baselineprofilepersister;

import org.gooru.profilebaseline.infra.data.ProfileBaselineProcessingContext;
import org.gooru.profilebaseline.infra.services.algebra.competency.CompetencyLine;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class BaselineProfilePersisterImpl implements BaselineProfilePersister {

  private final DBI dbi4core;

  BaselineProfilePersisterImpl(DBI dbi4core) {
    this.dbi4core = dbi4core;
  }

  @Override
  public Long persist(ProfileBaselineProcessingContext context, CompetencyLine resultLine) {
    // TODO: Implement this

    return null;
  }
}
