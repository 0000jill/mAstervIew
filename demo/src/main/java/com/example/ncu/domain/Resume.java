package com.example.ncu.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "resume", schema = "ncu", catalog = "")
public class Resume {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "resume_id", nullable = false)
    private int resume_id;
    
    @Basic
	@Column(name = "title", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT '我的履歷'")
    private String title;  // 履歷名稱
    
    @Basic
	@Column(name = "member_id", nullable = false)
	private int memberId;
    
    @Basic
	@Column(name = "gender", nullable = false)
    private String gender;
    
    @Basic
	@Column(name = "age", nullable = false)
    private int age;
    
    @Basic
	@Column(name = "hobby", nullable = false)
    private String hobby;  // 興趣
    
    @Basic
	@Column(name = "school_name", nullable = false)
    private String schoolName;  // 學校名稱
    
    @Basic
	@Column(name = "academic", nullable = false)
    private String academic;  // 學歷 ex:學士,碩士
    
    @Basic
	@Column(name = "department_name", nullable = false)
    private String departmentName;  // 科系名稱
    
    @Basic
	@Column(name = "academic_period", nullable = false)
    private String academicPeriod;  // 就學期間 ex:2020年06月~2024年06月
    
    @Basic
	@Column(name = "status", nullable = false)
    private String status;  //就學狀態 ex:在學
    
    @Basic
	@Column(name = "preferred_type", nullable = false)
    private String preferredType;  //希望性質 ex:全職,實習,兼職
    
    @Basic
	@Column(name = "working_hours", nullable = false)
    private String workingHours;  //上班時段 ex:日班
    
    @Basic
	@Column(name = "position", nullable = false)
    private String position;  // 希望職稱

    public Resume() {
    }//end contructor Resume


    public void setId(int resume_id){ this.resume_id = resume_id; }
    public int getId() { return resume_id; }

    public void setTitle(String title){ this.title = title; }
    public String getTitle() { return title; }
    
    public void setMemberId(int memberId){ this.memberId = memberId; }
    public int getMemberId() { return memberId; }

    public void setGender(String gender){ this.gender = gender; }
    public String getGender() { return gender; }

    public void setAge(int age){ this.age = age; }
    public int getAge() { return age; }

    public void setHobby(String hobby){ this.hobby = hobby; }
    public String getHobby() { return hobby; }
    
    public void setSchoolName(String schoolName){ this.schoolName = schoolName; }
    public String getSchoolName() { return schoolName; }
    
    public void setAcademic(String academic){ this.academic = academic; }
    public String getAcademic() { return academic; }
    
    public void setDepartmentName(String departmentName){ this.departmentName = departmentName; }
    public String getDepartmentName() { return departmentName; }
    
    public void setAcademicPeriod(String academicPeriod){ this.academicPeriod = academicPeriod; }
    public String getAcademicPeriod() { return academicPeriod; }
    
    public void setStatus(String status){ this.status = status; }
    public String getStatus() { return status; }
    
    public void setPreferredType(String preferredType){ this.preferredType = preferredType; }
    public String getPreferredType() { return preferredType; }
    
    public void setWorkingHours(String workingHours){ this.workingHours = workingHours; }
    public String getWorkingHours() { return workingHours; }

    public void setPosition(String position){ this.position = position; }
    public String getPosition() { return position; }

}
