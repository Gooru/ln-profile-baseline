package org.gooru.profilebaseline.infra.services.baselineprofilereadcacheupdater;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.gooru.profilebaseline.infra.data.ProfileBaselineProcessingContext;
import org.gooru.profilebaseline.infra.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */

class CachedResponseCreatorImpl implements
    CachedResponseCreator {

  private final DBI dbi4ds;
  private ProfileBaselineProcessingContext context;
  private List<String> gutCodes;
  private BaselineProfileReadProfileCachedResponseDao dao;
  private static final Logger LOGGER = LoggerFactory.getLogger(CachedResponseCreatorImpl.class);
  private List<CachedResponseModel> models;
  private CachedResponse cachedResponse;

  CachedResponseCreatorImpl(DBI dbi4ds) {
    this.dbi4ds = dbi4ds;
  }

  @Override
  public String createCachedResponse(ProfileBaselineProcessingContext context,
      List<String> gutCodes) {

    this.context = context;
    this.gutCodes = gutCodes;

    initializeCompetencyMatrixModels();

    createCachedResponse();

    return transformedResponse();
  }

  private String transformedResponse() {
    String resultString;
    try {
      resultString = new ObjectMapper().writeValueAsString(cachedResponse);
    } catch (JsonProcessingException e) {
      LOGGER.warn("Exception while trying to convert cached response to string", e);
      throw new IllegalStateException("Exception while trying to convert cached response to string",
          e);
    }

    return resultString;
  }

  private void createCachedResponse() {
    cachedResponse = new BaselineProfileReadProfileCachedResponseBuilder().build(models);
  }

  private void initializeCompetencyMatrixModels() {
    models = fetchDao().mapBaselinedProfileToDomainCompetencyMatrix(
        CollectionUtils.convertToSqlArrayOfString(gutCodes), context.getSubject());
  }

  private BaselineProfileReadProfileCachedResponseDao fetchDao() {
    if (dao == null) {
      dao = dbi4ds.onDemand(BaselineProfileReadProfileCachedResponseDao.class);
    }
    return dao;
  }
}
