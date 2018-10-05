package org.gooru.profilebaseline.infra.services.queuerequest;

import org.gooru.profilebaseline.infra.data.ProfileBaselineContext;
import org.gooru.profilebaseline.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish
 */
public interface RequestQueueService {

  void enqueue(ProfileBaselineContext context);

  static RequestQueueService build() {
    return new RequestQueueServiceImpl(DBICreator.getDbiForDefaultDS());
  }

  static RequestQueueService build(DBI dbi) {
    return new RequestQueueServiceImpl(dbi);
  }

}
