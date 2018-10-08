package org.gooru.profilebaseline.infra.services.queueoperators;

import org.gooru.profilebaseline.infra.data.ProfileBaselineQueueModel;
import org.gooru.profilebaseline.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

public interface ProfileBaselineProcessingEligibilityVerifier {

  boolean isEligibleForProcessing(ProfileBaselineQueueModel model);

  static ProfileBaselineProcessingEligibilityVerifier build() {
    return new ProfileBaselineProcessingEligibilityVerifierImpl(DBICreator.getDbiForDefaultDS());
  }

  static ProfileBaselineProcessingEligibilityVerifier build(DBI dbi) {
    return new ProfileBaselineProcessingEligibilityVerifierImpl(dbi);
  }

}