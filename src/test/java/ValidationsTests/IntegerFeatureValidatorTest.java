package ValidationsTests;

import Core._Chromosome;
import Core._Gene;
import Core.IntegerGene;
import Validation.IChromosomeValidator;
import Validation.IntegerFeatureValidator;
import Validation.ValidationResult;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IntegerFeatureValidatorTest {

    /* TODO BY
               Mohamed Ashraf

               DONE
     */

    @Test
    public void testIntegerFeatureValidatorWithValidChromosome() {
        // Arrange
        IChromosomeValidator integerValidator = new IntegerFeatureValidator(1, 5, 5);


        List<_Gene> genes = new ArrayList<>();
        genes.add(new IntegerGene(5));
        genes.add(new IntegerGene(2));
        genes.add(new IntegerGene(3));
        genes.add(new IntegerGene(1));
        genes.add(new IntegerGene(4));

        _Chromosome chromosome = new _Chromosome(genes);

        boolean isValid = integerValidator.isValid(chromosome);

        assertTrue(isValid, "Chromosome should be valid");
    }

    @Test
    public void testIntegerFeatureValidatorWithInvalidChromosome() {
        // Arrange
        IChromosomeValidator integerValidator = new IntegerFeatureValidator(1, 5, 5);


        List<_Gene> genes = new ArrayList<>();
        genes.add(new IntegerGene(5));
        genes.add(new IntegerGene(2));
        genes.add(new IntegerGene(3));
        genes.add(new IntegerGene(1));
        genes.add(new IntegerGene(5));

        _Chromosome chromosome = new _Chromosome(genes);

        boolean isValid = integerValidator.isValid(chromosome);

        assertFalse(isValid, "Chromosome should be valid");
    }

    @Test
    public void testIntegerFeatureValidatorAndGetValidationResult() {
        // Arrange
        IChromosomeValidator integerValidator = new IntegerFeatureValidator(1, 5, 5);


        List<_Gene> genes = new ArrayList<>();
        genes.add(new IntegerGene(5));
        genes.add(new IntegerGene(2));
        genes.add(new IntegerGene(3));
        genes.add(new IntegerGene(3));
        genes.add(new IntegerGene(5));

        _Chromosome chromosome = new _Chromosome(genes);

        ValidationResult result = integerValidator.validate(chromosome);

        System.out.println(result.getViolations());
    }

}
