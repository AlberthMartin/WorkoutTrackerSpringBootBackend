package com.GymApp.GymApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CompletedWorkout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDate completedAt; //Date it was completed

    private Long durationSeconds;
    private String notes;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompletedSet> sets = new ArrayList<>();

    public void addSet(CompletedSet set) {
        sets.add(set);
        set.setWorkout(this);
    }

    public void removeSet(CompletedSet set) {
        sets.remove(set);
        set.setWorkout(null);
    }
}
