package itumulator.simulator;

import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.*;

public class Rabbit implements Actor{

    int age;
    double energy;
    boolean hasEaten;
    private final int deathAge;

    RabbitHole hole;

    public Rabbit() {
        age = 0;
        energy = calculateEnergy(0);
        this.deathAge = new Random().nextInt(5) + 8;
    }

    @Override
    public void act(World world) {
        Set<Location> nearbyTiles = getNearbyTiles(world);
        Map<Object, List<Location>> classMap = getTileClass(world, nearbyTiles);


        checkForDeath(world);
        Location targetLocation = nextLocation(classMap);
        if (targetLocation != null) {
            if (world.getTile(world.getLocation(this)).getClass() == Grass.class) {
                world.delete(world.getTile(targetLocation));
                hasEaten = false;
            }
            move(targetLocation, world);
        }
        if (age > 0) {
            reproduce(world, classMap);
        }


    }

    private void checkForDeath(World world) {
        if (age > this.deathAge) {
            world.delete(this);
        }
    }

    private void move(Location target, World world) {
        if (energy <= 10) {
            return;
        }
        energy -= 10;
        world.move(this, target);
    }

    private List<Location> searchForFood(World world, Set<Location> tileset) {
        List<Location> grassTiles = new ArrayList<>();
        for (Location tile : tileset) {
            if (world.getTile(tile).getClass() == Grass.class) {
                grassTiles.add(tile);
            }
        }
        return grassTiles;
    }

    private Map<Object, List<Location>> getTileClass(World world, Set<Location> nearbyTiles) {
        Map<Object, List<Location>> map = new HashMap<>();
        for (Location tile : nearbyTiles) {
            Object obj = world.getTile(tile);
            if (!map.containsKey(obj.getClass())) {
                map.put(obj.getClass(), new ArrayList<>());
            }
            map.get(obj.getClass()).add(tile);
        }

        return map;
    }


    private Location nextLocation(Map<Object, List<Location>> map) {
        List<Location> targets;
        if (!map.get(Grass.class).isEmpty()) {
            targets = map.get(Grass.class);
        } else {
            targets = map.get(null);
        }
        if (targets.isEmpty()) {
            return null;
        }
        int randomTileIndex = new Random().nextInt(targets.size());


        return targets.get(randomTileIndex);

    }

    private double calculateEnergy(int a) {
        return Math.pow(-(a-5), 2) + 8*2*4;
    }


    private void enterHole(World world) {
        world.remove(this);
        hole.addRabbit(this);
    }

    private void exitHole(World world) {
        world.setTile(world.getLocation(hole), this);
        energy = calculateEnergy(age);
        age += 1;
    }



    private void reproduce(World world, Map<Object, List<Location>> map) {
        if (energy <= 30) {
            return;
        }
        energy -= 30;
        if (map.containsKey(Rabbit.class)) {
            for (Location l : map.get(Rabbit.class)) {
                Rabbit r = (Rabbit) world.getTile(l);
                if (r.age > 0 && map.containsKey(null)) {
                    world.setTile(map.get(null).get(0), new Rabbit());
                }
            }
        }
    }

    private Set<Location> getNearbyTiles(World world) {
        return world.getSurroundingTiles();
    }



}
