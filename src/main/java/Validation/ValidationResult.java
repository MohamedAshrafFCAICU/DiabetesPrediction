package Validation;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
    private final boolean valid;
    private final List<String> violations;

    public ValidationResult(boolean valid) {
        this.valid = valid;
        this.violations = new ArrayList<>();
    }

    public ValidationResult(boolean valid, List<String> violations) {
        this.valid = valid;
        this.violations = new ArrayList<>(violations);
    }

    public boolean isValid() {
        return valid;
    }

    public List<String> getViolations() {
        return new ArrayList<>(violations);
    }

    public void addViolation(String violation) {
        violations.add(violation);
    }

    @Override
    public String toString() {
        if (valid) {
            return "Valid";
        }
        return "Invalid: " + String.join(", ", violations);
    }

    public static ValidationResult valid() {
        return new ValidationResult(true);
    }

    public static ValidationResult invalid(String... violations) {
        ValidationResult result = new ValidationResult(false);
        for (String violation : violations) {
            result.addViolation(violation);
        }
        return result;
    }
}
