/**
 * 程序的入口类，用于启动个人所得税计算器应用程序。
 */
public class Main {
    /**
     * 程序的主入口点。
     * @param args 命令行参数。
     */
    public static void main(String[] args) {
        double threshold = 5000.0; // 起征点
        double[] rates = {0.03, 0.1, 0.2, 0.25, 0.3, 0.35, 0.45}; // 税率
        double[] brackets = {3000, 12000, 25000, 35000, 55000, 80000}; // 税级的上限

        TaxRate taxRate = new TaxRate(threshold, rates, brackets);
        PersonalIncomeTaxCalculator calculator = new PersonalIncomeTaxCalculator(taxRate);
        UserInterface ui = new UserInterface(calculator);
        ui.showMenu();
    }
}
