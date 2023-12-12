package com.example.ncu.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "member", schema = "ncu", catalog = "")
public class Member {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "member_id")
    private int memberId;
    
    @Basic
	@Column(name = "name", nullable = false)
    private String name;

    @Basic
	@Column(name = "email", nullable = false)
    private String email;

    @Basic
	@Column(name = "password", nullable = false)
    private String password;
    
    @Basic
	@Column(name = "profileImage", nullable = false, columnDefinition = "VARCHAR(1000)")
    private String profileImage; // 存照片的檔名
    
    
    @Basic
	@Column(name = "careerId1", nullable = false, columnDefinition = "INT")
    private int careerId1; // 會員選的類別Id1
    
    @Basic
	@Column(name = "careerId2", nullable = false, columnDefinition = "INT")
    private int careerId2; // 會員選的類別Id2
    
    @Basic
	@Column(name = "careerId3", nullable = false, columnDefinition = "INT")
    private int careerId3; // 會員選的類別Id3
    
    // 建構子
    public Member() {
        this.profileImage = "default.png"; // 設定預設的圖片檔案名稱
        this.careerId1 = -1;
        this.careerId2 = -1;
        this.careerId3 = -1;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    
    public int getCareerId1() {
        return careerId1;
    }

    public void setCareerId1(int careerId1) {
        this.careerId1 = careerId1;
    }
    public int getCareerId2() {
        return careerId2;
    }

    public void setCareerId2(int careerId2) {
        this.careerId2 = careerId2;
    }
    public int getCareerId3() {
        return careerId3;
    }

    public void setCareerId3(int careerId3) {
        this.careerId3 = careerId3;
    }

}
