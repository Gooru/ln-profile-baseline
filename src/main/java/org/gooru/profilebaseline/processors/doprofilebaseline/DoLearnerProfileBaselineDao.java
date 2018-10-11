package org.gooru.profilebaseline.processors.doprofilebaseline;

import java.util.UUID;
import org.gooru.profilebaseline.infra.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author ashish.
 */

interface DoLearnerProfileBaselineDao {


  @SqlQuery("delete from learner_profile_baselined where user_id = any(:userIds) and course_id = :courseId and class_id is null")
  void resetProfileBaselineInfoForILForSpecifiedUsers(@Bind("userIds") PGArray<UUID> userIds,
      @Bind("courseId") UUID courseId);


  @SqlQuery("delete from learner_profile_baselined where user_id = any(:userIds) and course_id = :courseId and class_id = :classId")
  void resetProfileBaselineInfoInClassForSpecifiedUsers(@Bind("userIds") PGArray<UUID> userIds,
      @Bind("courseId") UUID courseId, @Bind("classId") UUID classId);

  @SqlQuery("delete from learner_profile_baselined where course_id = :courseId and class_id = :classId")
  void resetProfileBaselineInfoInClassForAllUsers(@Bind("courseId") UUID courseId,
      @Bind("classId") UUID classId);
}
