package com.example.fruit;

public class DataItem implements Comparable<DataItem> {
    private int number;
    private String fileName;
    private String dateTime; // 이미지의 업로드 날짜
    private String time; // 이미지의 업로드 시간

    public DataItem(int index, String fileName, String dateTime) {
        // 번호를 리스트의 인덱스에 따라서 1부터 시작하도록 설정
        this.number = index + 1;
        this.fileName = fileName;

        // dateTime 문자열을 파싱하여 dateTime과 time으로 분리
        String[] parts = dateTime.split("_");
        if (parts.length >= 2) {
            this.dateTime = parts[0];
            this.time = parts[1];
        } else {
            this.dateTime = dateTime;
            this.time = "";
        }
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDate() {
        return dateTime;
    }

    public String getTime() {
        return time;
    }

    @Override
    public int compareTo(DataItem other) {
        // 현재 객체(this)와 다른 객체(other)의 비교
        // 날짜 및 시간을 비교하여 정렬
        int dateComparison = this.dateTime.compareTo(other.dateTime);
        if (dateComparison == 0) {
            // 날짜가 동일한 경우 시간으로 비교
            return this.time.compareTo(other.time);
        } else {
            return dateComparison;
        }
    }
}