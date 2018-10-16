package org.gooru.profilebaseline.processors.postprocessors;

import org.gooru.profilebaseline.infra.data.ProfileBaselineQueueModel;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class RerouteApplicabilityProviderServiceImpl implements
    RerouteApplicabilityProviderService {

  private final DBI dbi;
  private ProfileBaselineQueueModel model;

  RerouteApplicabilityProviderServiceImpl(DBI dbi) {
    this.dbi = dbi;
  }

  @Override
  public RerouteApplicabilityProvider getProvider(ProfileBaselineQueueModel model) {
    this.model = model;
    if (model.getClassId() != null) {
      return routeApplicabilityProviderForClass();
    } else {
      return routeApplicabilityProviderForIL();
    }
  }

  private RerouteApplicabilityProvider routeApplicabilityProviderForIL() {
    // TODO: Provide implementation
    return null;
  }

  private RerouteApplicabilityProvider routeApplicabilityProviderForClass() {
    // TODO: Provide implementation
    return null;
  }
}
