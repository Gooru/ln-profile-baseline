package org.gooru.profilebaseline.infra.services.baselineprofilereadcacheupdater;

import org.gooru.profilebaseline.infra.data.ProfileBaselineProcessingContext;
import org.gooru.profilebaseline.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

public interface BaselineProfileReadCacheUpdater {

  void createCachedResponseAndUpdateProfile(ProfileBaselineProcessingContext context,
      Long baselineProfileId);

  static BaselineProfileReadCacheUpdater build(DBI dbi4ds) {
    return new BaselineProfileReadCacheUpdaterImpl(dbi4ds);
  }

  static BaselineProfileReadCacheUpdater build() {
    return new BaselineProfileReadCacheUpdaterImpl(DBICreator.getDbiForDsdbDS());
  }


}
