import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

import itumulator.executable.Program;
import itumulator.simulator.Grass;
import itumulator.simulator.Rabbit;
import itumulator.world.Location;
import itumulator.world.World;

public class Main {

    public static void main(String[] args) {
        int size = 5;
        Program p = new Program(size, 800, 75);

        World w = p.getWorld();

        // w.setTile(new Location(0, 0), new <MyClass>());

        // p.setDisplayInformation(<MyClass>.class, new DisplayInformation(<Color>, "<ImageName>"));





        p.show();
    }


    private static void startWorldWithFile(String fileName) {
        File f = new File(String.format("Data/week-1/%s", fileName));

        try (Scanner myReader = new Scanner(f)) {
            Program p = new Program(Integer.parseInt(myReader.nextLine()), 800, 75);
            World w = p.getWorld();

            while (myReader.hasNextLine()) {
                String[] s = myReader.nextLine().split(" ");
                String entity = s[0];
                int amount = Integer.parseInt(s[1]);

                Object entityClass = null;
                switch (entity) {
                    case "rabbit":
                        entityClass = new Rabbit();
                        break;
                    case "grass":
                        entityClass = new Grass();
                        break;
                    default:
                        System.out.println("Cannot resolve the system: " + entity);
                }
                for (int i = 0; i < amount; i++) {
                    Random random = new Random();
                    int xCord = random.nextInt(w.getSize());
                    int yCord = random.nextInt(w.getSize());
                    Location l = new Location(xCord, yCord);

                    while (w.getTile(l) != null) {
                        xCord = random.nextInt(w.getSize());
                        yCord = random.nextInt(w.getSize());
                        l = new Location(xCord, yCord);
                    }
                    w.setTile(l, entityClass);
                }
            }


        } catch (FileNotFoundException e) {
            System.out.println(String.format("File not found with error message: %s", e.getMessage()));
        }
    }
}