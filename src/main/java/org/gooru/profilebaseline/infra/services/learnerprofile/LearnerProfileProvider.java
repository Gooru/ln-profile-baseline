package org.gooru.profilebaseline.infra.services.learnerprofile;

import org.gooru.profilebaseline.infra.data.ProfileBaselineProcessingContext;
import org.gooru.profilebaseline.infra.jdbi.DBICreator;
import org.gooru.profilebaseline.infra.services.algebra.competency.CompetencyLine;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

public interface LearnerProfileProvider {

  CompetencyLine findLearnerProfileForUser(ProfileBaselineProcessingContext context);

  static LearnerProfileProvider build(DBI dbi4core, DBI dbi4ds) {
    return new LearnerProfileProviderImpl(dbi4core, dbi4ds);
  }

  static LearnerProfileProvider build() {
    return new LearnerProfileProviderImpl(DBICreator.getDbiForDefaultDS(),
        DBICreator.getDbiForDsdbDS());
  }

}
