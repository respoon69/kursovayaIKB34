package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Главный класс приложения.
 * Предоставляет интерфейс командной строки для работы с заметками: добавление, получение, удаление и очистка.
 * Также выводит информацию о праздниках на определённую дату.
 */

public class Main {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private static final Logger logger = LogManager.getLogger(Main.class);

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки (не используются)
     */

    public static void main(String[] args) {
        logger.info("Запуск приложения...");
        NotesRepository repository = new NotesRepository("notes.json");
        NotesService service = new NotesService(repository);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nВыберите действие:");
            System.out.println("1: Добавить заметку");
            System.out.println("2: Получить список заметок на дату");
            System.out.println("3: Удалить заметку по id");
            System.out.println("4: Очистить день");
            System.out.println("5: Выход");
            System.out.print("Введите цифру: ");

            String choice = scanner.nextLine().trim();
            logger.debug("Пользовательский ввод (действие): " + choice);

            if ("5".equals(choice)) {
                logger.info("Выбран пункт выхода из приложения.");
                break;
            }

            switch (choice) {
                case "1":
                    LocalDate dateAdd = readDate(scanner, "Введите дату (DD-MM-YYYY): ");
                    System.out.print("Введите текст заметки: ");
                    String content = scanner.nextLine();
                    logger.debug("Добавление заметки. Дата: " + dateAdd + ", Текст: " + content);
                    service.addNote(dateAdd, content);
                    System.out.println("Заметка добавлена.");
                    logger.info("Заметка добавлена для даты: " + dateAdd);
                    break;

                case "2":
                    LocalDate dateGet = readDate(scanner, "Введите дату (DD-MM-YYYY): ");
                    logger.debug("Запрошен список заметок для даты: " + dateGet);
                    String holiday = service.getHoliday(dateGet);
                    if (holiday != null) {
                        System.out.println("\nПраздник: " + holiday);
                        logger.info("Найден праздник на дату " + dateGet + ": " + holiday);
                    } else {
                        System.out.println("\nПраздник: отсутствует");
                        logger.info("Праздников на " + dateGet + " не найдено.");
                    }

                    List<Note> notes = service.getNotes(dateGet);
                    if (notes.isEmpty()) {
                        System.out.println("Заметок нет.");
                        logger.info("Для даты " + dateGet + " заметок не найдено.");
                    } else {
                        for (Note n : notes) {
                            System.out.println(n.getId() + ". " + n.getContent());
                        }
                        logger.info("Для даты " + dateGet + " получено заметок: " + notes.size());
                    }
                    break;

                case "3":
                    LocalDate dateRemove = readDate(scanner, "Введите дату (DD-MM-YYYY): ");
                    System.out.print("Введите id заметки для удаления: ");
                    int id;
                    try {
                        id = Integer.parseInt(scanner.nextLine().trim());
                        logger.debug("Запрошено удаление заметки. Дата: " + dateRemove + ", ID: " + id);
                    } catch (NumberFormatException e) {
                        System.out.println("Неверный формат ID. Пожалуйста, введите числовое значение.");
                        logger.warn("Попытка удалить заметку с некорректным ID.");
                        break;
                    }

                    List<Note> notesToRemove = service.getNotes(dateRemove);
                    boolean noteExists = false;
                    for (Note n : notesToRemove) {
                        if (n.getId() == id) {
                            noteExists = true;
                            break;
                        }
                    }

                    if (!noteExists) {
                        System.out.println("Заметка с таким ID не существует.");
                        logger.info("Попытка удалить несуществующую заметку с ID " + id + " для даты " + dateRemove);
                    } else {
                        service.removeNoteById(dateRemove, id);
                        System.out.println("\nЗаметка удалена.");
                        logger.info("Заметка с ID " + id + " для даты " + dateRemove + " удалена.");
                    }

                    break;

                case "4":
                    LocalDate dateClear = readDate(scanner, "Введите дату (DD-MM-YYYY): ");
                    logger.debug("Запрошена очистка дня: " + dateClear);
                    service.clearDay(dateClear);
                    System.out.println("\nДень очищен.");
                    logger.info("Все заметки для даты " + dateClear + " удалены.");
                    break;

                default:
                    System.out.println("Неверный выбор.");
                    logger.warn("Пользователь ввёл некорректный пункт меню: " + choice);
            }
        }

        scanner.close();
        logger.info("Завершение работы приложения.");
    }

    /**
     * Читает дату с консоли, используя заданный формат ("DD-MM-YYYY"),
     * и повторяет запрос, если формат неверный.
     *
     * @param scanner объект {@link Scanner} для чтения ввода
     * @param prompt подсказка пользователю для ввода даты
     * @return корректно считанная дата типа {@link LocalDate}
     */
    private static LocalDate readDate(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            logger.debug("Пользовательский ввод (дата): " + input);
            try {
                return LocalDate.parse(input, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Используйте правильный формат (DD-MM-YYYY)! ");
                logger.warn("Неверный формат даты: " + input);
            }
        }
    }
}
