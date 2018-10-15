package org.gooru.profilebaseline.processors.doprofilebaseline;

import org.gooru.profilebaseline.infra.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author ashish.
 */

interface DoLearnerProfileBaselineDao {


  @SqlQuery("delete from learner_profile_baselined where user_id = any(:userIds) and course_id = :courseId and class_id is null")
  void resetProfileBaselineInfoForILForSpecifiedUsers(@Bind("userIds") PGArray<String> userIds,
      @Bind("courseId") String courseId);


  @SqlQuery("delete from learner_profile_baselined where user_id = any(:userIds) and course_id = :courseId and class_id = :classId")
  void resetProfileBaselineInfoInClassForSpecifiedUsers(@Bind("userIds") PGArray<String> userIds,
      @Bind("courseId") String courseId, @Bind("classId") String classId);

  @SqlQuery("delete from learner_profile_baselined where course_id = :courseId and class_id = :classId")
  void resetProfileBaselineInfoInClassForAllUsers(@Bind("courseId") String courseId,
      @Bind("classId") String classId);
}
