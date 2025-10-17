package Config;

public class GeneticSettings {
    private final int populationSize;
    private final int chromosomeLength;
    private final double crossoverRate;
    private final double mutationRate;
    private final int maxGenerations;
    private final double targetFitness;
    private final int eliteCount;
    private final boolean verbose;

    private GeneticSettings(Builder builder) {
        this.populationSize = builder.populationSize;
        this.chromosomeLength = builder.chromosomeLength;
        this.crossoverRate = builder.crossoverRate;
        this.mutationRate = builder.mutationRate;
        this.maxGenerations = builder.maxGenerations;
        this.targetFitness = builder.targetFitness;
        this.eliteCount = builder.eliteCount;
        this.verbose = builder.verbose;
    }

    // Getters
    public int getPopulationSize() {
        return populationSize;
    }

    public int getChromosomeLength() {
        return chromosomeLength;
    }

    public double getCrossoverRate() {
        return crossoverRate;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public int getMaxGenerations() {
        return maxGenerations;
    }

    public double getTargetFitness() {
        return targetFitness;
    }

    public int getEliteCount() {
        return eliteCount;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public static class Builder {
        // Default values
        private int populationSize = 50;
        private int chromosomeLength = 20;
        private double crossoverRate = 0.8;
        private double mutationRate = 0.1;
        private int maxGenerations = 100;
        private double targetFitness = Double.MAX_VALUE;
        private int eliteCount = 2;
        private boolean verbose = false;

        public Builder populationSize(int val) {
            this.populationSize = val;
            return this;
        }

        public Builder chromosomeLength(int val) {
            this.chromosomeLength = val;
            return this;
        }

        public Builder crossoverRate(double val) {
            this.crossoverRate = val;
            return this;
        }

        public Builder mutationRate(double val) {
            this.mutationRate = val;
            return this;
        }

        public Builder maxGenerations(int val) {
            this.maxGenerations = val;
            return this;
        }

        public Builder targetFitness(double val) {
            this.targetFitness = val;
            return this;
        }

        public Builder eliteCount(int val) {
            this.eliteCount = val;
            return this;
        }

        public Builder verbose(boolean val) {
            this.verbose = val;
            return this;
        }

        public GeneticSettings build() {
            validate();
            return new GeneticSettings(this);
        }

        private void validate() {
            if (populationSize <= 0) {
                throw new IllegalArgumentException("Population size must be positive");
            }
            if (chromosomeLength <= 0) {
                throw new IllegalArgumentException("Chromosome length must be positive");
            }
            if (crossoverRate < 0 || crossoverRate > 1) {
                throw new IllegalArgumentException("Crossover rate must be between 0 and 1");
            }
            if (mutationRate < 0 || mutationRate > 1) {
                throw new IllegalArgumentException("Mutation rate must be between 0 and 1");
            }
            if (maxGenerations <= 0) {
                throw new IllegalArgumentException("Max generations must be positive");
            }
            if (eliteCount < 0) {
                throw new IllegalArgumentException("Elite count cannot be negative");
            }
            if (eliteCount >= populationSize) {
                throw new IllegalArgumentException("Elite count must be less than population size");
            }
        }
    }

    @Override
    public String toString() {
        return String.format("""
                GeneticSettings {
                  Population Size: %d
                  Chromosome Length: %d
                  Crossover Rate: %.2f
                  Mutation Rate: %.2f
                  Max Generations: %d
                  Elite Count: %d
                  Target Fitness: %.2f
                }""",
                populationSize, chromosomeLength, crossoverRate,
                mutationRate, maxGenerations, eliteCount, targetFitness);
    }
}
