package itumulator.simulator;

import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.List;

public class RabbitHole implements Actor, NonBlocking {
    List<Rabbit> occupants;

    @Override
    public void act(World world) {

    }

    public void addRabbit(Rabbit r) {
        occupants.add(r);
    }
}
