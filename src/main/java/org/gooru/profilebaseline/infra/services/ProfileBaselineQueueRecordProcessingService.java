package org.gooru.profilebaseline.infra.services;


import org.gooru.profilebaseline.infra.data.ProfileBaselineQueueModel;
import org.gooru.profilebaseline.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish
 */

public interface ProfileBaselineQueueRecordProcessingService {

  void doProfileBaseline(ProfileBaselineQueueModel model);

  static ProfileBaselineQueueRecordProcessingService build() {
    return new ProfileBaselineQueueRecordProcessingServiceImpl(DBICreator.getDbiForDefaultDS(),
        DBICreator.getDbiForDsdbDS());
  }

  static ProfileBaselineQueueRecordProcessingService build(DBI dbi4core, DBI dbi4ds) {
    return new ProfileBaselineQueueRecordProcessingServiceImpl(dbi4core, dbi4ds);
  }

}
