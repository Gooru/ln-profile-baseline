package org.gooru.profilebaseline.infra.services.baselineprofilepersister;

import org.gooru.profilebaseline.infra.data.ProfileBaselineProcessingContext;
import org.gooru.profilebaseline.infra.jdbi.DBICreator;
import org.gooru.profilebaseline.infra.services.algebra.competency.CompetencyLine;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

public interface BaselineProfilePersister {

  Long persist(ProfileBaselineProcessingContext context, CompetencyLine resultLine);

  static BaselineProfilePersister build(DBI dbi4ds) {
    return new BaselineProfilePersisterImpl(dbi4ds);
  }

  static BaselineProfilePersister build() {
    return new BaselineProfilePersisterImpl(DBICreator.getDbiForDefaultDS());
  }

}
