package com.epam.ems.dao.entity;

import com.epam.ems.dao.audit.AuditEntityListener;
import com.epam.ems.dao.constant.DBMetadata;
import com.epam.ems.dao.entity.role.Role;
import com.epam.ems.dao.entity.role.RoleConverter;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = DBMetadata.USER_TABLE)
@EntityListeners(AuditEntityListener.class)
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBMetadata.USER_TABLE_ID)
    private Long id;

    @Column(name = DBMetadata.USER_TABLE_USERNAME)
    private String username;

    @Column(name = DBMetadata.USER_TABLE_EMAIL)
    private String email;

    @Column(name = DBMetadata.USER_TABLE_PASSWORD)
    private String password;

    @Convert(converter = RoleConverter.class)
    @Column(name = "id_role")
    private Role role;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST},
            fetch = FetchType.LAZY,
            orphanRemoval = true, mappedBy = "user")
    @ToString.Exclude
    private Set<Order> orders;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
