package com.epam.ems.dao.constant;

public final class DBMetadata {
    public static final String CERTIFICATES_TABLE="gift_certificate";
    public static final String CERTIFICATES_TABLE_ID="id";
    public static final String CERTIFICATES_TABLE_NAME="name";
    public static final String CERTIFICATES_TABLE_DESC="description";
    public static final String CERTIFICATES_TABLE_PRICE="price";
    public static final String CERTIFICATES_TABLE_DURATION="duration";
    public static final String CERTIFICATES_TABLE_CREATED="create_date";
    public static final String CERTIFICATES_TABLE_LAST_UPDATE="last_update_date";

    public static final String TAG_TABLE="tag";
    public static final String TAG_TABLE_ID="tag_id";
    public static final String TAG_TABLE_NAME="tag_name";

    public static final String CERT_HAS_TAG_TABLE="certificate_has_tag";
    public static final String CERT_HAS_TAG_TABLE_ID_CERT="id_certificate";
    public static final String CERT_HAS_TAG_TABLE_ID_TAG="id_tag";
}
