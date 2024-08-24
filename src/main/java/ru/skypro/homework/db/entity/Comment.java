package ru.skypro.homework.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String text;

    @ManyToOne
    @JoinColumn(name = "ads_id")
    private Ad ad;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
