package org.gooru.profilebaseline.infra.services.queueoperators;

import org.gooru.profilebaseline.infra.data.ProfileBaselineQueueModel;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class ProfileBaselineDequeuerImpl implements ProfileBaselineDequeuer {

  private final DBI dbi;
  private ProfileBaselineDequeuerDao dao;

  ProfileBaselineDequeuerImpl(DBI dbi) {
    this.dbi = dbi;
  }

  @Override
  public void dequeue(ProfileBaselineQueueModel model) {
    fetchDao().dequeueRecord(model.getId());
  }

  private ProfileBaselineDequeuerDao fetchDao() {
    if (dao == null) {
      dao = dbi.onDemand(ProfileBaselineDequeuerDao.class);
    }
    return dao;
  }
}
