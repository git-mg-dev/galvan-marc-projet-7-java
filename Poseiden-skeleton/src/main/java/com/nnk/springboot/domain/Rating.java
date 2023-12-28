package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rating")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    @NotEmpty(message = "Moody's rating is mandatory")
    @Column(name = "moodys_rating")
    private String moodysRating;

    @NotNull
    @NotEmpty(message = "Sand P. rating is mandatory")
    @Column(name = "sand_p_rating")
    private String sandPRating;

    @NotNull
    @NotEmpty(message = "Fitch rating is mandatory")
    @Column(name = "fitch_rating")
    private String fitchRating;

    @NotNull(message = "Must not be null")
    @Min(value = 0, message = "Order number must be at least 0")
    @Column(name = "order_number")
    private Integer orderNumber;

    public Rating(String moodysRating, String sandPRating, String fitchRating, Integer orderNumber) {
        this.moodysRating = moodysRating;
        this.sandPRating = sandPRating;
        this.fitchRating = fitchRating;
        this.orderNumber = orderNumber;
    }
}
