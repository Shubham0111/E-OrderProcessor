package com.example.InstantOrderProcessor.entity;

import com.example.InstantOrderProcessor.enums.ItemStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "items")
@Getter
@Setter
public class Items extends BaseEntity{
   @Column(name = "item_name")
   private String name;
   @Column(name = "item_description")
   private String description;
   @Column(name = "item_price")
   private Double price;
   @Column(name = "item_units_available")
   private Long unitsAvailable;
   @Column(name = "item_status")
   private ItemStatus status;

}
