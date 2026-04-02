package com.rampsecure.rampsecure.inspection.model;

import com.rampsecure.rampsecure.equipment.model.Equipment;
import com.rampsecure.rampsecure.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InspectionReport {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User operator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Equipment equipment;

    private LocalDateTime inspectedAt;// when

    private LocalDateTime expiresAt;// inspectedAt + 8 hours

    @Enumerated(EnumType.STRING)
    private ChecklistStatus status;

    @OneToMany( mappedBy = "report", cascade = CascadeType.ALL)
    private List<ItemResult> results; //the individual item results

}
