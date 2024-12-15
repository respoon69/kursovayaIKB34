package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий набор заметок для конкретной даты,
 * а также информацию о празднике (если есть) и логике генерации ID для новых заметок.
 */
public class DayNotes {
    private String holiday;

    private List<Note> notes = new ArrayList<>();

    private int nextId = 1;

    /**
     * Возвращает название праздника, если он установлен для этой даты.
     *
     * @return название праздника или null, если праздник не установлен
     */
    public String getHoliday() {
        return holiday;
    }

    /**
     * Устанавливает название праздника для этой даты.
     *
     * @param holiday название праздника
     */
    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    /**
     * Возвращает список заметок, привязанных к этой дате.
     *
     * @return список заметок
     */
    public List<Note> getNotes() {
        return notes;
    }

    /**
     * Устанавливает список заметок для этой даты.
     *
     * @param notes список заметок
     */
    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    /**
     * Возвращает следующий доступный ID для новой заметки.
     *
     * @return следующий доступный идентификатор
     */
    public int getNextId() {
        return nextId;
    }

    /**
     * Увеличивает счетчик идентификаторов заметок на единицу
     * после добавления каждой новой заметки.
     */
    public void incrementNextId() {
        this.nextId++;
    }
}
