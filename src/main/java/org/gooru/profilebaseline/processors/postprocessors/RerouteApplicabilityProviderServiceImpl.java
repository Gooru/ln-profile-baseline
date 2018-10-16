package org.gooru.profilebaseline.processors.postprocessors;

import org.gooru.profilebaseline.infra.data.ProfileBaselineQueueModel;
import org.gooru.profilebaseline.infra.services.r0applicable.Route0ApplicableService;
import org.gooru.profilebaseline.infra.services.rescopeapplicable.RescopeApplicableService;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class RerouteApplicabilityProviderServiceImpl implements
    RerouteApplicabilityProviderService {

  private final DBI dbi;
  private ProfileBaselineQueueModel model;
  private boolean rescopeApplicable, route0Applicable;

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
    rescopeApplicable = RescopeApplicableService.build(dbi)
        .isRescopeApplicableToCourseInIL(model.getCourseId());
    route0Applicable = Route0ApplicableService.build(dbi)
        .isRoute0ApplicableToCourseInIL(model.getCourseId());
    return new RerouteApplicabilityProvider(rescopeApplicable, route0Applicable);
  }

  private RerouteApplicabilityProvider routeApplicabilityProviderForClass() {
    rescopeApplicable = RescopeApplicableService.build(dbi)
        .isRescopeApplicableToClass(model.getClassId());
    route0Applicable = Route0ApplicableService.build(dbi)
        .isRoute0ApplicableToClass(model.getClassId());
    return new RerouteApplicabilityProvider(rescopeApplicable, route0Applicable);
  }
}
