package org.gooru.profilebaseline.infra.services.queueoperators;

import org.gooru.profilebaseline.infra.data.ProfileBaselineQueueModel;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

interface ProfileBaselineQueueOperatorDao {

  @SqlUpdate("update profile_baseline_queue set status = 0 where status != 0")
  void initializeQueueStatus();

  @Mapper(ProfileBaselineQueueModel.ProfileBaselineQueueModelMapper.class)
  @SqlQuery(
      "select id, user_id, course_id, class_id, priority, status from profile_baseline_queue where status = 0 order by"
          + " priority desc limit 1")
  ProfileBaselineQueueModel getNextDispatchableModel();

  @SqlUpdate("update profile_baseline_queue set status = 1 where id = :modelId")
  void setQueuedRecordStatusAsDispatched(@Bind("modelId") Long id);

}
