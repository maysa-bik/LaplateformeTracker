import java.sql.*;
import java.util.Scanner;

public class StudentManagementSystem {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/studentdb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    private static Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void addStudent(Connection conn) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nom : ");
        String lastName = scanner.nextLine();
        System.out.print("Prénom : ");
        String firstName = scanner.nextLine();
        System.out.print("Âge : ");
        int age = scanner.nextInt();
        System.out.print("Notes (séparées par des virgules) : ");
        scanner.nextLine();
        String grade = scanner.nextLine();

        String sql = "INSERT INTO student (first_name, last_name, age, grade) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setInt(3, age);
            pstmt.setString(4, grade);
            pstmt.executeUpdate();
            System.out.println("Étudiant ajouté avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateStudent(Connection conn) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("ID de l'étudiant à modifier : ");
        int id = scanner.nextInt();
        System.out.print("Nouveau Nom : ");
        scanner.nextLine();
        String lastName = scanner.nextLine();
        System.out.print("Nouveau Prénom : ");
        String firstName = scanner.nextLine();
        System.out.print("Nouvel Âge : ");
        int age = scanner.nextInt();
        System.out.print("Nouvelles Notes (séparées par des virgules) : ");
        scanner.nextLine();
        String grade = scanner.nextLine();

        String sql = "UPDATE student SET first_name = ?, last_name = ?, age = ?, grade = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setInt(3, age);
            pstmt.setString(4, grade);
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
            System.out.println("Étudiant modifié avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteStudent(Connection conn) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("ID de l'étudiant à supprimer : ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM student WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Étudiant supprimé avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void showAllStudents(Connection conn) {
        String sql = "SELECT * FROM student";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Liste des étudiants :");
            System.out.println("ID | Nom | Prénom | Âge | Notes");
            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int age = rs.getInt("age");
                String grade = rs.getString("grade");
                System.out.printf("%d | %s | %s | %d | %s%n", id, lastName, firstName, age, grade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void findStudentById(Connection conn) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("ID de l'étudiant à rechercher : ");
        int id = scanner.nextInt();

        String sql = "SELECT * FROM student WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int age = rs.getInt("age");
                String grade = rs.getString("grade");
                System.out.printf("Nom : %s | Prénom : %s | Âge : %d | Notes : %s%n", lastName, firstName, age, grade);
            } else {
                System.out.println("Étudiant non trouvé !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Connection conn = connect();
        if (conn != null) {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\nSystème de gestion d'étudiants");
                System.out.println("1. Ajouter un étudiant");
                System.out.println("2. Modifier un étudiant");
                System.out.println("3. Supprimer un étudiant");
                System.out.println("4. Afficher tous les étudiants");
                System.out.println("5. Rechercher un étudiant par ID");
                System.out.println("6. Quitter");
                System.out.print("Choisissez une option : ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        addStudent(conn);
                        break;
                    case 2:
                        updateStudent(conn);
                        break;
                    case 3:
                        deleteStudent(conn);
                        break;
                    case 4:
                        showAllStudents(conn);
                        break;
                    case 5:
                        findStudentById(conn);
                        break;
                    case 6:
                        try {
                            conn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Au revoir !");
                        return;
                    default:
                        System.out.println("Option invalide. Veuillez réessayer.");
                }
            }
        }
    }
}
