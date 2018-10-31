package org.gooru.profilebaseline.infra.services.queueoperators;

import org.gooru.profilebaseline.infra.data.ProfileBaselineQueueModel;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */

class ProfileBaselineProcessingEligibilityVerifierImpl implements
    ProfileBaselineProcessingEligibilityVerifier {

  private final DBI dbi4core;
  private final DBI dbi4ds;
  private static final Logger LOGGER = LoggerFactory
      .getLogger(ProfileBaselineProcessingEligibilityVerifierImpl.class);
  private ProfileBaselineQueueModel model;
  private ProfileBaselineProcessingEligibilityVerifierDao dao4core;
  private ProfileBaselineProcessingEligibilityVerifierDao dao4ds;

  ProfileBaselineProcessingEligibilityVerifierImpl(DBI dbi4core, DBI dbi4ds) {
    this.dbi4core = dbi4core;
    this.dbi4ds = dbi4ds;
  }

  @Override
  public boolean isEligibleForProcessing(ProfileBaselineQueueModel model) {
    this.model = model;
    if (!recordIsStillInDispatchedState()) {
      LOGGER.debug("Record is not found to be in dispatched state, may be processed already.");
      return false;
    }
    if (wasBaselineAlreadyDone()) {
      LOGGER.debug("Profile baseline was already done");
      return false;
    }

    return true;
  }


  private boolean wasBaselineAlreadyDone() {
    if (model.getClassId() == null) {
      return fetchDsDao().profileBaselineDoneForUserInIL(model);
    }
    return fetchDsDao().profileBaselineDoneForUserInClass(model);
  }

  private boolean recordIsStillInDispatchedState() {
    return fetchCoreDao().isQueuedRecordStillDispatched(model.getId());
  }

  private ProfileBaselineProcessingEligibilityVerifierDao fetchCoreDao() {
    if (dao4core == null) {
      dao4core = dbi4core.onDemand(ProfileBaselineProcessingEligibilityVerifierDao.class);
    }
    return dao4core;
  }

  private ProfileBaselineProcessingEligibilityVerifierDao fetchDsDao() {
    if (dao4ds == null) {
      dao4ds = dbi4ds.onDemand(ProfileBaselineProcessingEligibilityVerifierDao.class);
    }
    return dao4ds;
  }

}
