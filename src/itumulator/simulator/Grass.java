package itumulator.simulator;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Grass implements Actor, NonBlocking {
    private static double SPREAD_PROBABILITY = 0.15;
    private Random random;

    public Grass() {
        this(new Random());
    }

    public Grass(Random random) {
        this.random = random;
    }

    @Override
    public void act(World world) {
        // grows only on day
        if (!world.isDay()) {
            return;
        }
        if (random.nextDouble() >= SPREAD_PROBABILITY) {
            return;
        }

        spread(world);
    }

    private void spread(World world) {
        Location current = world.getLocation(this);
        Set<Location> surrounding = world.getSurroundingTiles(current);

        // check for nonBlocking
        List<Location> available = new ArrayList<>();
        for (Location loc : surrounding) {
            if (!world.containsNonBlocking(loc)) {
                available.add(loc);
            }
        }

        if (available.isEmpty()) {
            return;
        }

        Location target = available.get(random.nextInt(available.size()));
        world.setTile(target, new Grass(random));
    }
}