package com.example.ncu.domain;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//履歷中的特殊經歷欄位 但可輸好幾個 so自己一個table
@Entity
@Table(name = "special", schema = "ncu", catalog = "")
public class Special {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "special_id", nullable = false)
	private int specialId;
	
	@Basic
	@Column(name = "resume_id", nullable = false)
	private int resumeId;  // resume的外鍵
	
	@Basic
	@Column(name = "special_name", nullable = false)
	private String specialName;  // 特殊經歷名稱
	
	@Basic
	@Column(name = "special_description", nullable = false)
	private String specialDescription;  // 特殊經歷說明

 
	public void setId(int specialId){ this.specialId = specialId; }
	public int getId() { return specialId; }
 
	public void setResumeId(int resumeId){ this.resumeId = resumeId; }
	public int getResumeId() { return resumeId; }
 
	public void setSpecialName(String specialName){ this.specialName = specialName; }
	public String getSpecialName() { return specialName; }
 
	public void setSpecialDescription(String specialDescription){ this.specialDescription = specialDescription; }
	public String getSpecialDescription() { return specialDescription; }

}