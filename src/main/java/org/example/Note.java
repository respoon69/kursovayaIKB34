package org.example;

/**
 * Класс, представляющий собой заметку.
 * Содержит идентификатор и текстовое содержимое заметки.
 */
public class Note {
    private int id;
    private String content;

    /**
     * Пустой конструктор, необходимый для десериализации.
     */
    public Note() {
    }

    /**
     * Создает заметку с указанными id и содержимым.
     *
     * @param id идентификатор заметки
     * @param content текст заметки
     */
    public Note(int id, String content) {
        this.id = id;
        this.content = content;
    }

    /**
     * Возвращает идентификатор заметки.
     *
     * @return идентификатор заметки
     */
    public int getId() {
        return id;
    }

    /**
     * Возвращает содержимое заметки.
     *
     * @return текст заметки
     */
    public String getContent() {
        return content;
    }

    /**
     * Устанавливает идентификатор заметки.
     *
     * @param id идентификатор для установки
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Устанавливает содержимое заметки.
     *
     * @param content новый текст заметки
     */
    public void setContent(String content) {
        this.content = content;
    }
}
