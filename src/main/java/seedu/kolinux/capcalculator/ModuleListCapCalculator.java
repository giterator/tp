package seedu.kolinux.capcalculator;

import seedu.kolinux.exceptions.KolinuxException;
import seedu.kolinux.module.ModuleDetails;

public class ModuleListCapCalculator extends CapCalculatorByCode {
    
    public static final String UNAVALIABLE_GRADE = "0";
    
    public ModuleListCapCalculator(String input) {
        super(input);
    }
    
    /*private boolean isNumeric(String input) {
        try {
            Double.parseDouble(input);
        } catch (NumberFormatException exception) {
            return false;
        }
        return true;
    }*/
    
    /*private boolean hasDesiredCap() {
        return isNumeric(modules.get(modules.size() - 1));
    }*/
    
    private boolean isInvalidModule(String module) {
        String[] moduleDescriptions = module.split("/");
        String grade = moduleDescriptions[1];
        return grade.equals("S") || grade.equals("U") || grade.equals("0");
    }
    
    /*private double getMcModulesWithGrade() {
        double totalMc = 0.0;
        for (String module : modules) {
            if (isInvalidModule(module)) {
                continue;
            }
            String[] moduleDescriptions = module.split("/");
            totalMc += Double.parseDouble(moduleDescriptions[1]);
        }
        return totalMc;
    }
    
    private double getMcModulesWithoutGrade() {
        double totalMc = 0.0;
        for (String module : invalidModules) {
            String[] moduleDescriptions = module.split("/");
            totalMc += Double.parseDouble(moduleDescriptions[1]);
        }
        return totalMc;
    }*/
    
    // return grade letter equal to or higher than cap
    /*private String getGradeLetter(double cap) {
        if (cap <= 0.0) {
            return "F";
        } else if (cap <= 1.0) {
            return "D";
        } else if (cap <= 1.5) {
            return "D+";
        } else if (cap <= 2.0) {
            return "C";
        } else if (cap <= 2.5) {
            return "C+";
        } else if (cap <= 3.0) {
            return "B-";
        } else if (cap <= 3.5) {
            return "B";
        } else if (cap <= 4.0) {
            return "B+";
        } else if (cap <= 4.5) {
            return "A-";
        } else if (cap <= 5.0) {
            return "A";
        } else {
            return "impossible";
        }
    }*/
    
    /*private String getMinimumGrade(double currentCap, double mcModuleWithGrade, double mcModuleWithoutGrade) {
        double desiredCap = Double.parseDouble(modules.get(modules.size() - 1));
        double minimumCap = ((desiredCap * mcModuleWithGrade) + (desiredCap * mcModuleWithoutGrade) - (currentCap * mcModuleWithGrade)) / mcModuleWithoutGrade;
        return getGradeLetter(minimumCap);
    }*/
    
    /*private String getExpectedGrades() {
        double currentCap = Double.parseDouble(getCap());
        double mcModuleWithGrade = getMcModulesWithGrade();
        double mcModuleWithoutGrade = getMcModulesWithoutGrade();
        return getMinimumGrade(currentCap, mcModuleWithGrade, mcModuleWithoutGrade);
    }*/

    public String executeCapCalculator() throws KolinuxException {
        checkModulesNotEmpty();
        /*String result;
        if (hasDesiredCap()) {
            result = getExpectedGrades();
        } else {
            result = getCap();
        }*/
        return getCap();
    }
}
