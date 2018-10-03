package org.gooru.profilebaseline.infra.services;

import org.skife.jdbi.v2.DBI;

/**
 * @author ashish
 */
class ProfileBaselineQueueInitializerServiceImpl implements ProfileBaselineQueueInitializerService {

  private final DBI dbi;

  ProfileBaselineQueueInitializerServiceImpl(DBI dbi) {
    this.dbi = dbi;
  }

  @Override
  public void initializeQueue() {
    ProfileBaselineQueueOperatorDao dao = dbi.onDemand(ProfileBaselineQueueOperatorDao.class);
    dao.initializeQueueStatus();
  }
}
