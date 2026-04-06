package com.finance.finance_backend.model;

public enum Role {
    VIEWER,   // can only view dashboard data
    ANALYST,  // can view records and access insights
    ADMIN     // can create, update, delete records and manage users
}

