package com.meomeo.thachnnph50584_asm.Model;

public class Task {
    private int id;
    private String name;
    private String content;
    private int status; // 0: mới tạo, 1: đang làm, 2: hoàn thành, -1: cho vào thùng rác
    private String start;
    private String end;

    public Task(int id, String name, String content, int status, String start, String end) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.status = status;
        this.start = start;
        this.end = end;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getStart() {
        return start;
    }
    public void setStart(String start) {
        this.start = start;
    }
    public String getEnd() {
        return end;
    }
    public void setEnd(String end) {
        this.end = end;
    }
}