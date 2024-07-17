import java.sql.*;
import java.util.Scanner;

class DatabaseConnection {
    private Connection connection;
    public Connection connect() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/student_db", "root", "password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class StudentManagement {
    public static void addStudent(String firstName, String lastName, int age, String grade) {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.connect();
        String query = "INSERT INTO student (first_name, last_name, age, grade) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setInt(3, age);
            stmt.setString(4, grade);
            stmt.executeUpdate();
            System.out.println("Étudiant ajouté avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public static void updateStudent(int id, String firstName, String lastName, int age, String grade) {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.connect();
        String query = "UPDATE student SET first_name = ?, last_name = ?, age = ?, grade = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setInt(3, age);
            stmt.setString(4, grade);
            stmt.setInt(5, id);
            stmt.executeUpdate();
            System.out.println("Étudiant modifié avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public static void deleteStudent(int id) {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.connect();
        String query = "DELETE FROM student WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Étudiant supprimé avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public static void displayAllStudents() {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.connect();
        String query = "SELECT * FROM student";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("ID | Nom | Prénom | Âge | Notes");
            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int age = rs.getInt("age");
                String grade = rs.getString("grade");
                System.out.println(id + " | " + lastName + " | " + firstName + " | " + age + " | " + grade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public static void searchStudent(int id) {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.connect();
        String query = "SELECT * FROM student WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int age = rs.getInt("age");
                String grade = rs.getString("grade");
                System.out.println("Nom : " + lastName + " | Prénom : " + firstName + " | Âge : " + age + " | Notes : " + grade);
            } else {
                System.out.println("Étudiant non trouvé.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Système de gestion d'étudiants");
            System.out.println("-----------------------------");
            System.out.println("1. Ajouter un étudiant");
            System.out.println("2. Modifier un étudiant");
            System.out.println("3. Supprimer un étudiant");
            System.out.println("4. Afficher tous les étudiants");
            System.out.println("5. Rechercher un étudiant");
            System.out.println("6. Quitter");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Nom : ");
                    String firstName = scanner.next();
                    System.out.print("Prénom : ");
                    String lastName = scanner.next();
                    System.out.print("Âge : ");
                    int age = scanner.nextInt();
                    System.out.print("Notes : ");
                    String grade = scanner.next();
                    addStudent(firstName, lastName, age, grade);
                    break;
                case 2:
                    System.out.print("ID de l'étudiant à modifier : ");
                    int idToUpdate = scanner.nextInt();
                    System.out.print("Nom : ");
                    String newFirstName = scanner.next();
                    System.out.print("Prénom : ");
                    String newLastName = scanner.next();
                    System.out.print("Âge : ");
                    int newAge = scanner.nextInt();
                    System.out.print("Notes : ");
                    String newGrade = scanner.next();
                    updateStudent(idToUpdate, newFirstName, newLastName, newAge, newGrade);
                    break;
                case 3:
                    System.out.print("ID de l'étudiant à supprimer : ");
                    int idToDelete = scanner.nextInt();
                    deleteStudent(idToDelete);
                    break;
                case 4:
                    displayAllStudents();
                    break;
                case 5:
                    System.out.print("ID de l'étudiant à rechercher : ");
                    int idToSearch = scanner.nextInt();
                    searchStudent(idToSearch);
                    break;
                case 6:
                    System.out.println("Au revoir !");
                    return;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }
}
