package org.gooru.profilebaseline.processors.doprofilebaseline;

import org.gooru.profilebaseline.infra.services.queuerequest.RequestQueueService;
import org.gooru.profilebaseline.infra.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class DoLearnerProfileBaselineService {

  private final DBI dbi4core;
  private final DBI dbi4ds;
  private DoLearnerProfileBaselineCommand command;
  private DoLearnerProfileBaselineDao dao;

  DoLearnerProfileBaselineService(DBI dbi4core, DBI dbi4ds) {
    this.dbi4core = dbi4core;
    this.dbi4ds = dbi4ds;
  }


  void doProfileBaseline(DoLearnerProfileBaselineCommand command) {
    this.command = command;
    if (command.getClassId() != null) {
      doProfileBaselineInClass();
    } else {
      doProfileBaselineForIL();
    }
  }

  private void doProfileBaselineForIL() {
    if (command.isOverride()) {
      resetProfileBaselineForSpecifiedMembersForIL();
    }
    queueProfileBaselineRequests();
  }

  private void doProfileBaselineInClass() {
    if (command.isOverride()) {
      if (command.applyToAllMembers()) {
        resetProfileBaselineForWholeClass();
      } else if (command.hasMembershipInfo()) {
        resetProfileBaselineForSpecifiedMembersInClass();
      }
    }
    queueProfileBaselineRequests();
  }

  private void resetProfileBaselineForSpecifiedMembersForIL() {
    fetchDao().resetProfileBaselineInfoForILForSpecifiedUsers(
        CollectionUtils.convertFromListUUIDToSqlArrayOfUUID(command.getMemberIds()),
        command.getCourseId());
  }

  private void resetProfileBaselineForSpecifiedMembersInClass() {
    fetchDao().resetProfileBaselineInfoInClassForSpecifiedUsers(
        CollectionUtils.convertFromListUUIDToSqlArrayOfUUID(command.getMemberIds()),
        command.getCourseId(),
        command.getClassId());
  }

  private void resetProfileBaselineForWholeClass() {
    fetchDao()
        .resetProfileBaselineInfoInClassForAllUsers(command.getCourseId(), command.getClassId());
  }


  private void queueProfileBaselineRequests() {
    RequestQueueService service = RequestQueueService.build();
    service.enqueue(command.asRescopeContext());
  }

  private DoLearnerProfileBaselineDao fetchDao() {
    if (dao == null) {
      dao = dbi4ds.onDemand(DoLearnerProfileBaselineDao.class);
    }
    return dao;
  }

}
