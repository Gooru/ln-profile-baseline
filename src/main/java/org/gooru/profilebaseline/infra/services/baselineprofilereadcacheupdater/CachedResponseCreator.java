package org.gooru.profilebaseline.infra.services.baselineprofilereadcacheupdater;

import java.util.List;
import org.gooru.profilebaseline.infra.data.ProfileBaselineProcessingContext;
import org.gooru.profilebaseline.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

public interface CachedResponseCreator {

  String createCachedResponse(ProfileBaselineProcessingContext context, List<String> gutCodes);

  static CachedResponseCreator build(DBI dbi4ds) {
    return new CachedResponseCreatorImpl(dbi4ds);
  }

  static CachedResponseCreator build() {
    return new CachedResponseCreatorImpl(DBICreator.getDbiForDsdbDS());
  }


}
