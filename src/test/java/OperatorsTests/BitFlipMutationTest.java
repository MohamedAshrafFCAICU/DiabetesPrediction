package OperatorsTests;

import Core.BinaryGene;
import Core._Chromosome;
import Core._Gene;
import Operators.BitFlipMutation;
import Problems.DiabetesDataset;
import Problems.DiabetesPatient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BitFlipMutationTest
{
    /* TODO BY
                Ahmed Kamel
     */


    @Test
    @DisplayName("Test mutation with 0% mutation rate - no changes")
    void testNoMutation() {
        BitFlipMutation mutator = new BitFlipMutation(0.0);
        _Chromosome<Boolean> chromosome = createChromosome(10, true);

        mutator.mutate(chromosome);

        for (int i = 0; i < chromosome.getLength(); i++) {
            assertTrue(chromosome.getGene(i).getValue(),
                    "Gene should not be mutated with 0% rate");
        }
    }

    @Test
    @DisplayName("Test mutation with 100% mutation rate - all bits flip")
    void testFullMutation() {
        BitFlipMutation mutator = new BitFlipMutation(1.0);
        _Chromosome<Boolean> chromosome = createChromosome(10, true);

        mutator.mutate(chromosome);

        for (int i = 0; i < chromosome.getLength(); i++) {
            assertFalse(chromosome.getGene(i).getValue(),
                    "Gene should be flipped with 100% rate");
        }
    }

    @Test
    @DisplayName("Test mutation flips bits correctly")
    void testBitFlip() {
        BitFlipMutation mutator = new BitFlipMutation(1.0, 42);
        List<_Gene<Boolean>> genes = new ArrayList<>();
        genes.add(new BinaryGene(true));
        genes.add(new BinaryGene(false));
        genes.add(new BinaryGene(true));

        _Chromosome<Boolean> chromosome = new _Chromosome<>(genes);
        mutator.mutate(chromosome);

        assertFalse(chromosome.getGene(0).getValue(), "True should flip to false");
        assertTrue(chromosome.getGene(1).getValue(), "False should flip to true");
        assertFalse(chromosome.getGene(2).getValue(), "True should flip to false");
    }


    private _Chromosome<Boolean> createChromosome(int length, boolean value) {
        List<_Gene<Boolean>> genes = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            genes.add(new BinaryGene(value));
        }
        return new _Chromosome<>(genes);
    }

}
