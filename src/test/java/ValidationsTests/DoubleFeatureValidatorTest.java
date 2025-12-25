package ValidationsTests;

import Core.DoubleGene;
import Core._Chromosome;
import Core._Gene;
import Validation.DoubleFeatureValidator;
import Validation.IChromosomeValidator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DoubleFeatureValidatorTest
{
     /* TODO BY
                Anas Mahmoud

                Done
     */

    @Test
    public void TestDoubleFeatureValidatorWithValidChromosome()
    {
        IChromosomeValidator  doubleValidator =  new DoubleFeatureValidator(0.1,0.9);
        ArrayList<_Gene> genes = new ArrayList<>();
        genes.add(new DoubleGene(0.2));
        genes.add(new DoubleGene(0.3));
        genes.add(new DoubleGene(0.7));
        genes.add(new DoubleGene(0.8));
        genes.add(new DoubleGene(0.1));

        _Chromosome chromosome = new _Chromosome(genes);

        boolean isValid = doubleValidator.isValid(chromosome);

        assertTrue(isValid, "Chromosome should be valid");
    }

    @Test
    public void TestDoubleFeatureValidatorWithInvalidChromosome()
    {
        IChromosomeValidator  doubleValidator =  new DoubleFeatureValidator(0.1,0.7);
        ArrayList<_Gene> genes = new ArrayList<>();
        genes.add(new DoubleGene(0.2));
        genes.add(new DoubleGene(0.3));
        genes.add(new DoubleGene(0.7));
        genes.add(new DoubleGene(0.8));
        genes.add(new DoubleGene(0.1));

        _Chromosome chromosome = new _Chromosome(genes);

        boolean isValid = doubleValidator.isValid(chromosome);

        assertFalse(isValid, "Chromosome should be valid");
    }

    @Test
    public void TestDoubleFeatureValidatorResultWithInvalidChromosomeAndGetValidationResult()
    {
        IChromosomeValidator  doubleValidator =  new DoubleFeatureValidator(0.1,0.7);
        ArrayList<_Gene> genes = new ArrayList<>();
        genes.add(new DoubleGene(0.2));
        genes.add(new DoubleGene(0.3));
        genes.add(new DoubleGene(0.7));
        genes.add(new DoubleGene(0.8));
        genes.add(new DoubleGene(0.1));

        _Chromosome chromosome = new _Chromosome(genes);

        System.out.println(doubleValidator.validate(chromosome));
    }

}
