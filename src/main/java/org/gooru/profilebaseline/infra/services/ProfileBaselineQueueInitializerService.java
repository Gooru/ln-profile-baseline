package org.gooru.profilebaseline.infra.services;

import org.gooru.profilebaseline.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * This service will be used once at the start of application. This service will mark all the record
 * in DB queue which are marked as either dispatched or in process, to queued state. This is to
 * handle cases where some records were being processed while the system shut down, and thus those
 * record need to be reprocessed.
 *
 * @author ashish
 */
public interface ProfileBaselineQueueInitializerService {

  void initializeQueue();

  static ProfileBaselineQueueInitializerService build() {
    return new ProfileBaselineQueueInitializerServiceImpl(DBICreator.getDbiForDefaultDS());
  }

  static ProfileBaselineQueueInitializerService build(DBI dbi) {
    return new ProfileBaselineQueueInitializerServiceImpl(dbi);
  }

}
