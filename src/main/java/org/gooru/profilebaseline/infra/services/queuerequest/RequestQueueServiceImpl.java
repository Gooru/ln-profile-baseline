package org.gooru.profilebaseline.infra.services.queuerequest;

import java.util.List;
import java.util.UUID;
import org.gooru.profilebaseline.infra.data.ProfileBaselineContext;
import org.gooru.profilebaseline.infra.data.ProfileBaselineQueueModel;
import org.gooru.profilebaseline.infra.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 18/5/18.
 */
class RequestQueueServiceImpl implements RequestQueueService {

  private static final Logger LOGGER = LoggerFactory.getLogger(RequestQueueService.class);
  private final DBI dbi;
  private ProfileBaselineContext context;
  private RequestQueueDao queueDao;

  RequestQueueServiceImpl(DBI dbi) {
    this.dbi = dbi;
  }

  @Override
  public void enqueue(ProfileBaselineContext context) {
    this.context = context;
    queueDao = dbi.onDemand(RequestQueueDao.class);
    if (context.getClassId() != null) {
      doQueueingForClass();
    } else {
      doQueueingForIL();
    }

  }

  private void doQueueingForIL() {
    if (context.getCourseId() == null) {
      LOGGER.warn("Profile baseline fired for IL without courseId. Abort.");
      return;
    }
    if (!queueDao.isCourseNotDeleted(context.getCourseId())) {
      LOGGER.warn("Profile baseline fired for deleted or not existing course: '{}'. Abort.",
          context.getCourseId());
      return;
    }
    queueInDb();
  }

  private void doQueueingForClass() {
    if (!queueDao.isClassNotDeletedAndNotArchived(context.getClassId())) {
      LOGGER
          .warn("Profile baseline fired for archived or deleted class: '{}'", context.getClassId());
      return;
    }
    UUID courseId = queueDao.fetchCourseForClass(context.getClassId());
    if (courseId == null) {
      LOGGER.warn("No course associated with class: '{}'. Will not rescope", context.getClassId());
      return;
    }

    if (context.getCourseId() != null && !context.getCourseId().equals(courseId)) {
      LOGGER.warn(
          "Course specified in request '{}' does not match course associated with class '{}'. Will use "
              + "the one associated with class", context.getCourseId(), courseId);
    }

    populateMemberships(courseId);
    queueInDb();
  }

  private void populateMemberships(UUID courseId) {
    if (context.isOOBRequest() || context.areUsersJoiningClass()) {
      // Validate membership of provided users
      List<UUID> existingMembersOfClassFromSpecifiedList = queueDao.fetchSpecifiedMembersOfClass(
          context.getClassId(),
          CollectionUtils.convertFromListUUIDToSqlArrayOfUUID(context.getMemberIds()));

      if (existingMembersOfClassFromSpecifiedList.size() < context.getMemberIds().size()) {
        LOGGER.warn("Not all specified users are members of class. Will process only members");
      }
      context = context.createNewContext(existingMembersOfClassFromSpecifiedList, courseId);
    } else {
      List<UUID> members = queueDao.fetchMembersOfClass(context.getClassId());
      context = context.createNewContext(members, courseId);
    }
  }

  private void queueInDb() {
    queueDao.queueRequests(context.getMemberIds(),
        ProfileBaselineQueueModel.fromLpBaselineContextNoMembers(context));
  }

}
