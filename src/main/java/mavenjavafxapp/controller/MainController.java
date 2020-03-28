package mavenjavafxapp.controller;

import hibernate.HibernateUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.Groups;
import models.Lecturers;
import models.Students;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by HP on 18.03.2017.
 */
public class MainController {
    @FXML
    private TextField loginField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField enameFile;
    @FXML
    private Button addStudentToGroup;
    @FXML
    private Label getStatusCreateFile;
    @FXML
    private Label label;
    @FXML
    private RadioButton createFileRadio;
    @FXML
    private RadioButton createDirectoryRadio;
    @FXML
    private Label statusCreateFile;
    @FXML
    private TextField createFileField;
    static public Students students = new Students();
    private TextField pathTextField = new TextField();//init textField
    private boolean FILESTATUS;
    private TableView<DirectoryItem> table = new TableView<DirectoryItem>();//initialization var for visible var "tableData"
    private final ObservableList<DirectoryItem> tableData = FXCollections.observableArrayList();//initialization var for save data
    private static final String english = "abcdefghijklmnopqrstuvwxyz";//
    private static final String numbers = "0123456789";
    private static final int n = 2 * english.length() + numbers.length();//

    private static String KeyGen() {
        SecureRandom secureRandom = null;
        try {
            secureRandom = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        char[] symbols = new char[n];
        int i = 0;
        for (char letter : english.toCharArray()) {
            symbols[i] = letter;
            i++;
        }
        for (char letter : english.toUpperCase().toCharArray()) {
            symbols[i] = letter;
            i++;
        }
        for (char number : numbers.toCharArray()) {
            symbols[i] = number;
            i++;
        }
        System.out.print("All symbols: ");
        System.out.println(symbols);
        String key = "";
        for (int j = 0; j < 16; j++) {
            char a = symbols[secureRandom.nextInt(n)];
            key += a;//
        }
        return key;
    }

    private void CopyFile(int numbers, String filePath, String fileExtension) {
        File file = new File(filePath);
        for (int i = 0; i < numbers; i++) {
            File target = new File(filePath + " " + i + fileExtension);
            try {
                Files.copy(file.toPath(), target.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void encrypt(Key secretKey, InputStream is, OutputStream os, Cipher cipher)
            throws Throwable {
        encryptOrDecrypt(secretKey, Cipher.ENCRYPT_MODE, is, os, cipher);
    }

    public static void decrypt(Key key, InputStream is, OutputStream os, Cipher cipher)
            throws Throwable {
        encryptOrDecrypt(key, Cipher.DECRYPT_MODE, is, os, cipher);
    }

    public static void encryptOrDecrypt(Key key, int mode, InputStream is,
                                        OutputStream os, Cipher cipher) throws Throwable {
        if (mode == Cipher.ENCRYPT_MODE) {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            CipherInputStream cis = new CipherInputStream(is, cipher);
            doCopy(cis, os);
        } else if (mode == Cipher.DECRYPT_MODE) {
            cipher.init(Cipher.DECRYPT_MODE, key);
            CipherOutputStream cos = new CipherOutputStream(os, cipher);
            doCopy(is, cos);
        }
    }

    public static void doCopy(InputStream is, OutputStream os)
            throws IOException {
        byte[] bytes = new byte[64];
        int numBytes;
        while ((numBytes = is.read(bytes)) != -1) {
            os.write(bytes, 0, numBytes);
        }
        os.flush();
        os.close();
        is.close();
    }

    @FXML
    public void Registration(ActionEvent actionEvent) throws IOException {
        try {
            Stage stage = new Stage();
            String fxmlFile = "/fxml/registration.fxml";
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResourceAsStream(fxmlFile));
            stage.setTitle("Registration");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);//відкриє вікно
            stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());//робить вікно модальним
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void Create() throws IOException {
        File file = new File(pathTextField.getText().toString() + '\\' + createFileField.getText().toString());
        if (createFileField.getText().equals("")) {

            statusCreateFile.setStyle("-fx-text-fill: blue");
            statusCreateFile.setText("Input please fill");

        } else {
            if (createFileRadio.isSelected()) {
                if (!file.exists() || file.isDirectory()) {
                    file.createNewFile();
                    statusCreateFile.setStyle("-fx-text-fill: green");
                    statusCreateFile.setText("File successfully created");
                    initTableData();
                    System.out.println(createFileField.getText());
                } else {
                    statusCreateFile.setStyle("-fx-text-fill: red");
                    statusCreateFile.setText("File exists");
                }
            }
            if (createDirectoryRadio.isSelected()) {
                if (!file.exists() || file.isFile()) {
                    file.mkdir();
                    statusCreateFile.setStyle("-fx-text-fill: green");
                    statusCreateFile.setText("Directory successfully created");
                    initTableData();
                } else {
                    statusCreateFile.setStyle("-fx-text-fill: red");
                    statusCreateFile.setText("Directory exists");
                }
            }

        }
    }

    /*@FXML
    public void EncryptFileForGroup(ActionEvent actionEvent) {
        File file = new File(pathTextField.getText().toString() + '\\' + createFileField.getText().toString());
    }*/


    @FXML
    public void SignIn() {
        //List <Students>;
        //List <Lecturers>;
        SignInStudents();
        SignInLecturers();

    }

    private static String getFileExtension(String mystr) {
        int index = mystr.indexOf('.');
        return index == -1 ? null : mystr.substring(index);
    }


    public void SignInLecturers() {
        List<Lecturers> lecturers = null;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        if (loginField.getText().equals("") || passwordField.getText().equals("")) {
            System.out.println("sdf");
            label.setStyle("-fx-text-fill: red");
            label.setText("Please fill in all fields");
        } else {
            try {
                session.beginTransaction();
                Query query = session.createQuery("FROM Lecturers ");
                lecturers = query.list();
                session.getTransaction().commit();
                session.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (Lecturers gg : lecturers) {
                System.out.println(gg.toString());
                System.out.println("dd");
                if (gg.getLogin().equals(loginField.getText()) && gg.getPassword().equals(DigestUtils.md5Hex(passwordField.getText()))) {
                    label.setStyle("-fx-text-fill: green");
                    label.setText("Congratulations!!! You sign in!!!");
                    MainController.students.setLogin(gg.getLogin());
                    MainController.students.setId(gg.getId());
                    loginField.setText("");
                    passwordField.setText("");

                    Stage stage = new Stage();
                    HBox hbox = new HBox();//впорядковує елементи по горизонталі
                    for (File root : File.listRoots()) {//отримуємо всі списки жостких і з'ємних дисків
                        String rootPath = root.toString();
                        Button btn = new Button(rootPath);
                        btn.setOnAction(
                                new EventHandler<ActionEvent>() {
                                    public void handle(ActionEvent event) {
                                        tableData.clear();
                                        pathTextField.setText(rootPath);
                                        initTableData();//функція відображення вмісту каталогів
                                    }
                                }
                        );
                        hbox.getChildren().add(btn);//додать до панельки дані кнопки
                    }
                    Button settings = new Button("Settings");
                    settings.setAlignment(Pos.BASELINE_RIGHT);
                    hbox.getChildren().add(settings);

                    settings.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            Stage stage = new Stage();
                            String fxmlFile = "/fxml/modifyUserSettings.fxml";
                            FXMLLoader loader = new FXMLLoader();
                            Parent root = null;
                            try {
                                root = loader.load(getClass().getResourceAsStream(fxmlFile));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            stage.setTitle("Modify settings " + MainController.students.getLogin());
                            stage.setScene(new Scene(root));
                            stage.initModality(Modality.WINDOW_MODAL);//відкриє вікно
                            stage.initOwner(((Node) event.getSource()).getScene().getWindow());//робить вікно модальним
                            stage.show();
                        }
                    });

                    pathTextField.setText(File.listRoots()[0].toString());
                    initTableData();//функція відображення вмісту каталогів
                    TableColumn nameCol = new TableColumn("Name");//задаєм топ таблиці зі значенням
                    nameCol.setCellValueFactory(new PropertyValueFactory<DirectoryItem, String>("name"));
                    nameCol.setMinWidth(200);

                    TableColumn sizeCol = new TableColumn("Size");//задаєм топ таблиці зі значенням
                    sizeCol.setCellValueFactory(new PropertyValueFactory<DirectoryItem, Long>("size"));
                    sizeCol.setMinWidth(100);

                    table.setItems(tableData);//fill table tableData (data about files and directory)
                    table.getColumns().addAll(nameCol, sizeCol);//без цього не будуть відображуватись дані в таблиці для показу даних
                    HBox actionOnFiles = new HBox(10);
                    Button rename = new Button("Rename");
                    Button deleteButton = new Button("Delete");
                    Button createFile = new Button("Create file");
                    Button fileEncryptionButton = new Button("Encrypt file");
                    Button interactWithGroupButton = new Button("Interact with group");
                    Button find = new Button("Find");

                    actionOnFiles.getChildren().addAll(rename, deleteButton, createFile, fileEncryptionButton, interactWithGroupButton, find);

                    createFile.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            Stage stage = new Stage();
                            String fxmlFile = "/fxml/createFileAndDirectory.fxml";
                            FXMLLoader loader = new FXMLLoader();
                            Parent root = null;
                            try {
                                root = loader.load(getClass().getResourceAsStream(fxmlFile));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            stage.setTitle("Created files for " + MainController.students.getLogin());
                            stage.setScene(new Scene(root));
                            stage.initModality(Modality.WINDOW_MODAL);//відкриє вікно
                            stage.initOwner(((Node) event.getSource()).getScene().getWindow());//робить вікно модальним
                            stage.show();
                        }
                    });
                    //

                    interactWithGroupButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            Stage stage = new Stage();
                            String fxmlFile = "/fxml/student-group.fxml";
                            FXMLLoader loader = new FXMLLoader();
                            Parent root = null;
                            try {
                                root = loader.load(getClass().getResourceAsStream(fxmlFile));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            stage.setTitle("STUDENTS-GROUPS");
                            stage.setScene(new Scene(root));
                            stage.initModality(Modality.WINDOW_MODAL);//відкриє вікно
                            stage.initOwner(((Node) event.getSource()).getScene().getWindow());//робить вікно модальним
                            stage.show();
                        }
                    });
                    rename.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            Stage stage = new Stage();
                            String fxmlFile = "/fxml/renameFile.fxml";
                            FXMLLoader loader = new FXMLLoader();
                            Parent root = null;
                            try {
                                root = loader.load(getClass().getResourceAsStream(fxmlFile));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            stage.setTitle("Created files for " + MainController.students.getLogin());
                            stage.setScene(new Scene(root));
                            stage.initModality(Modality.WINDOW_MODAL);//відкриє вікно
                            stage.initOwner(((Node) event.getSource()).getScene().getWindow());//робить вікно модальним
                            stage.show();
                        }
                    });

                    table.setRowFactory((TableView<DirectoryItem> tv) -> {//обробник подій повязаних з вибором даних мишкою
                        TableRow<DirectoryItem> row = new TableRow<>();
                        row.setOnMouseClicked((MouseEvent event) -> {
                            DirectoryItem rowData = row.getItem();
                            if (event.getClickCount() == 2 && (!row.isEmpty())) {
                                if (rowData.getSize() == "folder") {
                                    String tempStr = rowData.getName();
                                    if (tempStr == "..") {
                                        tempStr = pathTextField.getText();
                                        int lastSlash = tempStr.lastIndexOf("\\");
                                        pathTextField.setText(tempStr.substring(0, lastSlash));
                                    } else
                                        pathTextField.setText(new StringBuilder(pathTextField.getText()).append("\\").append(tempStr).toString());
                                    initTableData();
                                } else {

                                    File file = new File(pathTextField.getText().toString() + '\\' + rowData.getName().toString());

                                    Desktop desktop = null;
                                    if (Desktop.isDesktopSupported()) {
                                        desktop = Desktop.getDesktop();
                                    }
                                    try {
                                        desktop.open(file);
                                    } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                            }
                            fileEncryptionButton.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    String fileExtension = getFileExtension(rowData.getName().toString());
                                    String filePath = pathTextField.getText().toString() + '\\' + rowData.getName().toString();

                                    List<Students> student = null;
                                    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
                                    Session session = sessionFactory.openSession();
                                    try {//початок транзакції
                                        Criteria criteria = session.createCriteria(Students.class);
                                        student = criteria.list();
                                        session.close();
                                    } catch (Exception e) {
                                        session.getTransaction().rollback();
                                        e.printStackTrace();
                                    }

                                    List<String> list = new ArrayList<String>();
                                    int j = 0;
                                    for (Students ss : student) {
                                        if (ss.getKek() == null) {
                                            session = sessionFactory.openSession();
                                            try {
                                                session.beginTransaction();
                                                list.add(KeyGen());
                                                //ss.setKek(list.get(j));
                                                Students studentsToUpdate = (Students) session.get(Students.class, ss.getId());
                                                studentsToUpdate.setKek(list.get(j));
                                                session.persist(studentsToUpdate);
                                                session.getTransaction().commit();
                                                session.close();
                                            } catch (Exception e) {
                                                session.getTransaction().rollback();
                                                e.printStackTrace();
                                            }
                                        } else {
                                            list.add(ss.getKek());
                                            System.out.println("Random key: " + list.get(j));
                                        }
                                        j++;
                                    }
                                    MainController copy = new MainController();
                                    copy.CopyFile(student.size(), filePath, fileExtension);

                                    for (int i = 0; i < student.size(); i++) {
                                        String pathEncrypted = filePath + " " + i + fileExtension;
                                        String pathDecrypted = filePath + "encrypted " + i + fileExtension;
                                        try {

                                            KeyGenerator keyGenerator = KeyGenerator.getInstance("Blowfish");
                                            keyGenerator.init(256);
                                            Key secretKey = new SecretKeySpec(list.get(i).getBytes(), "Blowfish");
                                            Cipher cipher = Cipher.getInstance("Blowfish");
                                            File fileEncrypted = new File(pathEncrypted);
                                            File fileDecrypted = new File(pathDecrypted);

                                            FileInputStream fis = new FileInputStream(pathEncrypted);
                                            FileOutputStream fos = new FileOutputStream(pathDecrypted);
                                            encrypt(secretKey, fis, fos, cipher);

                                            fileEncrypted.delete();
                                            if (!fileEncrypted.exists()) { // если файл существует, то переименовываем его
                                                fileDecrypted.renameTo(new java.io.File(pathEncrypted));
                                            }

                                            System.out.println("You encrypted file.");
                                        } catch (Throwable e) {
                                            e.printStackTrace();
                                        }
                                        initTableData();
                                    }
                                }
                            });
                            deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    File file = new File(pathTextField.getText().toString() + '\\' + rowData.getName().toString());
                                    if (file.delete()) {
                                        System.out.println(pathTextField.getText().toString() + '\\' + rowData.getName().toString() + " файл удален");
                                        initTableData();
                                    } else System.out.println("Файла /Users/prologistic/file.txt не обнаружено");
                                }
                            });
                        });
                        return row;
                    });


                    VBox vbox = new VBox(10, hbox, pathTextField, table, actionOnFiles);//табличні дані будуть відображатись у вертикальному вигляді
                    vbox.setPadding(new Insets(10, 20, 10, 20));

                    Scene scene = new Scene(vbox);
                    scene.getStylesheets().add("JavaFXApp.css");//на сцену прибиваєм фон
                    stage.setTitle("Hello " + MainController.students.getLogin());
                    stage.setScene(scene);
                    stage.show();
                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            Platform.exit();
                        }
                    });
                } else {
                    label.setStyle("-fx-text-fill: red");
                    label.setText("Invalid authentication");
                }
            }

        }
    }

        /*SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM Students");
        List<Students> students = query.list();
        session.getTransaction().commit();
        session.close();


        String label = studentsListView.getStyle();
        System.out.println(label);
       /*for (Students gg : students) {
            if (gg.getLogin().equals()) {
                System.out.println(gg.getLogin());
                break;
            } else {
                System.out.println("sasay lalka");
            }
        }*/


    public void SignInStudents() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        List<Students> students;
        if (loginField.getText().equals("") || passwordField.getText().equals("")) {
            System.out.println("sdf");
            label.setStyle("-fx-text-fill: red");
            label.setText("Please fill in all fields");
        } else {
            try {
                session.beginTransaction();
                Query query = session.createQuery("FROM Students");
                students = query.list();
                session.getTransaction().commit();

                for (Students gg : students) {
                    System.out.println(gg.toString());
                    if (gg.getLogin().equals(loginField.getText()) && gg.getPassword().equals(DigestUtils.md5Hex(passwordField.getText()))) {
                        label.setStyle("-fx-text-fill: green");
                        label.setText("Congratulations!!! You sign in!!!");
                        MainController.students.setLogin(gg.getLogin());
                        MainController.students.setId(gg.getId());
                        loginField.setText("");
                        passwordField.setText("");

                        Stage stage = new Stage();
                        HBox hbox = new HBox();//впорядковує елементи по горизонталі
                        for (File root : File.listRoots()) {//отримуємо всі списки жостких і з'ємних дисків
                            String rootPath = root.toString();
                            Button btn = new Button(rootPath);
                            btn.setOnAction(
                                    new EventHandler<ActionEvent>() {
                                        public void handle(ActionEvent event) {
                                            tableData.clear();
                                            pathTextField.setText(rootPath);
                                            initTableData();//функція відображення вмісту каталогів
                                        }
                                    }
                            );
                            hbox.getChildren().add(btn);//додать до панельки дані кнопки
                        }
                        Button settings = new Button("Settings");
                        settings.setAlignment(Pos.BASELINE_RIGHT);
                        hbox.getChildren().add(settings);

                        settings.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                Stage stage = new Stage();
                                String fxmlFile = "/fxml/modifyUserSettings.fxml";
                                FXMLLoader loader = new FXMLLoader();
                                Parent root = null;
                                try {
                                    root = loader.load(getClass().getResourceAsStream(fxmlFile));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                stage.setTitle("Modify settings " + MainController.students.getLogin());
                                stage.setScene(new Scene(root));
                                stage.initModality(Modality.WINDOW_MODAL);//відкриє вікно
                                stage.initOwner(((Node) event.getSource()).getScene().getWindow());//робить вікно модальним
                                stage.show();
                            }
                        });

                        pathTextField.setText(File.listRoots()[0].toString());
                        initTableData();//функція відображення вмісту каталогів
                        TableColumn nameCol = new TableColumn("Name");//задаєм топ таблиці зі значенням
                        nameCol.setCellValueFactory(new PropertyValueFactory<DirectoryItem, String>("name"));
                        nameCol.setMinWidth(200);

                        TableColumn sizeCol = new TableColumn("Size");//задаєм топ таблиці зі значенням
                        sizeCol.setCellValueFactory(new PropertyValueFactory<DirectoryItem, Long>("size"));
                        sizeCol.setMinWidth(100);

                        table.setItems(tableData);//fill table tableData (data about files and directory)
                        table.getColumns().addAll(nameCol, sizeCol);//без цього не будуть відображуватись дані в таблиці для показу даних
                        HBox actionOnFiles = new HBox(10);
                        Button rename = new Button("Rename");
                        Button deleteButton = new Button("Delete");
                        Button createFile = new Button("Create file");
                        Button fileDecryptionButton = new Button("Decrypt file");
                        Button find = new Button("Find");

                        actionOnFiles.getChildren().addAll(rename, deleteButton, createFile, fileDecryptionButton, find);

                        createFile.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                Stage stage = new Stage();
                                String fxmlFile = "/fxml/createFileAndDirectory.fxml";
                                FXMLLoader loader = new FXMLLoader();
                                Parent root = null;
                                try {
                                    root = loader.load(getClass().getResourceAsStream(fxmlFile));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                stage.setTitle("Created files for " + MainController.students.getLogin());
                                stage.setScene(new Scene(root));
                                stage.initModality(Modality.WINDOW_MODAL);//відкриє вікно
                                stage.initOwner(((Node) event.getSource()).getScene().getWindow());//робить вікно модальним
                                stage.show();
                            }
                        });

                        rename.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                Stage stage = new Stage();
                                String fxmlFile = "/fxml/renameFile.fxml";
                                FXMLLoader loader = new FXMLLoader();
                                Parent root = null;
                                try {
                                    root = loader.load(getClass().getResourceAsStream(fxmlFile));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                stage.setTitle("Created files for " + MainController.students.getLogin());
                                stage.setScene(new Scene(root));
                                stage.initModality(Modality.WINDOW_MODAL);//відкриє вікно
                                stage.initOwner(((Node) event.getSource()).getScene().getWindow());//робить вікно модальним
                                stage.show();
                            }
                        });

                        table.setRowFactory((TableView<DirectoryItem> tv) -> {//обробник подій повязаних з вибором даних мишкою
                            TableRow<DirectoryItem> row = new TableRow<>();
                            row.setOnMouseClicked((MouseEvent event) -> {
                                DirectoryItem rowData = row.getItem();
                                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                                    if (rowData.getSize() == "folder") {
                                        String tempStr = rowData.getName();
                                        if (tempStr == "..") {
                                            tempStr = pathTextField.getText();
                                            int lastSlash = tempStr.lastIndexOf("\\");
                                            pathTextField.setText(tempStr.substring(0, lastSlash));
                                        } else
                                            pathTextField.setText(new StringBuilder(pathTextField.getText()).append("\\").append(tempStr).toString());
                                        initTableData();
                                    } else {

                                        File file = new File(pathTextField.getText().toString() + '\\' + rowData.getName().toString());

                                        Desktop desktop = null;
                                        if (Desktop.isDesktopSupported()) {
                                            desktop = Desktop.getDesktop();
                                        }
                                        try {
                                            desktop.open(file);
                                        } catch (IOException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                fileDecryptionButton.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        String fileExtension = getFileExtension(rowData.getName().toString());
                                        //String filePath = pathTextField.getText().toString() + '\\' + rowData.getName().toString();


                                        String key = gg.getKek();

                                        String pathEncrypted = pathTextField.getText().toString() + '\\' + rowData.getName().toString();
                                        String pathDecrypted = pathEncrypted + "encrypted " + fileExtension;
                                        try {
                                            KeyGenerator keyGenerator = KeyGenerator.getInstance("Blowfish");
                                            keyGenerator.init(256);
                                            Key secretKey = new SecretKeySpec(key.getBytes(), "Blowfish");
                                            Cipher cipher = Cipher.getInstance("Blowfish");

                                            File fileEncrypted = new File(pathEncrypted);
                                            File fileDecrypted = new File(pathDecrypted);

                                            FileInputStream fis2 = new FileInputStream(pathEncrypted);
                                            FileOutputStream fos2 = new FileOutputStream(pathDecrypted);
                                            decrypt(secretKey, fis2, fos2, cipher);
                                            fileEncrypted.delete();

                                            if (!fileEncrypted.exists()) { // если файл существует, то переименовываем его
                                                fileDecrypted.renameTo(new java.io.File(pathEncrypted));
                                            } else {
                                                System.out.println("UUUUUU!!!");
                                            }
                                            System.out.println("You decrypted file.");

                                        } catch (Throwable e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                                deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        File file = new File(pathTextField.getText().toString() + '\\' + rowData.getName().toString());
                                        if (file.delete()) {
                                            System.out.println(pathTextField.getText().toString() + '\\' + rowData.getName().toString() + " файл удален");
                                            initTableData();
                                        } else System.out.println("Файла /Users/prologistic/file.txt не обнаружено");
                                    }
                                });
                            });
                            return row;
                        });


                        VBox vbox = new VBox(10, hbox, pathTextField, table, actionOnFiles);//табличні дані будуть відображатись у вертикальному вигляді
                        vbox.setPadding(new Insets(10, 20, 10, 20));

                        Scene scene = new Scene(vbox);
                        scene.getStylesheets().add("JavaFXApp.css");//на сцену прибиваєм фон
                        stage.setTitle("Hello " + MainController.students.getLogin());
                        stage.setScene(scene);
                        stage.show();
                        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent event) {
                                Platform.exit();
                            }
                        });
                    } else {
                        label.setStyle("-fx-text-fill: red");
                        label.setText("Invalid authentication");
                        //return 1;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //return 0;
    }


    private void initTableData() {//функція створена для відображення вмісту каталогів, жостких і з'ємних дисків
        String path = pathTextField.getText();
        File[] list = new File(path).listFiles();
        tableData.clear();//табличні дані чистяться
        if (path.length() > 3)//умова для повернення назад
            tableData.add(new DirectoryItem("..", "folder"));
        if (list != null)//умова для показу файлів
            for (File file : list) {//показуємо всі файли і директорії що знаходяться в каталогі
                String fileName = file.getName();
                if (file.isDirectory())//якщо даний файл є каталогом
                    tableData.add(new DirectoryItem(fileName, "folder"));//то відобразимо його та присвоємо йому розміру значення "каталог"
                else//якщо даний файл є файлом
                    tableData.add(new DirectoryItem(fileName, String.valueOf(file.length())));//то відобразимо файл і покажемо його розмір
            }
    }









/**/
    /*public void RenameFile(ActionEvent actionEvent) {
        File file = new java.io.File(pathTextField.getText().toString()+); // создаем объект на файл test.txt
        if(file.exists()){ // если файл существует, то переименовываем его
            file.renameTo(new java.io.File("D:\\main.java"));
        }
        else{
            System.out.println("File not found!");
        }
    }*/
}
