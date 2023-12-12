package com.example.ncu.domain;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//履歷中的工作經歷欄位 但可輸好幾個 so自己一個table
@Entity
@Table(name = "workexp", schema = "ncu", catalog = "")
public class Workexp {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "workexp_id", nullable = false)
    private int workexpId;
	
	@Basic
	@Column(name = "resume_id", nullable = false)
    private int resumeId;  // resume的外鍵
	
	@Basic
	@Column(name = "workexp_name", nullable = false)
    private String workexpName;  // 工作經歷名稱
	
	@Basic
	@Column(name = "work_period", nullable = false)
    private String workPeriod;  // 任職期間 ex:2020年06月~2021年06月
	
	@Basic
	@Column(name = "workexp_description", nullable = false)
    private String workexpDescription;  // 工作經歷說明

    
    public void setId(int workexpId){ this.workexpId = workexpId; }
    public int getId() { return workexpId; }
    
    public void setResumeId(int resumeId){ this.resumeId = resumeId; }
    public int getResumeId() { return resumeId; }
    
    public void setWorkexpName(String workexpName){ this.workexpName = workexpName; }
    public String getWorkexpName() { return workexpName; }
    
    public void setWorkPeriod(String workPeriod){ this.workPeriod = workPeriod; }
    public String getWorkPeriod() { return workPeriod; }
    
    public void setWorkexpDescription(String workexpDescription){ this.workexpDescription = workexpDescription; }
    public String getWorkexpDescription() { return workexpDescription; }

}
