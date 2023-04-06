package com.epam.esm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * class representing tag entity
 *
 * @author bakhridinova
 */

@Data
@Entity
@Table(name = "tags")
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

    @Override
    public int compareTo(Tag o) {
        return this.getName().compareTo(o.getName());
    }
}
