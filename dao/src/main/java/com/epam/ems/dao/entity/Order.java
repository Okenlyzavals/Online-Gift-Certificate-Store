package com.epam.ems.dao.entity;

import com.epam.ems.dao.audit.AuditEntityListener;
import com.epam.ems.dao.constant.DBMetadata;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = DBMetadata.ORDER_TABLE)
@EntityListeners(AuditEntityListener.class)
public class Order{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBMetadata.ORDER_TABLE_ID)
    private Long id;

    @Column(name = DBMetadata.ORDER_TABLE_PRICE)
    private BigDecimal price;

    @Column(name = DBMetadata.ORDER_TABLE_DATE)
    private LocalDateTime date;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name = DBMetadata.ORDER_TABLE_USER, referencedColumnName = DBMetadata.USER_TABLE_ID)
    private User user;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(
            name = DBMetadata.ORDER_HAS_CERT_TABLE,
            joinColumns = {@JoinColumn(name = DBMetadata.ORDER_HAS_CERT_TABLE_ID_ORDER)},
            inverseJoinColumns = {@JoinColumn(name = DBMetadata.ORDER_HAS_CERT_TABLE_ID_CERT)}
    )
    @ToString.Exclude
    private Set<GiftCertificate> certificates;

    @PrePersist
    public void onPrePersist(){
        if (price == null){
            price = new BigDecimal("0.000");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Order order = (Order) o;
        return id != null && Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
