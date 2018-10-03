package org.gooru.profilebaseline.infra.services;

import org.gooru.profilebaseline.infra.data.ProfileBaselineQueueModel;
import org.gooru.profilebaseline.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * This service is responsible to read the record from the queue and return to caller. Caller needs
 * to decides as to what they want to do with the record. This means updating the status of record
 * to indicate that they are being processed. However, fetching the record using this service will
 * mark the record for being dispatched.
 *
 * @author ashish
 */
public interface ProfileBaselineQueueRecordDispatcherService {

  ProfileBaselineQueueModel getNextRecordToDispatch();

  static ProfileBaselineQueueRecordDispatcherService build() {
    return new ProfileBaselineQueueRecordDispatcherServiceImpl(DBICreator.getDbiForDefaultDS());
  }

  static ProfileBaselineQueueRecordDispatcherService build(DBI dbi) {
    return new ProfileBaselineQueueRecordDispatcherServiceImpl(dbi);
  }

}
