package org.gooru.profilebaseline.infra.services.classsetting;

import org.gooru.profilebaseline.infra.data.ProfileBaselineProcessingContext;
import org.gooru.profilebaseline.infra.services.algebra.competency.CompetencyLine;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class ClassGradeLowBoundFinderImpl implements ClassGradeLowBoundFinder {

  private final DBI dbi4core;
  private final DBI dbi4ds;

  ClassGradeLowBoundFinderImpl(DBI dbi4core, DBI dbi4ds) {
    this.dbi4core = dbi4core;
    this.dbi4ds = dbi4ds;
  }

  @Override
  public CompetencyLine findLowGradeForClass(ProfileBaselineProcessingContext context) {
    // TODO: Implement this

    return null;
  }
}
