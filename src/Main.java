import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        // -----------------------------
        // Controllo OS e OpenGL
        // -----------------------------
        String os = System.getProperty("os.name").toLowerCase();

        if(os.contains("win")) {
            // Forza OpenGL su Windows
            System.setProperty("sun.java2d.opengl", "true");
            // Disabilita Direct3D per evitare fallback problematici
            System.setProperty("sun.java2d.d3d", "false");
        }

        // -----------------------------
        // Creazione finestra
        // -----------------------------
        JFrame window = new JFrame("Bob's Adventure");
        window.setContentPane(new GamePanel());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setVisible(true);
    }
}
