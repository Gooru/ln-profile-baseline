package org.gooru.profilebaseline.infra.services.classsetting;

import org.gooru.profilebaseline.infra.data.ProfileBaselineProcessingContext;
import org.gooru.profilebaseline.infra.jdbi.DBICreator;
import org.gooru.profilebaseline.infra.services.algebra.competency.CompetencyLine;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

public interface ClassGradeLowBoundFinder {

  CompetencyLine findLowGradeForClass(ProfileBaselineProcessingContext context);

  static ClassGradeLowBoundFinder build(DBI dbi4core, DBI dbi4ds) {
    return new ClassGradeLowBoundFinderImpl(dbi4core, dbi4ds);
  }

  static ClassGradeLowBoundFinder build() {
    return new ClassGradeLowBoundFinderImpl(DBICreator.getDbiForDefaultDS(),
        DBICreator.getDbiForDsdbDS());
  }

}
