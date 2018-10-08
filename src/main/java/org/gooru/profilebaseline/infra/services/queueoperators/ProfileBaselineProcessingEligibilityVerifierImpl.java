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

  private final DBI dbi;
  private static final Logger LOGGER = LoggerFactory
      .getLogger(ProfileBaselineProcessingEligibilityVerifierImpl.class);
  private ProfileBaselineQueueModel model;
  private ProfileBaselineProcessingEligibilityVerifierDao dao;

  ProfileBaselineProcessingEligibilityVerifierImpl(DBI dbi) {
    this.dbi = dbi;
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
      return fetchDao().profileBaselineDoneForUserInIL(model);
    }
    return fetchDao().profileBaselineDoneForUserInClass(model);
  }

  private boolean recordIsStillInDispatchedState() {
    return fetchDao().isQueuedRecordStillDispatched(model.getId());
  }

  private ProfileBaselineProcessingEligibilityVerifierDao fetchDao() {
    if (dao == null) {
      dao = dbi.onDemand(ProfileBaselineProcessingEligibilityVerifierDao.class);
    }
    return dao;
  }

}
