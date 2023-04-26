package com.epam.esm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

/**
 * class representing tag entity
 *
 * @author bakhridinova
 */

@Data
@Entity
@Builder
@Table(name = "tags")
@ToString(exclude = "certificates")
@EqualsAndHashCode(exclude="certificates")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Tag implements Identifiable, Comparable<Tag> {
    @Id
    @Column(name = "tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "name",
            nullable = false,
            unique = true)
    private String name;

    @ManyToMany(mappedBy = "tags")
    Set<Certificate> certificates;

    @Override
    public int compareTo(Tag o) {
        return this.getName().compareTo(o.getName());
    }
}
