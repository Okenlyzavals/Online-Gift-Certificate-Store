package com.epam.ems.dao.entity;

import com.epam.ems.dao.audit.AuditEntityListener;
import com.epam.ems.dao.constant.DBMetadata;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = DBMetadata.CERTIFICATES_TABLE)
@EntityListeners(AuditEntityListener.class)
@DynamicUpdate
public class GiftCertificate{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBMetadata.CERTIFICATES_TABLE_ID)
    private Long id;

    @Column(name = DBMetadata.CERTIFICATES_TABLE_NAME)
    private String name;

    @Column(name = DBMetadata.CERTIFICATES_TABLE_DESC)
    private String description;

    @Column(name = DBMetadata.CERTIFICATES_TABLE_PRICE)
    private BigDecimal price;

    @Column(name = DBMetadata.CERTIFICATES_TABLE_DURATION)
    private Integer duration;

    @Column(name = DBMetadata.CERTIFICATES_TABLE_CREATED)
    private LocalDateTime createDate;

    @Column(name = DBMetadata.CERTIFICATES_TABLE_LAST_UPDATE)
    private LocalDateTime lastUpdateDate;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = DBMetadata.CERT_HAS_TAG_TABLE,
            joinColumns = {@JoinColumn(name = DBMetadata.CERT_HAS_TAG_TABLE_ID_CERT,
                    referencedColumnName = DBMetadata.CERTIFICATES_TABLE_ID )},
            inverseJoinColumns = {@JoinColumn(name = DBMetadata.CERT_HAS_TAG_TABLE_ID_TAG,
                    referencedColumnName = DBMetadata.TAG_TABLE_ID )}
    )
    @ToString.Exclude
    private Set<Tag> tags;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "certificates")
    @ToString.Exclude
    private List<Order> orders;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GiftCertificate that = (GiftCertificate) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
