package org.gooru.profilebaseline.infra.services.learnerprofile;

import org.gooru.profilebaseline.infra.data.ProfileBaselineProcessingContext;
import org.gooru.profilebaseline.infra.services.algebra.competency.CompetencyLine;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class LearnerProfileProviderImpl implements LearnerProfileProvider {

  private final DBI dbi4core;
  private final DBI dbi4ds;

  LearnerProfileProviderImpl(DBI dbi4core, DBI dbi4ds) {
    this.dbi4core = dbi4core;
    this.dbi4ds = dbi4ds;
  }

  @Override
  public CompetencyLine findLearnerProfileForUser(ProfileBaselineProcessingContext context) {
    // TODO: Implement this

    return null;
  }
}
