package com.example.ncu.domain;

import java.util.List;

public class ResumeWithExpertise {
	
	    private Resume resume;
	    private List<Expertise> expertiseList;
	    private List<Special> specialList;
	    private List<Workexp> workexpList;

	    public Resume getResume() {
	        return resume;
	    }

	    public void setResume(Resume resume) {
	        this.resume = resume;
	    }

	    public List<Expertise> getExpertiseList() {
	        return expertiseList;
	    }

	    public void setExpertiseList(List<Expertise> expertiseList) {
	        this.expertiseList = expertiseList;
	    }
	    
	    public List<Special> getSpecialList() {
	        return specialList;
	    }

	    public void setSpecialList(List<Special> specialList) {
	        this.specialList = specialList;
	    }
	    
	    public List<Workexp> getWorkexpList() {
	        return workexpList;
	    }

	    public void setWorkexpList(List<Workexp> workexpList) {
	        this.workexpList = workexpList;
	    }

}

