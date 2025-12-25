# 🧬 Diabetes Feature Selection using Genetic Algorithms

A high-performance Java framework implementing Genetic Algorithms (GA) for intelligent feature selection in diabetes prediction. This production-ready system combines evolutionary computation with machine learning to identify optimal feature subsets, achieving faster training times and improved model interpretability.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/)
[![Maven](https://img.shields.io/badge/Maven-3.11.0-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Tests](https://img.shields.io/badge/Tests-19%20Passing-brightgreen.svg)](#testing)

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Key Features](#-key-features)
- [Tech Stack](#-tech-stack)
- [System Architecture](#-system-architecture)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [Usage Examples](#-usage-examples)
- [Performance Optimizations](#-performance-optimizations)
- [Testing](#-testing)
- [Configuration](#-configuration)
- [Results & Metrics](#-results--metrics)
- [Future Improvements](#-future-improvements)
- [Contributors](#-contributors)

---

## 🎯 Overview

This project addresses the challenge of **high-dimensional feature spaces in medical diagnosis** by implementing a sophisticated genetic algorithm framework. Rather than using all available medical indicators, the system intelligently evolves feature subsets that maximize prediction accuracy while minimizing computational overhead.

### 🔬 Why This Matters

- **Medical Interpretability**: Fewer features = clearer insights for healthcare professionals
- **Computational Efficiency**: Reduced feature sets cut training time by up to 70%
- **Overfitting Prevention**: Eliminates redundant features that confuse ML models
- **Production Ready**: Parallel evaluation, caching, and thread-safe design

### 🎯 Problem Solved

Given a diabetes dataset with 20+ features, identify the optimal 5-10 feature subset that:
- Maximizes Random Forest classification accuracy
- Minimizes model complexity
- Generalizes well to unseen data

---

## ✨ Key Features

### 🧬 Advanced Genetic Algorithm Engine

- **Multiple Representations**: Binary, Integer, and Real-valued encodings
- **Generic Type System**: `_Chromosome<T>` supports any gene type
- **Flexible Operators**: Mix and match 13+ genetic operators
- **Smart Validation**: Built-in constraint checking and automatic repair

### 🚀 Production-Grade Performance

- **Parallel Fitness Evaluation**: Multi-threaded population assessment
- **Intelligent Caching**: 90%+ cache hit rate reduces redundant evaluations
- **Thread-Safe Design**: ConcurrentHashMap and ThreadLocal for concurrency
- **Stratified Sampling**: Balanced class distribution in train/test splits

### 🔧 Comprehensive Operator Suite

#### Selection Methods
- **Tournament Selection**: Simulates survival of the fittest
- **Roulette Wheel Selection**: Fitness-proportionate selection

#### Crossover Techniques
- **Single-Point Crossover**: Classic genetic recombination
- **Uniform Crossover**: Gene-by-gene probabilistic exchange
- **Order-1 Crossover (OX1)**: Preserves gene ordering for permutations

#### Mutation Strategies
- **Bit-Flip Mutation**: Binary representation mutations
- **Swap Mutation**: Position-based gene exchanges
- **Uniform Mutation**: Continuous value perturbations
- **Integer Mutation**: Duplicate-prevention for permutation encodings

#### Replacement Strategies
- **Elitist Replacement**: Guarantees best solutions survive
- **Generational Replacement**: Complete population turnover
- **Steady-State Replacement**: Gradual evolution

### 🤖 ML Integration

- **Random Forest Classifier**: Ensemble learning via Smile library
- **Configurable Trees**: Adjustable depth, node size, and tree count
- **Cross-Validation**: Built-in train/test splitting with stratification
- **Accuracy-Based Fitness**: Balances accuracy with feature economy

---

## 🛠 Tech Stack

| Component | Technology | Version | Purpose |
|-----------|-----------|---------|---------|
| **Language** | Java | 21 | LTS with modern features |
| **Build Tool** | Maven | 3.11.0 | Dependency management |
| **ML Library** | Smile | 3.0.2 | Random Forest implementation |
| **Data Processing** | Gson | 2.10.1 | JSON/CSV parsing |
| **Testing** | JUnit 5 | 5.10.0 | Unit & integration tests |
| **Mocking** | Mockito | 5.5.0 | Test doubles |
| **Assertions** | AssertJ | 3.24.2 | Fluent test assertions |
| **Concurrency** | Java Executors | Built-in | Parallel processing |

---

## 🏗 System Architecture

### High-Level Architecture

```
┌────────────────────────────────────────────────────────┐
│                   Client Layer                          │
│              Main.java (Entry Point)                    │
│      Binary GA Runner │ Integer GA Runner               │
└────────────────────────────────────────────────────────┘
                          │
                          ▼
┌────────────────────────────────────────────────────────┐
│              Configuration Layer                        │
│   GeneticSettings (Builder) │ GeneticOperators (Builder)│
│   Population: 50 │ Generations: 100 │ Elite: 5         │
└────────────────────────────────────────────────────────┘
                          │
                          ▼
┌────────────────────────────────────────────────────────┐
│              Core GA Engine                             │
│   _GeneticAlgorithm<T> (Main Evolution Loop)           │
│   ├─ Initialize Population                             │
│   ├─ Evaluate Fitness (Parallel)                       │
│   ├─ Select Parents                                     │
│   ├─ Crossover & Mutation                              │
│   └─ Replace Population                                │
└────────────────────────────────────────────────────────┘
         │                │                │
         ▼                ▼                ▼
┌─────────────┐  ┌─────────────┐  ┌─────────────┐
│  Operators  │  │  Validation │  │   Problems  │
│             │  │             │  │             │
│ • Encoders  │  │ • Binary    │  │ • Dataset   │
│ • Selectors │  │ • Integer   │  │ • Fitness   │
│ • Crossover │  │ • Double    │  │ • Patient   │
│ • Mutators  │  │ • Repair    │  │             │
│ • Replace   │  │             │  │             │
└─────────────┘  └─────────────┘  └─────────────┘
```

### Data Flow

```
Dataset (CSV)
    │
    ├─► DiabetesDataset.loadFromCsv()
    │       │
    │       ├─► Parse features & labels
    │       └─► Stratified 80/20 split
    │
    ▼
Train/Test Split
    │
    ├─► X_train (5000 samples) ──┐
    ├─► y_train (labels)         │
    ├─► X_test (500 samples)     │
    └─► y_test (labels)          │
                                 │
                                 ▼
                    DiabetesFitnessFunction<T>
                         │
                         ├─► Thread-Local RF Models
                         ├─► Feature Subset Selection
                         ├─► Train on X_train_subset
                         ├─► Evaluate on X_test_subset
                         └─► Cache Results
                                 │
                                 ▼
                         Fitness = Accuracy - (0.01 × FeatureRatio)
```

### Design Patterns Used

- **Builder Pattern**: `GeneticSettings.Builder`, `GeneticOperators.Builder`
- **Strategy Pattern**: Interchangeable genetic operators via interfaces
- **Template Method**: `_GeneticAlgorithm.evolve()` defines algorithm skeleton
- **Factory Pattern**: `IEncoder.createInitialPopulation()`
- **Observer Pattern**: Verbose mode for evolution tracking

---

## 📁 Project Structure

```
DiabetesPrediction/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── Client/
│   │   │   │   └── Main.java                    # 🎯 Application entry point
│   │   │   ├── Config/
│   │   │   │   ├── GeneticSettings.java         # ⚙️ GA hyperparameters
│   │   │   │   └── GeneticOperators.java        # 🔧 Operator configuration
│   │   │   ├── Core/
│   │   │   │   ├── _GeneticAlgorithm.java       # 🧬 Main evolution engine
│   │   │   │   ├── _Population.java             # 👥 Population management
│   │   │   │   ├── _Chromosome.java             # 🧬 Chromosome representation
│   │   │   │   ├── _Gene.java                   # 🧬 Gene interface
│   │   │   │   ├── BinaryGene.java              # 0/1 genes
│   │   │   │   ├── IntegerGene.java             # Integer genes
│   │   │   │   └── DoubleGene.java              # Real-valued genes
│   │   │   ├── Operators/
│   │   │   │   ├── Encoders/
│   │   │   │   │   ├── BinaryEncoder.java       # Binary representation
│   │   │   │   │   ├── IntegerEncoder.java      # Permutation encoding
│   │   │   │   │   └── DoubleEncoder.java       # Real-valued encoding
│   │   │   │   ├── Selectors/
│   │   │   │   │   ├── TournamentSelector.java  # k-tournament
│   │   │   │   │   └── RouletteWheelSelector.java # Fitness-proportionate
│   │   │   │   ├── Crossovers/
│   │   │   │   │   ├── SinglePointCrossover.java
│   │   │   │   │   ├── UniformCrossover.java
│   │   │   │   │   └── Order1Crossover.java     # For permutations
│   │   │   │   ├── Mutations/
│   │   │   │   │   ├── BitFlipMutation.java     # For binary
│   │   │   │   │   ├── SwapMutation.java        # For permutations
│   │   │   │   │   ├── UniformMutation.java     # For real-valued
│   │   │   │   │   └── IntegerMutation.java     # Duplicate-safe
│   │   │   │   └── Replacements/
│   │   │   │       ├── ElitistReplacement.java  # Preserves top N
│   │   │   │       ├── GenerationalReplacement.java
│   │   │   │       └── SteadyStateReplacement.java
│   │   │   ├── OperatorsContracts/
│   │   │   │   ├── IEncoder.java                # Encoding interface
│   │   │   │   ├── IFitnessFunction.java        # Fitness evaluation
│   │   │   │   ├── ISelector.java               # Selection interface
│   │   │   │   ├── ICrossover.java              # Crossover interface
│   │   │   │   ├── IMutator.java                # Mutation interface
│   │   │   │   └── IReplacementStrategy.java    # Replacement interface
│   │   │   ├── Validation/
│   │   │   │   ├── IChromosomeValidator.java    # Validation interface
│   │   │   │   ├── BinaryFeatureValidator.java  # Min/max features
│   │   │   │   ├── IntegerFeatureValidator.java # No duplicates
│   │   │   │   ├── DoubleFeatureValidator.java  # Range validation
│   │   │   │   └── ValidationResult.java        # Validation report
│   │   │   ├── Problems/
│   │   │   │   ├── DiabetesDataset.java         # 📊 Dataset loader
│   │   │   │   ├── DiabetesPatient.java         # Patient record
│   │   │   │   └── DiabetesFitnessFunction.java # 🎯 Fitness evaluation
│   │   │   └── ML/
│   │   │       └── SimpleRandomForest.java      # 🌲 RF wrapper
│   │   └── resources/
│   │       └── oversampled_dataset.csv          # 📁 Training data
│   └── test/
│       └── java/
│           ├── OperatorsTests/                  # ✅ 13 test classes
│           ├── ValidationsTests/                # ✅ 3 test classes
│           ├── ProblemsTests/                   # ✅ 2 test classes
│           └── MLModelsTests/                   # ✅ 1 test class
└── pom.xml                                      # Maven configuration
```

---

## 🚀 Getting Started

### Prerequisites

```bash
☑️ Java 21 (LTS recommended)
☑️ Maven 3.11+
☑️ Git (optional)
☑️ 8GB RAM minimum (for dataset processing)
```

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/MohamedAshrafFCAICU/DiabetesPrediction.git
cd DiabetesPrediction
```

2. **Build the project**
```bash
mvn clean install
```

3. **Run tests**
```bash
mvn test
```

4. **Run the application**
```bash
# Update CSV path in Main.java first!
mvn exec:java -Dexec.mainClass="Client.Main"
```

### Quick Start Configuration

```java
// In Main.java, update the dataset path:
private static final String CSV_PATH = 
    "C:\\YOUR_PATH\\oversampled_dataset.csv";
```

---

## 💻 Usage Examples

### Example 1: Binary Representation (Feature Subset Selection)

```java
// Configure GA settings
GeneticSettings settings = new GeneticSettings.Builder()
    .populationSize(50)
    .chromosomeLength(20)          // 20 features
    .crossoverRate(0.8)
    .mutationRate(0.1)
    .maxGenerations(100)
    .eliteCount(5)
    .verbose(true)
    .build();

// Set up binary representation (5-10 features)
BinaryFeatureValidator validator = 
    new BinaryFeatureValidator(5, 10);

// Configure genetic operators
GeneticOperators<Boolean> operators = 
    new GeneticOperators.Builder<Boolean>()
    .encoder(new BinaryEncoder(validator, 5, 10))
    .fitnessFunction(new DiabetesFitnessFunction<>(...))
    .selector(new RouletteWheelSelector<>())
    .crossover(new SinglePointCrossover<>(0.8))
    .mutator(new BitFlipMutation(0.1))
    .replacementStrategy(new ElitistReplacement<>(5))
    .build();

// Run evolution
_GeneticAlgorithm<Boolean> ga = 
    new _GeneticAlgorithm<>(settings, operators);
_Chromosome<Boolean> best = ga.evolve();

// Extract selected features
List<Integer> features = 
    (List<Integer>) operators.getEncoder().decode(best);
System.out.println("Selected features: " + features);
```

### Example 2: Integer Representation (Feature Ranking)

```java
// Configure for permutation encoding
GeneticSettings settings = new GeneticSettings.Builder()
    .populationSize(50)
    .chromosomeLength(6)           // Select top 6 features
    .crossoverRate(0.8)
    .mutationRate(0.1)
    .maxGenerations(100)
    .build();

IntegerFeatureValidator validator = 
    new IntegerFeatureValidator(1, 20, 6);

GeneticOperators<Integer> operators = 
    new GeneticOperators.Builder<Integer>()
    .encoder(new IntegerEncoder(validator, 1, 20))
    .selector(new TournamentSelector<>(3))
    .crossover(new Order1Crossover<>(0.8, validator))
    .mutator(new IntegerMutation(0.1, 1, 20, validator))
    .replacementStrategy(new GenerationalReplacement<>())
    .build();

_GeneticAlgorithm<Integer> ga = 
    new _GeneticAlgorithm<>(settings, operators);
_Chromosome<Integer> best = ga.evolve();
```

### Example 3: Loading & Splitting Dataset

```java
// Load from CSV
DiabetesDataset dataset = DiabetesDataset.loadFromCsv(
    "path/to/oversampled_dataset.csv"
);

System.out.println("Total patients: " + dataset.getSize());
System.out.println("Features: " + dataset.getFeatureCount());

// Stratified 80/20 split (balanced classes)
DiabetesDataset.DatasetSplit split = dataset.split(0.8);

double[][] X_train = split.getTrainingFeatures();
int[] y_train = split.getTrainingLabels();
double[][] X_test = split.getTestFeatures();
int[] y_test = split.getTestLabels();
```

---

## ⚡ Performance Optimizations

### 1. Parallel Fitness Evaluation

```java
// IFitnessFunction uses ExecutorService for parallel evaluation
default void evaluate(_Population<T> population) {
    int processors = Runtime.getRuntime().availableProcessors();
    ExecutorService executor = Executors.newFixedThreadPool(processors);
    
    // Evaluates all chromosomes in parallel
    // 4-core CPU: 4x speedup!
}
```

### 2. Intelligent Caching

```java
// Fitness results cached by chromosome signature
private final Map<String, Double> fitnessCache = 
    new ConcurrentHashMap<>();

// Cache stats: 90%+ hit rate
fitnessFunction.printCacheStats();
// Output: Hits: 4500, Misses: 500, Rate: 90.0%
```

### 3. ThreadLocal Models

```java
// Each thread gets its own RF model (avoids synchronization)
private final ThreadLocal<SimpleRandomForest> threadLocalModel;

threadLocalModel = ThreadLocal.withInitial(() -> 
    new SimpleRandomForest(numTrees, 10, 10)
);
```

### 4. Stratified Sampling

```java
// Reduces dataset from 100K → 5K samples
// Maintains class balance (50/50 diabetic/non-diabetic)
private static final int MAX_TRAIN_SAMPLES = 5000;
private static final int MAX_TEST_SAMPLES = 500;
```

### Performance Benchmarks

| Configuration | Time per Generation | Total Time (100 gens) |
|---------------|--------------------|-----------------------|
| 50 trees, no cache | 12s | 20 minutes |
| 50 trees, with cache | 3s | 5 minutes ✅ |
| 100 trees, with cache | 5s | 8.3 minutes |
| Parallel eval (4 cores) | 2s | 3.3 minutes 🚀 |

---

## 🧪 Testing

### Test Coverage

```
✅ 19 Test Classes
├── OperatorsTests/
│   ├── TournamentSelectorTest.java
│   ├── RouletteWheelSelectorTest.java
│   ├── SinglePointCrossoverTest.java
│   ├── UniformCrossoverTest.java
│   ├── Order1CrossoverTest.java
│   ├── BitFlipMutationTest.java
│   ├── SwapMutationTest.java
│   ├── UniformMutationTest.java
│   ├── IntegerMutationTest.java
│   ├── ElitistReplacementTest.java
│   ├── GenerationalReplacementTest.java
│   └── SteadyStateReplacementTest.java
├── ValidationsTests/
│   ├── BinaryFeatureValidatorTest.java
│   ├── IntegerFeatureValidatorTest.java
│   └── DoubleFeatureValidatorTest.java
├── ProblemsTests/
│   ├── DiabetesDatasetTest.java
│   └── DiabetesFitnessFunctionTest.java (12 tests)
└── MLModelsTests/
    └── SimpleRandomForestTest.java (6 tests)
```

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=DiabetesFitnessFunctionTest

# Run specific test method
mvn test -Dtest=DiabetesFitnessFunctionTest#testValidFeatureSelection

# Generate coverage report (with JaCoCo plugin)
mvn clean test jacoco:report
```

### Key Test Scenarios

**Fitness Function Tests:**
- ✅ Valid feature selection (5-10 features)
- ✅ Boundary conditions (min/max features)
- ✅ Invalid chromosomes (too few/many features)
- ✅ Caching behavior (cache hits/misses)
- ✅ Thread safety (parallel evaluation)
- ✅ Performance benchmarks

**Operator Tests:**
- ✅ Selection preserves population size
- ✅ Crossover produces valid offspring
- ✅ Mutation respects constraints
- ✅ Replacement maintains elite solutions

---

## ⚙️ Configuration

### GeneticSettings Parameters

| Parameter | Type | Default | Description | Recommended Range |
|-----------|------|---------|-------------|-------------------|
| `populationSize` | int | 50 | Chromosomes per generation | 30-100 |
| `chromosomeLength` | int | 20 | Genes per chromosome | Matches feature count |
| `crossoverRate` | double | 0.8 | P(crossover) | 0.7-0.9 |
| `mutationRate` | double | 0.1 | P(mutation per gene) | 0.05-0.2 |
| `maxGenerations` | int | 100 | Evolution iterations | 50-200 |
| `targetFitness` | double | MAX | Early stop threshold | 0.95 for accuracy |
| `eliteCount` | int | 5 | Best solutions preserved | 2-10% of pop |
| `verbose` | boolean | true | Print progress | Enable for debugging |

### Fitness Function Tuning

```java
// Balance accuracy vs feature count
double featurePenalty = 0.01;  // Higher = fewer features
int numTrees = 50;             // Higher = slower but more accurate

DiabetesFitnessFunction<Boolean> fitness = 
    new DiabetesFitnessFunction<>(
        X_train, y_train,
        X_test, y_test,
        validator,
        featurePenalty,  // 0.01 = 1% penalty per feature
        numTrees,        // 50 trees = good balance
        totalFeatures
    );
```

---

## 📊 Results & Metrics

### Sample Output

```
🚀 Initializing population...
✓ Initial population created
  Population size: 50
  Chromosome length: 20

Evaluating 50 chromosomes with 4 threads...
  Progress: 50/50 (100%) - 8s elapsed   
  Completed in 8 seconds (avg 0.16s per chromosome)

Gen   0 | Best:   0.7842 | Avg:   0.6521 | Worst:   0.4103
Gen  10 | Best:   0.8156 | Avg:   0.7234 | Worst:   0.5821
Gen  25 | Best:   0.8521 | Avg:   0.7892 | Worst:   0.6543
Gen  50 | Best:   0.8734 | Avg:   0.8156 | Worst:   0.7012
Gen  75 | Best:   0.8821 | Avg:   0.8321 | Worst:   0.7456
Gen 100 | Best:   0.8891 | Avg:   0.8521 | Worst:   0.7823

============================================================
EVOLUTION COMPLETE!
============================================================
Total Generations: 100
Best Fitness: 0.8891
Best Solution: [0, 2, 5, 7, 11, 13, 18]

======================================
FINAL RESULT
======================================
Total Runtime: 5 min 23 sec (323000 ms)
Best Fitness: 0.8891

Selected Features: 7 / 20

Selected Feature Indices and Names:
---------------------------------------------------------------------------
  1. Feature  0: Age                    
  2. Feature  2: BMI                    
  3. Feature  5: Blood Pressure         
  4. Feature  7: Glucose Level          
  5. Feature 11: Insulin                
  6. Feature 13: Diabetes Pedigree      
  7. Feature 18: Pregnancies            
---------------------------------------------------------------------------

Cache Statistics:
  Hits: 4521, Misses: 479, Rate: 90.4%
```

### Fitness Evolution Graph (Conceptual)

```
Fitness
  1.0 ┤                                      ╭───────
  0.9 ┤                            ╭────────╯
  0.8 ┤                   ╭────────╯
  0.7 ┤          ╭────────╯
  0.6 ┤   ╭──────╯
  0.5 ┤╭──╯
  0.4 ┼─────────────────────────────────────────────────
      0        25        50        75        100
                    Generation
  
  ──── Best Fitness    ---- Avg Fitness
```

---

## 🔮 Future Improvements

### Short-term (Next Sprint)
- [ ] Add JavaDoc documentation for all public APIs
- [ ] Implement adaptive mutation rates (decrease over time)
- [ ] Add convergence detection (early stopping)
- [ ] Export results to CSV/JSON for analysis
- [ ] Add visualization dashboard (fitness plots)

### Medium-term (Next Quarter)
- [ ] Multi-objective optimization (accuracy + speed + interpretability)
- [ ] Support for other ML algorithms (SVM, Neural Networks)
- [ ] Hyperparameter auto-tuning (meta-GA)
- [ ] Real-time progress monitoring (WebSocket dashboard)
- [ ] Distributed GA across multiple machines (MPI/Spark)

### Long-term (Future Versions)
- [ ] Integration with WEKA/MOA for more algorithms
- [ ] Deep learning feature extraction (CNN + GA)
- [ ] Federated learning support (privacy-preserving)
- [ ] Medical database connectors (FHIR/HL7)
- [ ] Mobile app for risk assessment (Android/iOS)
- [ ] Cloud deployment (AWS Lambda/Azure Functions)

### Research Directions
- [ ] Hybrid GA-PSO algorithms
- [ ] Transfer learning across datasets
- [ ] Explainable AI (SHAP/LIME integration)
- [ ] Quantum-inspired genetic algorithms

---

## 👥 Contributors

This project is developed by **Group 7** (Mansoura University - Faculty of Engineering):

| Name               | Role                    | Contributions                                                                  | GitHub |
|--------------------|-------------------------|--------------------------------------------------------------------------------|--------|
| **Ahmed Kamel**    | ML & Core               | ML models, Fitness function, Core GA, Tests                                    | [@GEMIv1](https://github.com/GEMIv1) |
| **Mohamed Ashraf** | Operators & Integration | Selection, Mutation operators, Tests, Architecture, Main class & Documentation | [@MohamedAshrafFCAICU](https://github.com/MohamedAshrafFCAICU) |
| **Anas Mahmoud**   | Operators               | Crossover & Mutation operators, Tests                                          | [@ANASARABY](https://github.com/ANASARABY) |
| **Husam Abozid**   | Operators               | Mutation, Selection, and Tests                                                 | [@HusamAbozide](https://github.com/HusamAbozide) |
| **Omar Hatem**     | Operators               | Crossover & Tests                                                              | [@Omar-Hatem777](https://github.com/Omar-Hatem777) |

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2025 Group 7

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction...
```

---



## 🌟 Acknowledgments

- **Smile ML Library** for the excellent machine learning toolkit
- **JUnit & Mockito** communities for robust testing frameworks
- **UCI Machine Learning Repository** for diabetes datasets
- **Professor Sabah Sayed** for guidance and support
- **Cairo University** for providing resources

---


<div align="center">

**⭐ If you find this project useful, please consider giving it a star!**

**🍴 Fork it | 🐛 Report Bugs | 💡 Request Features**

Made with ❤️ by Group 7

[🔝 Back to Top](#-diabetes-feature-selection-using-genetic-algorithms)

</div>
