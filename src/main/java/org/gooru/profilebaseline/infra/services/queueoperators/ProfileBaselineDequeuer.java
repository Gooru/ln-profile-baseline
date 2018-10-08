package org.gooru.profilebaseline.infra.services.queueoperators;

import org.gooru.profilebaseline.infra.data.ProfileBaselineQueueModel;
import org.gooru.profilebaseline.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

public interface ProfileBaselineDequeuer {

  void dequeue(ProfileBaselineQueueModel model);

  static ProfileBaselineDequeuer build(DBI dbi) {
    return new ProfileBaselineDequeuerImpl(dbi);
  }

  static ProfileBaselineDequeuer build() {
    return new ProfileBaselineDequeuerImpl(DBICreator.getDbiForDefaultDS());
  }

}
