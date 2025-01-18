package com.baekyaton.backend.domain.house.entity;

import com.baekyaton.backend.domain.user.entity.User;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "house")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(name = "thumbnail_url", nullable = false)
    private String thumbnailUrl;

    @ElementCollection
    @CollectionTable(name = "house_image", joinColumns = @JoinColumn(name = "house_id"))
    private List<String> imageUrls = new ArrayList<>();

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column
    private int size;

    private String description;

    private int likeCount;

    private int price;

    public void postLike() {
        likeCount++;
    }

    public void postDislike() {
        likeCount--;
    }
}
