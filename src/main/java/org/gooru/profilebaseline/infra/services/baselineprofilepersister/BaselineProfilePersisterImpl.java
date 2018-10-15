package org.gooru.profilebaseline.infra.services.baselineprofilepersister;

import java.util.ArrayList;
import java.util.List;
import org.gooru.profilebaseline.infra.data.ProfileBaselineProcessingContext;
import org.gooru.profilebaseline.infra.services.algebra.competency.Competency;
import org.gooru.profilebaseline.infra.services.algebra.competency.CompetencyLine;
import org.gooru.profilebaseline.infra.services.algebra.competency.DomainCode;
import org.gooru.profilebaseline.infra.services.baselineprofilereadcacheupdater.CachedResponseCreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class BaselineProfilePersisterImpl implements BaselineProfilePersister {

  private final DBI dbi4ds;
  private BaselineProfilePersisterDao dsdao;
  private ProfileBaselineProcessingContext context;
  private CompetencyLine resultLine;
  private List<String> gutCodesToPersist;
  private String lpDataString;

  BaselineProfilePersisterImpl(DBI dbi4ds) {
    this.dbi4ds = dbi4ds;
  }

  @Override
  public Long persist(ProfileBaselineProcessingContext context, CompetencyLine resultLine) {
    this.context = context;
    this.resultLine = resultLine;

    initializeGutCodesForProfileFromCompetencyLine();
    initializeCachedResponse();

    LearnerProfileBaselinedModel model = LearnerProfileBaselinedModel
        .build(context, gutCodesToPersist, lpDataString);

    return fetchDsDao().persistLearnerProfileBaselined(model.asBean());
  }

  private void initializeCachedResponse() {
    lpDataString = CachedResponseCreator.build(dbi4ds)
        .createCachedResponse(context, gutCodesToPersist);
  }

  private void initializeGutCodesForProfileFromCompetencyLine() {
    List<DomainCode> domains = resultLine.getDomains();
    gutCodesToPersist = new ArrayList<>();
    for (DomainCode domain : domains) {
      Competency competencyForDomain = resultLine.getCompetencyForDomain(domain);
      if (competencyForDomain != null) {
        gutCodesToPersist.add(competencyForDomain.getCompetencyCode().getCode());
      }
    }
  }

  private BaselineProfilePersisterDao fetchDsDao() {
    if (dsdao == null) {
      dsdao = dbi4ds.onDemand(BaselineProfilePersisterDao.class);
    }
    return dsdao;
  }
}
