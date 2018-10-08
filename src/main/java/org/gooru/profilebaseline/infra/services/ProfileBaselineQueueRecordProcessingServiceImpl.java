package org.gooru.profilebaseline.infra.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gooru.profilebaseline.infra.data.ProfileBaselineQueueModel;
import org.gooru.profilebaseline.infra.services.queueoperators.ProfileBaselineDequeuer;
import org.gooru.profilebaseline.infra.services.queueoperators.ProfileBaselineProcessingEligibilityVerifier;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */

class ProfileBaselineQueueRecordProcessingServiceImpl implements
    ProfileBaselineQueueRecordProcessingService {

  private final DBI dbi4core;
  private final DBI dbi4ds;
  private ProfileBaselineQueueModel model;
  private static final Logger LOGGER = LoggerFactory
      .getLogger(ProfileBaselineQueueRecordProcessingServiceImpl.class);

  ProfileBaselineQueueRecordProcessingServiceImpl(DBI dbi4core, DBI dbi4ds) {
    this.dbi4core = dbi4core;
    this.dbi4ds = dbi4ds;
  }

  @Override
  public void doProfileBaseline(ProfileBaselineQueueModel model) {
    this.model = model;
    if (!ProfileBaselineProcessingEligibilityVerifier.build(dbi4core)
        .isEligibleForProcessing(model)) {
      LOGGER.debug("Record is not found to be in dispatched state, may be processed already.");
      dequeueRecord();
      return;
    }
    processRecord();
  }

  private void dequeueRecord() {
    LOGGER.debug("Dequeueing record");
    ProfileBaselineDequeuer.build(dbi4core).dequeue(model);
  }

  private void processRecord() {
    LOGGER.debug("Doing real processing");
    // TODO: Implement this

    // Don't let exception leak, handle it here
/*
    try {
      SkippedItemsResponse items = RescopeProcessor.buildRescopeProcessor()
          .rescopedItems(RescopeProcessorContext.buildFromRescopeQueueModel(model));
      ObjectMapper mapper = new ObjectMapper();
      try {
        String skippedItemsString = mapper.writeValueAsString(items);
        dao.persistRescopedContent(model, skippedItemsString);
      } catch (JsonProcessingException e) {
        LOGGER.warn("Not able to convert skipped items to JSON for model '{}'", model.toJson(), e);
      }
    } catch (Exception e) {
      LOGGER.warn("Not able to do rescope for model: '{}'. Will dequeue record.", e);
    } finally {
      dequeueRecord();
    }
*/
  }


}
