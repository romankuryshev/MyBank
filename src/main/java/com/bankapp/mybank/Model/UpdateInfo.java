package com.bankapp.mybank.Model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class UpdateInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long updateId;

    @Enumerated(EnumType.STRING)
    private UpdateType updateType;
    private LocalDate updateDate;

    public UpdateInfo() {}

    public UpdateInfo(UpdateType updateType, LocalDate updateDate) {
        this.updateType = updateType;
        this.updateDate = updateDate;
    }

    public Long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Long updateId) {
        this.updateId = updateId;
    }

    public UpdateType getUpdateType() {
        return updateType;
    }

    public void setUpdateType(UpdateType updateType) {
        this.updateType = updateType;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }
}
