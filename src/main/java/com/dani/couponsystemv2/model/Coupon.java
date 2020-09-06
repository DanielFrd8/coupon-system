package com.dani.couponsystemv2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@NoArgsConstructor
@Entity
@Table(name = "coupon")
public class Coupon extends BaseEntity {

    @Getter
    @NotEmpty
    @Length(min = 2, max = 30)
    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Getter
    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Getter
    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Getter
    @Column(name = "amount", nullable = false)
    private int amount;

    @Getter
    @Column(name = "price", nullable = false)
    private double price;

    @Getter
    @NotEmpty
    @Lob
    @Column(name = "image")
    private String image;

    @Getter
    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "coupons")
    private List<Customer> customers = new ArrayList<>();

    @Getter
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    public Coupon(String title, LocalDate startDate, LocalDate endDate, int amount, double price, String image, String description) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.price = price;
        this.image = image;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", amount=" + amount +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", description='" + description +
                '}';
    }

    public List<Customer> customerList(){
        return customers;
    }
}
