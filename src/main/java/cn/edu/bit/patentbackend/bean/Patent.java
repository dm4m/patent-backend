package cn.edu.bit.patentbackend.bean;

public class Patent {

    //分页
    private Integer index;

    private Integer id;

    private String patentType;

    private String publicationNo;

    private String publicationDate;

    private String patentCode;

    private String applicationDate;

    private String title;

    private String applicantAddress;

    private String agency;

    private String agent;

    private String pageNum;

    private String applicantArea;

    private String displayMainclassCode;

    private String pdf;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPatentType() {
        return patentType;
    }

    public void setPatentType(String patentType) {
        this.patentType = patentType == null ? null : patentType.trim();
    }

    public String getPublicationNo() {
        return publicationNo;
    }

    public void setPublicationNo(String publicationNo) {
        this.publicationNo = publicationNo == null ? null : publicationNo.trim();
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate == null ? null : publicationDate.trim();
    }

    public String getPatentCode() {
        return patentCode;
    }

    public void setPatentCode(String patentCode) {
        this.patentCode = patentCode == null ? null : patentCode.trim();
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate == null ? null : applicationDate.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getApplicantAddress() {
        return applicantAddress;
    }

    public void setApplicantAddress(String applicantAddress) {
        this.applicantAddress = applicantAddress == null ? null : applicantAddress.trim();
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency == null ? null : agency.trim();
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent == null ? null : agent.trim();
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum == null ? null : pageNum.trim();
    }

    public String getApplicantArea() {
        return applicantArea;
    }

    public void setApplicantArea(String applicantArea) {
        this.applicantArea = applicantArea == null ? null : applicantArea.trim();
    }

    public String getDisplayMainclassCode() {
        return displayMainclassCode;
    }

    public void setDisplayMainclassCode(String displayMainclassCode) {
        this.displayMainclassCode = displayMainclassCode == null ? null : displayMainclassCode.trim();
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf == null ? null : pdf.trim();
    }
}