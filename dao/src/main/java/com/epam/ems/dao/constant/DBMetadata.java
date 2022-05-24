package com.epam.ems.dao.constant;

public final class DBMetadata {
    public static final String CERTIFICATES_TABLE="gift_certificate";
    public static final String CERTIFICATES_TABLE_ID="certificate_id";
    public static final String CERTIFICATES_TABLE_NAME="certificate_name";
    public static final String CERTIFICATES_TABLE_DESC="certificate_description";
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

    public static final String USER_TABLE="users";
    public static final String USER_TABLE_ID="id_users";
    public static final String USER_TABLE_EMAIL="email";
    public static final String USER_TABLE_PASSWORD="user_password";
    public static final String USER_TABLE_USERNAME="username";

    public static final String ORDER_TABLE="orders";
    public static final String ORDER_TABLE_ID="id_orders";
    public static final String ORDER_TABLE_DATE="purchase_date";
    public static final String ORDER_TABLE_PRICE="order_price";
    public static final String ORDER_TABLE_USER="id_user";

    public static final String ORDER_HAS_CERT_TABLE="orders_has_certificates";
    public static final String ORDER_HAS_CERT_TABLE_ID_ORDER="order_id";
    public static final String ORDER_HAS_CERT_TABLE_ID_CERT="cert_id";


}
