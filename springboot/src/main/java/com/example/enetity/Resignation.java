package com.example.enetity;


import java.time.LocalDate;

public class Resignation {
    private Integer id;
    private Integer employeeId;        // 员工ID
    private String resignationType; // 离职类型（主动离职/辞退/退休）
    private LocalDate applyDate;    // 申请日期
    private LocalDate lastWorkDate; // 最后工作日
    private String reason;          // 离职理由
    private String status;          // 状态（待审批/已批准/已驳回）
    private Long approverId;        // 审批人ID
    private String approvalComment; // 审批意见

    //    非数据库字段
    private String employeeName;
    private String departmentName;

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResignationType() {
        return resignationType;
    }

    public void setResignationType(String resignationType) {
        this.resignationType = resignationType;
    }

    public LocalDate getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(LocalDate applyDate) {
        this.applyDate = applyDate;
    }

    public LocalDate getLastWorkDate() {
        return lastWorkDate;
    }

    public void setLastWorkDate(LocalDate lastWorkDate) {
        this.lastWorkDate = lastWorkDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getApproverId() {
        return approverId;
    }

    public void setApproverId(Long approverId) {
        this.approverId = approverId;
    }

    public String getApprovalComment() {
        return approvalComment;
    }

    public void setApprovalComment(String approvalComment) {
        this.approvalComment = approvalComment;
    }
}
