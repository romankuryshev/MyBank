package com.bankapp.mybank.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;

public enum UpdateType {

    CARD,
    DEPOSIT,
    CREDIT,
    ALL;

    UpdateType() {
    }
}
