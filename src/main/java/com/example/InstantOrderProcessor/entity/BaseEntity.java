package com.example.InstantOrderProcessor.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@ToString
public class BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Id
    private Long id;

    @Column(name = "created_at")
    private Date CreatedAt;

    @Column(name = "updated_at")
    private Date UpdatedAt;

    public BaseEntity() {

    }

    @PrePersist
    void createDate() {
        if(this.CreatedAt == null) {
            this.setCreatedAt(new Date());
        }
        this.setUpdatedAt(new Date());
    }

    @PreUpdate
    void updatedAt() {this.setUpdatedAt(new Date());}
}
