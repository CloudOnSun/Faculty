package com.example.contestclient;


//public class Main extends Application{
//    @Override
//    public void start(Stage stage) throws IOException {
//
//        Properties props=new Properties();
//        try {
//            props.load(new FileReader("bd.config"));
//        } catch (IOException e) {
//            System.out.println("Cannot find bd.config "+e);
//        }
//
//        var adminRepo = new AdminDBRepository(props);
//        var contRepo = new ContestantDBRepository(props);
//        var partRepo = new ParticipationDBRepository(props);
//        var raceRepo = new SwimmingRaceDBRepository(props);
//
//        Service srv = new Service(adminRepo, contRepo, partRepo, raceRepo);
//
//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/login.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
//        LoginController loginController = fxmlLoader.getController();
//        loginController.setService(srv);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public static void main(String[] args) {
//        Application.launch();
//    }
//
//}