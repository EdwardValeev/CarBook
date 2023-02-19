package ru.edvaleev.CarBook.dto;

import java.time.LocalDateTime;

public class DataBaseDTO {

    private long count;

    private LocalDateTime firstEntryDate;

    private LocalDateTime lastEntryDate;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public LocalDateTime getFirstEntryDate() {
        return firstEntryDate;
    }

    public void setFirstEntryDate(LocalDateTime firstEntryDate) {
        this.firstEntryDate = firstEntryDate;
    }

    public LocalDateTime getLastEntryDate() {
        return lastEntryDate;
    }

    public void setLastEntryDate(LocalDateTime lastEntryDate) {
        this.lastEntryDate = lastEntryDate;
    }
}
