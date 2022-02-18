package cn.edu.bit.patentbackend.bean;

public class PatentWithBLOBs extends Patent {
    private String mainClassList;

    private String classList;

    private String applicantList;

    private String inventorList;

    private String abstract_;

    private String signoryItem;

    private String displayClassList;

    private String fLawstatus;

    public String getMainClassList() {
        return mainClassList;
    }

    public void setMainClassList(String mainClassList) {
        this.mainClassList = mainClassList == null ? null : mainClassList.trim();
    }

    public String getClassList() {
        return classList;
    }

    public void setClassList(String classList) {
        this.classList = classList == null ? null : classList.trim();
    }

    public String getApplicantList() {
        return applicantList;
    }

    public void setApplicantList(String applicantList) {
        this.applicantList = applicantList == null ? null : applicantList.trim();
    }

    public String getInventorList() {
        return inventorList;
    }

    public void setInventorList(String inventorList) {
        this.inventorList = inventorList == null ? null : inventorList.trim();
    }

    public String getAbstract() {
        return abstract_;
    }

    public void setAbstract(String abstract_) {
        this.abstract_ = abstract_ == null ? null : abstract_.trim();
    }

    public String getSignoryItem() {
        return signoryItem;
    }

    public void setSignoryItem(String signoryItem) {
        this.signoryItem = signoryItem == null ? null : signoryItem.trim();
    }

    public String getDisplayClassList() {
        return displayClassList;
    }

    public void setDisplayClassList(String displayClassList) {
        this.displayClassList = displayClassList == null ? null : displayClassList.trim();
    }

    public String getfLawstatus() {
        return fLawstatus;
    }

    public void setfLawstatus(String fLawstatus) {
        this.fLawstatus = fLawstatus == null ? null : fLawstatus.trim();
    }
}