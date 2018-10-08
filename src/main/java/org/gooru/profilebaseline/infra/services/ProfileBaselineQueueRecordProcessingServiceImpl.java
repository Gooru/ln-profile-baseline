package org.gooru.profilebaseline.infra.services;

import org.gooru.profilebaseline.infra.data.ProfileBaselineQueueModel;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class ProfileBaselineQueueRecordProcessingServiceImpl implements
    ProfileBaselineQueueRecordProcessingService {

  private final DBI dbi4core;
  private final DBI dbi4ds;

  ProfileBaselineQueueRecordProcessingServiceImpl(DBI dbi4core, DBI dbi4ds) {
    this.dbi4core = dbi4core;
    this.dbi4ds = dbi4ds;
  }

  @Override
  public void doProfileBaseline(ProfileBaselineQueueModel model) {
    // TODO: Implement this
  }
}
