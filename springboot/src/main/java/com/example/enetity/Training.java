package com.example.enetity;

import java.time.LocalDate;

public class Training {
    private Long id;
    private String title;      // 培训名称（VARCHAR(100)）
    private String description; // 培训内容（TEXT）
    private LocalDate date;     // 培训日期（DATE）


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
