package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Репозиторий для хранения и доступа к заметкам.
 * Использует JSON-файл для хранения данных о заметках по датам.
 */
public class NotesRepository {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File file;

    private Map<String, DayNotes> data = new HashMap<>();

    /**
     * Создает репозиторий заметок, связывая его с указанным файлом.
     *
     * @param fileName имя файла, в котором будут храниться заметки
     */
    public NotesRepository(String fileName) {
        this.file = new File(fileName);
        load();
    }

    /**
     * Загружает данные из файла.
     * Если файл существует, читаются и десериализуются данные в {@code Map<String, DayNotes>}.
     * Если файл не найден, создаётся пустой набор данных.
     */
    private void load() {
        if (file.exists()) {
            try {
                data = objectMapper.readValue(file, new TypeReference<>() {});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Сохраняет текущие данные в JSON-файл.
     */
    public void save() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Возвращает все данные о заметках.
     *
     * @return карту, где ключ - дата в формате YYYY-MM-DD, значение - объект {@link DayNotes} с заметками
     */
    public Map<String, DayNotes> getAll() {
        return data;
    }

    /**
     * Возвращает заметки за определённый день.
     *
     * @param date дата, для которой нужно получить заметки
     * @return объект {@link DayNotes}, содержащий заметки за указанный день, или null, если ничего не найдено
     */
    public DayNotes getDayNotes(LocalDate date) {
        return data.get(date.toString());
    }

    /**
     * Добавляет или обновляет заметки для определённого дня.
     *
     * @param date дата, для которой устанавливаются заметки
     * @param dayNotes объект {@link DayNotes} с заметками
     */
    public void putDayNotes(LocalDate date, DayNotes dayNotes) {
        data.put(date.toString(), dayNotes);
    }

    /**
     * Удаляет заметки за определённый день.
     *
     * @param date дата, данные по которой должны быть удалены
     */
    public void removeDay(LocalDate date) {
        data.remove(date.toString());
    }
}
