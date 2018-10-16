package org.gooru.profilebaseline.processors.postprocessors;

import org.gooru.profilebaseline.infra.data.ProfileBaselineQueueModel;
import org.gooru.profilebaseline.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

interface RerouteApplicabilityProviderService {

  RerouteApplicabilityProvider getProvider(
      ProfileBaselineQueueModel model);

  static RerouteApplicabilityProviderService build(DBI dbi) {
    return new RerouteApplicabilityProviderServiceImpl(dbi);
  }

  static RerouteApplicabilityProviderService build() {
    return new RerouteApplicabilityProviderServiceImpl(DBICreator.getDbiForDefaultDS());
  }
}
