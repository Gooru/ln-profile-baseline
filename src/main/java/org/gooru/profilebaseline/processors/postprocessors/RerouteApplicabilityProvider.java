package org.gooru.profilebaseline.processors.postprocessors;

/**
 * @author ashish.
 */

class RerouteApplicabilityProvider {

  private final boolean rescopeApplicable;
  private final boolean route0Applicable;


  RerouteApplicabilityProvider(boolean rescopeApplicable, boolean route0Applicable) {
    this.rescopeApplicable = rescopeApplicable;
    this.route0Applicable = route0Applicable;
  }

  public boolean isRescopeApplicable() {
    return rescopeApplicable;
  }

  public boolean isRoute0Applicable() {
    return route0Applicable;
  }
}
