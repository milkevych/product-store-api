package org.projects.notificationservice.store.entity;

public record User(
        String id,
        String firstname,
        String lastname,
        String email
){}
