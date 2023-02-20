package com.lixer.womensafetyapp;

public class create_new_report_model {
    String report_title;
    String report_data;

    public create_new_report_model(String report_title, String report_data) {
        this.report_title = report_title;
        this.report_data = report_data;
    }

    public String getReport_title() {
        return report_title;
    }

    public void setReport_title(String report_title) {
        this.report_title = report_title;
    }

    public String getReport_data() {
        return report_data;
    }

    public void setReport_data(String report_data) {
        this.report_data = report_data;
    }
}
