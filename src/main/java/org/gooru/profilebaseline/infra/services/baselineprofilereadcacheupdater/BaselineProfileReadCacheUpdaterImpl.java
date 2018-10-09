package org.gooru.profilebaseline.infra.services.baselineprofilereadcacheupdater;

import org.gooru.profilebaseline.infra.data.ProfileBaselineProcessingContext;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class BaselineProfileReadCacheUpdaterImpl implements BaselineProfileReadCacheUpdater {

  private final DBI dbi4ds;

  BaselineProfileReadCacheUpdaterImpl(DBI dbi4ds) {
    this.dbi4ds = dbi4ds;
  }

  @Override
  public void createCachedResponseAndUpdateProfile(ProfileBaselineProcessingContext context,
      Long baselineProfileId) {
    // TODO: Implement this

  }
}
