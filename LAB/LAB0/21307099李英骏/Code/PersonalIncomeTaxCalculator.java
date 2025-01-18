/**
 * 个人所得税计算器类，负责计算税额。
 */
public class PersonalIncomeTaxCalculator {
    private TaxRate taxRate;
    /**
     * 构造方法，接收一个TaxRate对象。
     * @param taxRate 税率对象。
     */
    public PersonalIncomeTaxCalculator(TaxRate taxRate) {
        this.taxRate = taxRate;
    }
    /**
     * 接口，返回税率对象。
     * @return taxRate 税率对象。
     */
    public TaxRate getTaxRate() {
        return taxRate;
    }
    /**
     * 根据收入计算税额。
     * @param income 收入金额。
     * @return 应缴纳的税额。
     */
    public double calculateTax(double income) {
        double threshold = taxRate.getThreshold(); // 免税阈值
        double[] brackets = taxRate.getBrackets(); // 税级的上限
        double[] rates = taxRate.getRates(); // 税率数组

        if (income <= threshold) {
            return 0;
        }

        double taxableIncome = income - threshold; // 应纳税所得额
        double tax = 0;

        // 用于记录当前处理的累计收入部分
        double currentBracketMax = 0;

        for (int i = 0; i < brackets.length; i++) {
            double currentBracketTax = 0; // 当前税级的税额
            if (taxableIncome > brackets[i]) {
                // 对于除最后一个税级以外的情况
                currentBracketTax = (brackets[i] - currentBracketMax) * rates[i];
                if(i==brackets.length-1) {
                    // 最后一个税级
                    currentBracketTax += (taxableIncome-brackets[i]) * rates[i+1];
                }
            } else {
                // 最后一个税级或者当收入在当前税级范围内的情况
                currentBracketTax = (taxableIncome - currentBracketMax) * rates[i];
                tax += currentBracketTax;
                break; // 已经计算到收入所在的税级，可以跳出循环
            }
            tax += currentBracketTax;
            currentBracketMax = brackets[i]; // 更新已处理的累计收入部分
        }

        return tax;
    }
}
