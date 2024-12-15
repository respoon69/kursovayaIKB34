package org.example;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.stream.Collectors;
import java.util.*;

/**
 * Сервис для работы с заметками.
 * Предоставляет функциональность добавления, получения, удаления, очистки заметок по датам.
 * Также определяет, есть ли праздник на указанную дату.
 */

public class NotesService {
    private final NotesRepository repository;
    private final Map<MonthDay, String> holidays;

    /**
     * Создает экземпляр сервиса заметок.
     *
     * @param repository репозиторий для работы с данными заметок
     */
    public NotesService(NotesRepository repository) {
        this.repository = repository;
        this.holidays = initHolidays();
    }

    /**
     * Инициализирует карту праздников.
     *
     * @return карта, где ключ - день года ({@link MonthDay}), значение - название праздника
     */
    private Map<MonthDay, String> initHolidays() {
        Map<MonthDay, String> map = new HashMap<>();
        map.put(MonthDay.of(1, 1), "Новый год");
        map.put(MonthDay.of(1, 7), "Рождество Христово");
        map.put(MonthDay.of(1, 11), "День заповедников и национальных парков России");
        map.put(MonthDay.of(1, 13), "День Российской печати");
        map.put(MonthDay.of(1, 14), "Старый Новый год");
        map.put(MonthDay.of(1, 20), "День Республики Крым");
        map.put(MonthDay.of(1, 27), "День полного освобождения Ленинграда");
        map.put(MonthDay.of(2, 9), "День гражданской авиации в России");
        map.put(MonthDay.of(2, 14), "День Святого Валентина");
        map.put(MonthDay.of(2, 23), "День защитника Отечества");
        map.put(MonthDay.of(3, 8), "Международный женский день");
        map.put(MonthDay.of(3, 18), "День воссоединения Крыма с Россией");
        map.put(MonthDay.of(4, 1), "День смеха");
        map.put(MonthDay.of(4, 12), "День космонавтики(День рождения автора курсовой работы)");
        map.put(MonthDay.of(4, 20), "Национальный день донора");
        map.put(MonthDay.of(5, 1), "Праздник Весны и Труда");
        map.put(MonthDay.of(5, 9), "День Победы");
        map.put(MonthDay.of(5, 15), "Международный день семей");
        map.put(MonthDay.of(6, 1), "Международный день зашиты детей");
        map.put(MonthDay.of(6, 6), "День русского языка");
        map.put(MonthDay.of(6, 12), "День России");
        map.put(MonthDay.of(6, 27), "День молодежи");
        map.put(MonthDay.of(7, 1), "День ветеранов боевых действий");
        map.put(MonthDay.of(7, 7), "День Ивана Купала");
        map.put(MonthDay.of(7, 8), "День любви, семьи и верности");
        map.put(MonthDay.of(8, 22), "День Государственного флага Российской Федерации");
        map.put(MonthDay.of(9, 1), "День знаний");
        map.put(MonthDay.of(10, 5), "День учителя");
        map.put(MonthDay.of(10, 28), "День бабушек и дедушек");
        map.put(MonthDay.of(11, 4), "День народного единства");
        map.put(MonthDay.of(11, 25), "День матери");
        map.put(MonthDay.of(12, 3), "День неизвестного солдата");
        map.put(MonthDay.of(12, 9), "День Героев Отечества");
        map.put(MonthDay.of(12, 12), "День Конституции Российской Федерации");
        map.put(MonthDay.of(12, 31), "Последний день в году");
        return map;
    }

    /**
     * Добавляет заметку к определенной дате.
     *
     * @param date дата, к которой будет добавлена заметка
     * @param content текст заметки
     */
    public void addNote(LocalDate date, String content) {
        DayNotes dayNotes = repository.getDayNotes(date);
        if (dayNotes == null) {
            dayNotes = new DayNotes();
        }

        String holiday = getHolidayIfOccurred(date);
        if (holiday != null) {
            dayNotes.setHoliday(holiday);
        }

        int newId = dayNotes.getNextId();
        Note newNote = new Note(newId, content);
        dayNotes.getNotes().add(newNote);
        dayNotes.incrementNextId();
        repository.putDayNotes(date, dayNotes);
        repository.save();
    }

    /**
     * Возвращает список заметок для заданной даты.
     *
     * @param date дата, для которой требуется получить заметки
     * @return список заметок на указанную дату (может быть пустым)
     */
    public List<Note> getNotes(LocalDate date) {
        DayNotes dayNotes = repository.getDayNotes(date);
        if (dayNotes == null) {
            dayNotes = new DayNotes();
            String holiday = getHolidayIfOccurred(date);
            if (holiday != null) {
                dayNotes.setHoliday(holiday);
                repository.putDayNotes(date, dayNotes);
                repository.save();
            }
            return Collections.emptyList();
        } else {
            String holiday = getHolidayIfOccurred(date);
            if (holiday != null) {
                dayNotes.setHoliday(holiday);
            } else {
                dayNotes.setHoliday(null);
            }
            repository.putDayNotes(date, dayNotes);
            repository.save();
            return dayNotes.getNotes();
        }
    }

    /**
     * Вспомогательный метод для получения праздника (если есть) для данной даты.
     *
     * @param date дата для проверки на праздник
     * @return название праздника или null, если праздника нет
     */
    private String getHolidayIfOccurred(LocalDate date) {
        MonthDay monthDay = MonthDay.from(date);
        return holidays.get(monthDay);
    }

    /**
     * Возвращает название праздника для заданной даты, если он есть.
     *
     * @param date дата, для которой нужно получить праздник
     * @return название праздника или null, если праздник отсутствует
     */
    public String getHoliday(LocalDate date) {
        return getHolidayIfOccurred(date);
    }

    /**
     * Удаляет заметку по её ID для заданной даты.
     *
     * @param date дата, в рамках которой выполняется удаление заметки
     * @param id идентификатор заметки для удаления
     */
    public void removeNoteById(LocalDate date, int id) {
        DayNotes dayNotes = repository.getDayNotes(date);
        if (dayNotes == null) {
            return;
        }
        List<Note> filtered = dayNotes.getNotes().stream()
                .filter(note -> note.getId() != id)
                .collect(Collectors.toList());
        dayNotes.setNotes(filtered);
        repository.putDayNotes(date, dayNotes);
        repository.save();
    }

    /**
     * Очищает все заметки за указанный день.
     *
     * @param date дата, которая будет очищена
     */
    public void clearDay(LocalDate date) {
        repository.removeDay(date);
        repository.save();
    }
}
