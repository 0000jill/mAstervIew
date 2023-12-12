package com.example.ncu.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "career", schema = "ncu", catalog = "")
public class Career {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "career_id")
    private Integer careerId;

    @Basic
	@Column(name = "title", nullable = false)
    private String title;


    public Integer getCareerId() {
        return careerId;
    }

    public void setCareerId(Integer careerId) {
        this.careerId = careerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
