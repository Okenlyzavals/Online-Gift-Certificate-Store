package com.epam.ems.dao.entity;

import com.epam.ems.dao.audit.AuditEntityListener;
import com.epam.ems.dao.constant.DBMetadata;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = DBMetadata.TAG_TABLE)
@EntityListeners(AuditEntityListener.class)
@NamedStoredProcedureQuery(
        name="Tag.GetMostUsedTag",
        procedureName="GetMostUsedTag",
        resultClasses = { Tag.class }
)
public class Tag{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBMetadata.TAG_TABLE_ID)
    private Long id;

    @Column(name = DBMetadata.TAG_TABLE_NAME)
    private String name;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<GiftCertificate> certificates;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Tag tag = (Tag) o;
        return id != null && Objects.equals(id, tag.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
