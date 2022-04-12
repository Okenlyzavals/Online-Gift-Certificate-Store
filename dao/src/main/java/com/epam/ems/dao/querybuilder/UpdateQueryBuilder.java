package com.epam.ems.dao.querybuilder;

import com.epam.ems.dao.constant.DBMetadata;
import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.Tag;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.sql.Timestamp;
import java.util.*;

@Component
public class UpdateQueryBuilder {
    private static final String SQL_CREATE_TAG_LINK = String.format(
            "INSERT INTO %s (%s,%s) VAlUES(?,?)",
            DBMetadata.CERT_HAS_TAG_TABLE, DBMetadata.CERT_HAS_TAG_TABLE_ID_CERT,
            DBMetadata.CERT_HAS_TAG_TABLE_ID_TAG);
    private static final String SQL_REMOVE_TAG_LINK = String.format(
            "DELETE FROM %s WHERE %s=? AND %s=?",
            DBMetadata.CERT_HAS_TAG_TABLE, DBMetadata.CERT_HAS_TAG_TABLE_ID_CERT,
            DBMetadata.CERT_HAS_TAG_TABLE_ID_TAG);

    private final MultiValueMap<String, List<Object>> queries = new LinkedMultiValueMap<>();
    private final StringBuilder updateColumnsQuery = new StringBuilder();
    private final List<Object> params = new ArrayList<>();
    private GiftCertificate newCertificate;
    private GiftCertificate oldCertificate;

    public MultiValueMap<String, List<Object>> parse(GiftCertificate newCertificate, GiftCertificate oldCertificate){
        if(oldCertificate == null || newCertificate == null){
            return new LinkedMultiValueMap<>();
        }

        params.clear();
        queries.clear();
        updateColumnsQuery.setLength(0);

        this.newCertificate = newCertificate;
        this.oldCertificate = oldCertificate;

        buildQueries();
        return queries;
    }

    private void buildQueries(){
        Map<String,Object> toUpdate = getMapOfParamsToUpdate();
        if(!toUpdate.isEmpty()){
            buildConditionsAndParams(toUpdate);
            params.add(newCertificate.getId());
            queries.add(String.format("UPDATE %s SET ", DBMetadata.CERTIFICATES_TABLE) +
                            updateColumnsQuery +
                            String.format("WHERE %s=?", DBMetadata.CERTIFICATES_TABLE_ID),
                    params);
           queries.putAll(getTagUpdateQueries());
        }
    }

    private void buildConditionsAndParams(Map<String,Object> paramsToUpdate){
        int size = paramsToUpdate.entrySet().size();
        String newDesc = (String) paramsToUpdate.get(DBMetadata.CERTIFICATES_TABLE_DESC);
        if(newDesc != null
                && (newDesc.equalsIgnoreCase("null") || newDesc.isBlank())){
            paramsToUpdate.replace(DBMetadata.CERTIFICATES_TABLE_DESC, null);
        }
        for(Map.Entry<String,Object> parameter : paramsToUpdate.entrySet()){
            updateColumnsQuery
                    .append(" ")
                    .append(parameter.getKey())
                    .append(" =?")
                    .append(size>1 ? ", " : " ");
            params.add(parameter.getValue());
            size--;
        }
    }

    private MultiValueMap<String, List<Object>> getTagUpdateQueries(){
        MultiValueMap<String, List<Object>> result = new LinkedMultiValueMap<>();

        if(newCertificate.getTags() == null){
            return result;
        }
        HashSet<Tag> oldTags = new HashSet<>(oldCertificate.getTags());
        HashSet<Tag> newTags = new HashSet<>(newCertificate.getTags());

        HashSet<Tag> temp = new HashSet<>(oldTags);

        temp.retainAll(newTags);
        oldTags.removeAll(temp);
        newTags.removeAll(temp);
        for(Tag tag : oldTags){
            result.add(
                    SQL_REMOVE_TAG_LINK,
                    Arrays.asList(newCertificate.getId(),tag.getId()));
        }
        for(Tag tag : newTags){
            result.add(
                    SQL_CREATE_TAG_LINK,
                    Arrays.asList(newCertificate.getId(),tag.getId()));
        }
        return result;
    }

    private Map<String,Object> getMapOfParamsToUpdate(){
        Map<String,Object> oldParams = getMapOfFields(oldCertificate);
        Map<String,Object> newParams = getMapOfFields(newCertificate);

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
