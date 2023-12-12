package com.example.ncu.domain;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


// 履歷中的專長欄位 但可輸好幾個 so自己一個table
@Entity
@Table(name = "expertise", schema = "ncu", catalog = "")
public class Expertise {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "expertise_id", nullable = false)
    private int expertiseId;
	
	@Basic
	@Column(name = "resume_id", nullable = false)
    private int resumeId;  // resume的外鍵
	
	@Basic
	@Column(name = "expertise_name", nullable = false)
    private String expertiseName;  // 專長名稱
	
	@Basic
	@Column(name = "expertise_description", nullable = false)
    private String expertiseDescription;  // 專長說明
    
    public Expertise() {
    }//end contructor Expertise
    
    public void setId(int expertiseId){ this.expertiseId = expertiseId; }
    public int getId() { return expertiseId; }
    
    public void setResumeId(int resumeId){ this.resumeId = resumeId; }
    public int getResumeId() { return resumeId; }
    
    public void setExpertiseName(String expertiseName){ this.expertiseName = expertiseName; }
    public String getExpertiseName() { return expertiseName; }
    
    public void setExpertiseDescription(String expertiseDescription){ this.expertiseDescription = expertiseDescription; }
    public String getExpertiseDescription() { return expertiseDescription; }

}
