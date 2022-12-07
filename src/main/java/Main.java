import java.sql.*;

public class Main {
//    Скачать и установить СУБД MySQL. Создать таблицу с данными студентов группы.
//    Создать Java приложение для получения списка всех студентов группы.
//    Улучшить основное задание. Создать таблицу с городами, где живут студенты.
//    Вывести информацию о каждом студенте группы и его родном городе.
//    Предусмотреть операции добавления новых городов, новых студентов, удаления студентов и удаления городов.

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASS = "17081991";
    private static final String QUERY_DELETE_ALL_TABLES = "DROP TABLE students, cities";
    private static final String QUERY_CREATE_CITIES_TABLE = "CREATE TABLE cities (\n" +
            "      id SERIAL PRIMARY KEY,\n" +
            "      name VARCHAR(20)\n" +
            "    );";
    private static final String QUERY_CREATE_STUDENTS_TABLE = "CREATE TABLE students (\n" +
            "    id              SERIAL PRIMARY KEY,\n" +
            "    first_name      VARCHAR(20),\n" +
            "    last_name       VARCHAR(30),\n" +
            "    city            Integer references cities(id)\n" +
            "    );";
    private static final String QUERY_FILL_CITIES_TABLE = "INSERT INTO cities (name)\n" +
            "VALUES ('Minsk'),\n" +
            "       ('Brest'),\n" +
            "       ('Mogilev');";
    private static final String QUERY_FILL_STUDENTS_TABLE = "INSERT INTO students (first_name, last_name, city)\n" +
            "VALUES ('Petrov', 'Mihail', 1),\n" +
            "       ('Kuchini', 'Bobby', 2),\n" +
            "       ('Bezmozgov', 'David', 1),\n" +
            "       ('Ivanov', 'Sam', 3);";

    private static final String QUERY_GET_DATA_TABLES = "SELECT students.id, students.first_name, students.last_name, cities.name\n" +
            "FROM students\n" +
            "LEFT JOIN cities ON students.city = cities.id;";

    public static void executeUpdate(Connection connection, String query) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.executeUpdate();
    }

    public static void viewingTable(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(QUERY_GET_DATA_TABLES);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt(1);
            String firstName = resultSet.getString(2);
            String lastName = resultSet.getString(3);
            String city = resultSet.getString(4);
            System.out.println("Students: " + id + " - " + firstName + " " + lastName + "; " + city);
        }
    }

    public static void main(String[] argv) throws InterruptedException {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            executeUpdate(connection, QUERY_DELETE_ALL_TABLES);
            System.out.println("Tables deleted");
            Thread.sleep(2000);
            executeUpdate(connection, QUERY_CREATE_CITIES_TABLE.concat(QUERY_CREATE_STUDENTS_TABLE));
            System.out.println("Tables created");
            Thread.sleep(2000);
            executeUpdate(connection, QUERY_FILL_CITIES_TABLE);
            System.out.println("Cities Table filled");
            Thread.sleep(2000);
            executeUpdate(connection, QUERY_FILL_STUDENTS_TABLE);
            System.out.println("Students Table filled");
            Thread.sleep(2000);

            viewingTable(connection);
            System.out.println();
            executeUpdate(connection, "INSERT INTO students (first_name, last_name, city)\n" +
                    "VALUES ('Petrovsky', 'Serge', 1);");
            viewingTable(connection);
            System.out.println();
            executeUpdate(connection, "UPDATE students SET city=2 WHERE id=5;");
            viewingTable(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}