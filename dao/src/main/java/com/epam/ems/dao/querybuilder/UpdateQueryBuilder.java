package com.epam.ems.dao.querybuilder;

import com.epam.ems.dao.constant.DBMetadata;
import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.Tag;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.sql.Timestamp;
import java.util.*;

@Component
public class UpdateQueryBuilder {
    private static final String SQL_CREATE_TAG_LINK = String.format(
            "INSERT INTO %s (%s,%s) VAlUES",
            DBMetadata.CERT_HAS_TAG_TABLE, DBMetadata.CERT_HAS_TAG_TABLE_ID_CERT,
            DBMetadata.CERT_HAS_TAG_TABLE_ID_TAG);
    private static final String SQL_REMOVE_TAG_LINK = String.format(
            "DELETE FROM %s WHERE ",
            DBMetadata.CERT_HAS_TAG_TABLE);

    private final MultiValueMap<String, List<Object>> queries = new LinkedMultiValueMap<>();
    private final List<Object> totalParams = new ArrayList<>();
    private final StringBuilder fullQuery = new StringBuilder();
    private GiftCertificate updatedCertificate;
    private GiftCertificate oldCertificate;

    public void parse(GiftCertificate newCertificate, GiftCertificate oldCertificate){

        totalParams.clear();
        queries.clear();
        fullQuery.setLength(0);

        this.updatedCertificate = newCertificate;
        this.oldCertificate = oldCertificate;

        if(oldCertificate == null || newCertificate == null){
            return;
        }
        buildQueries();
        for(String query : queries.keySet()){
            appendQueriesAndParameters(query);
        }
    }

    private void appendQueriesAndParameters(String query){
        for(List<Object> parameters : queries.get(query)){
            fullQuery.append(query).append(";\n ");
            totalParams.addAll(parameters);
        }
    }

    public Object[] getParams() {
        return totalParams.toArray();
    }

    public String getQuery() {
        return fullQuery.toString();
    }

    private void buildQueries(){
        List<Object> params = new ArrayList<>();
        Map<String,Object> toUpdate = getMapOfParamsToUpdate();
        if(!toUpdate.isEmpty()){
            String conditions = buildConditionsAndParams(toUpdate, params);
            params.add(oldCertificate.getId());
            queries.add(String.format("UPDATE %s SET", DBMetadata.CERTIFICATES_TABLE) +
                            conditions +
                            String.format("WHERE %s=?", DBMetadata.CERTIFICATES_TABLE_ID),
                    params);
        }
        queries.putAll(getTagUpdateQueries());
    }

    private String buildConditionsAndParams(Map<String,Object> paramsToUpdate, List<Object> params){
        int paramsLeft = paramsToUpdate.entrySet().size();
        String newDesc = (String) paramsToUpdate.get(DBMetadata.CERTIFICATES_TABLE_DESC);
        if(newDesc != null
                && (newDesc.equalsIgnoreCase("null") || newDesc.isBlank())){
            paramsToUpdate.replace(DBMetadata.CERTIFICATES_TABLE_DESC, null);
        }
        StringBuilder updateColumnsQuery = new StringBuilder();
        for(Map.Entry<String,Object> parameter : paramsToUpdate.entrySet()){
            updateColumnsQuery
                    .append(" ")
                    .append(parameter.getKey())
                    .append("=?")
                    .append(paramsLeft-- > 1 ? ", " : " ");
            params.add(parameter.getValue());
        }
        return updateColumnsQuery.toString();
    }

    private MultiValueMap<String, List<Object>> getTagUpdateQueries(){
        MultiValueMap<String, List<Object>> result = new LinkedMultiValueMap<>();

        if(updatedCertificate.getTags() == null){
            return result;
        }

        List<Tag> oldTags = oldCertificate.getTags();
        List<Tag> updatedTags = updatedCertificate.getTags();

        List<Tag> commonTags = getCommonTags(oldTags, updatedTags);

        oldTags.removeAll(commonTags);
        updatedTags.removeAll(commonTags);

        result.addAll(getQueryForRemovalOfUnusedTagLinks(oldTags));
        result.addAll(getQueryForAdditionOfNewTagLinks(updatedTags));
        return result;
    }

    private List<Tag> getCommonTags(List<Tag> oldTags, List<Tag> newTags){
        List<Tag> commonTags = new ArrayList<>(oldTags);
        commonTags.retainAll(newTags);

        return commonTags;
    }

    private MultiValueMap<String, List<Object>> getQueryForRemovalOfUnusedTagLinks(List<Tag> unusedTags){
        MultiValueMap<String, List<Object>> result = new LinkedMultiValueMap<>();
        if(!CollectionUtils.isEmpty(unusedTags)){
            StringBuilder query = new StringBuilder(SQL_REMOVE_TAG_LINK);
            List<Object> params = new ArrayList<>();
            int tagsLeft = unusedTags.size();
            for(Tag tag : unusedTags){
                query.append(String.format("(%s=? AND %s=?)",
                                DBMetadata.CERT_HAS_TAG_TABLE_ID_CERT,
                                DBMetadata.CERT_HAS_TAG_TABLE_ID_TAG))
                        .append(tagsLeft-- > 1 ? " OR " : "");
                params.add(oldCertificate.getId());
                params.add(tag.getId());
            }
            result.add(query.toString(), params);
        }
        return result;
    }

    private MultiValueMap<String, List<Object>> getQueryForAdditionOfNewTagLinks(List<Tag> addedTags){
        MultiValueMap<String, List<Object>> result = new LinkedMultiValueMap<>();
        if(!CollectionUtils.isEmpty(addedTags)){
            StringBuilder query = new StringBuilder(SQL_CREATE_TAG_LINK);
            List<Object> params = new ArrayList<>();
            int tagsLeft = addedTags.size();
            for(Tag tag : addedTags){
                query.append("(?,?)").append(tagsLeft-- > 1 ? "," : "");
                params.add(oldCertificate.getId());
                params.add(tag.getId());
            }
            result.add(query.toString(), params);
        }
        return result;
    }

    private Map<String,Object> getMapOfParamsToUpdate(){
        Map<String,Object> oldParams = getMapOfFields(oldCertificate);
        Map<String,Object> newParams = getMapOfFields(updatedCertificate);

        newParams.entrySet().removeAll(oldParams.entrySet());
        newParams.entrySet().removeIf(e-> e.getValue() == null);
        return newParams;
    }

    private Map<String, Object> getMapOfFields(GiftCertificate certificate){
        Map<String, Object> res = new HashMap<>();
        res.put(DBMetadata.CERTIFICATES_TABLE_NAME, certificate.getName());
        res.put(DBMetadata.CERTIFICATES_TABLE_DESC, certificate.getDescription());
        res.put(DBMetadata.CERTIFICATES_TABLE_PRICE, certificate.getPrice());
        res.put(DBMetadata.CERTIFICATES_TABLE_DURATION, certificate.getDuration());
        res.put(DBMetadata.CERTIFICATES_TABLE_CREATED, certificate.getCreateDate() == null ? null : Timestamp.valueOf(certificate.getCreateDate()));
        res.put(DBMetadata.CERTIFICATES_TABLE_LAST_UPDATE, certificate.getLastUpdateDate() == null ? null : Timestamp.valueOf(certificate.getLastUpdateDate()));
        return res;
    }

}
