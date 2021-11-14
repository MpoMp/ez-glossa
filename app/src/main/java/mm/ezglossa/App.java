package mm.ezglossa;

import static javafx.application.Application.launch;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());

       launch(GuiApp.class, args);

    }
}
