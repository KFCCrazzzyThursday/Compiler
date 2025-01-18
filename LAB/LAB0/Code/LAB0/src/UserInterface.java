import java.util.Scanner;

/**
 * UserInterface类用于用户交互，提供菜单并接收用户输入。
 */
public class UserInterface {
    private Scanner scanner;
    private PersonalIncomeTaxCalculator calculator;
    private TaxRate taxRate;
    /**
     * UserInterface的构造函数。
     * @param calculator 用于税额计算的计算器对象
     */
    public UserInterface(PersonalIncomeTaxCalculator calculator) {
        this.calculator = calculator;
        this.scanner = new Scanner(System.in);
        this.taxRate=calculator.getTaxRate();
    }

    /**
     * 显示主菜单并根据用户选择执行不同的操作。
     */
    public void showMenu() {
        while (true) {
            System.out.println("\n欢迎使用个人所得税计算器");
            System.out.println("1. 计算个人所得税");
            System.out.println("2. 调整起征点");
            System.out.println("3. 调整税率");
            System.out.println("4. 显示当前税率表");
            System.out.println("5. 退出");
            System.out.print("请选择一个操作：");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    calculateTax();
                    break;
                case 2:
                    adjustThreshold();
                    break;
                case 3:
                    adjustRates();
                    break;
                case 4:
                    showTaxRateTable();
                    break;
                case 5:
                    System.out.println("感谢使用，再见！");
                    return;
                default:
                    System.out.println("无效输入，请重新输入！");
            }
        }
    }

    /**
     * 根据用户输入的月收入计算应缴纳的税额。
     */
    private void calculateTax() {
        System.out.print("请输入您的月工资薪金总额: ");
        double income = scanner.nextDouble();
        double tax = calculator.calculateTax(income);
        System.out.printf("根据您的输入，您需要缴纳的个人所得税额为：%.2f元\n", tax);
    }

    /**
     * 调整税率的起征点。
     */
    private void adjustThreshold() {
        System.out.print("请输入新的起征点：");
        double newThreshold = scanner.nextDouble();
        taxRate.setThreshold(newThreshold);
        System.out.println("起征点已更新！");
    }

    /**
     * 为税率表提供新的税率。
     */
    private void adjustRates() {
        while (true) {
            System.out.println("当前税级的数量是 " +(taxRate.getBrackets().length+1));
            System.out.println("请输入新的税率（以逗号分隔），应包含 " + (taxRate.getBrackets().length+1) + " 个税率：");

            scanner.nextLine();
            String[] rateStrs = scanner.nextLine().split(",");
            if (rateStrs.length != (taxRate.getBrackets().length+1)) {
                System.out.println("输入的税率数量不正确，请输入 " + (taxRate.getBrackets().length+1) + " 个税率值。");
                continue; // 用户输入不正确时，继续循环，要求用户重新输入
            }

            try {
                double[] newRates = new double[rateStrs.length];
                for (int i = 0; i < rateStrs.length; i++) {
                    newRates[i] = Double.parseDouble(rateStrs[i].trim()) / 100.0; // 转换为小数
                }
                taxRate.setRates(newRates);
                System.out.println("税率已更新！");
                break; // 税率更新成功后跳出循环
            } catch (NumberFormatException e) {
                System.out.println("输入包含非法数字，请确保所有输入都是有效的数字。");
                // 错误的数字输入后，继续循环要求用户重新输入
            }
        }
    }

    /**
     * 显示当前的税率表。
     */
    private void showTaxRateTable() {
        System.out.println("\n当前的税率表如下：");
        System.out.println(taxRate.getTaxRateTable());
    }
}
