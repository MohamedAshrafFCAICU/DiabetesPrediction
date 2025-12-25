package Config;

import OperatorsContracts.*;

public class GeneticOperators<T> {
    private final IEncoder<T> encoder;
    private final IFitnessFunction<T> fitnessFunction;
    private final ISelector<T> selector;
    private final ICrossover<T> crossover;
    private final IMutator<T> mutator;
    private final IReplacementStrategy<T> replacementStrategy;

    private GeneticOperators(Builder<T> builder) {
        this.encoder = builder.encoder;
        this.fitnessFunction = builder.fitnessFunction;
        this.selector = builder.selector;
        this.crossover = builder.crossover;
        this.mutator = builder.mutator;
        this.replacementStrategy = builder.replacementStrategy;
    }

    public IEncoder<T> getEncoder() {
        return encoder;
    }

    public IFitnessFunction<T> getFitnessFunction() {
        return fitnessFunction;
    }

    public ISelector<T> getSelector() {
        return selector;
    }

    public ICrossover<T> getCrossover() {
        return crossover;
    }

    public IMutator<T> getMutator() {
        return mutator;
    }

    public IReplacementStrategy<T> getReplacementStrategy() {
        return replacementStrategy;
    }


    public static class Builder<T> {
        private IEncoder<T> encoder;
        private IFitnessFunction<T> fitnessFunction;
        private ISelector<T> selector;
        private ICrossover<T> crossover;
        private IMutator<T> mutator;
        private IReplacementStrategy<T> replacementStrategy;

        public Builder<T> encoder(IEncoder<T> val) {
            this.encoder = val;
            return this;
        }

        public Builder<T> fitnessFunction(IFitnessFunction<T> val) {
            this.fitnessFunction = val;
            return this;
        }

        public Builder<T> selector(ISelector<T> val) {
            this.selector = val;
            return this;
        }

        public Builder<T> crossover(ICrossover<T> val) {
            this.crossover = val;
            return this;
        }

        public Builder<T> mutator(IMutator<T> val) {
            this.mutator = val;
            return this;
        }

        public Builder<T> replacementStrategy(IReplacementStrategy<T> val) {
            this.replacementStrategy = val;
            return this;
        }

        public GeneticOperators<T> build() {
            validate();
            return new GeneticOperators<>(this);
        }

        private void validate() {
            if (encoder == null) {
                throw new IllegalStateException("Encoder is required");
            }
            if (fitnessFunction == null) {
                throw new IllegalStateException("Fitness function is required");
            }
            if (selector == null) {
                throw new IllegalStateException("Selector is required");
            }
            if (crossover == null) {
                throw new IllegalStateException("Crossover is required");
            }
            if (mutator == null) {
                throw new IllegalStateException("Mutator is required");
            }
            if (replacementStrategy == null) {
                throw new IllegalStateException("Replacement strategy is required");
            }
        }
    }
}
