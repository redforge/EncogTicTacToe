import org.encog.neural.neat.NEATPopulation;

/**
 * Created by Ethan on 2/15/2017.
 */
public class ShittyPopCopyThing {
    private NEATPopulation pop;

    public ShittyPopCopyThing(NEATPopulation pop) {
        this.pop = pop;
    }

    public ShittyPopCopyThing(ShittyPopCopyThing shittyPopCopyThingArg) {
        this(shittyPopCopyThingArg.getPop());
    }

    public void setPop(NEATPopulation pop) {
        this.pop = pop;
    }
    public NEATPopulation getPop() {
        return pop;
    }
}
