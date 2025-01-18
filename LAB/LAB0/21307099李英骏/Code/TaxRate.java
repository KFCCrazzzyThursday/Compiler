/**
 * TaxRate类用于管理税率和税级阈值。
 */
public class TaxRate {
    private double threshold; // 免税阈值
    private double[] rates; // 税率数组
    private double[] brackets; // 税级的上限

    /**
     * TaxRate构造函数。
     * @param threshold 免税起征点
     * @param rates 税率，每个税级的比率
     * @param brackets 各个税级的收入上限
     */
    public TaxRate(double threshold, double[] rates, double[] brackets) {
        this.threshold = threshold;
        this.rates = rates;
        this.brackets = brackets;
    }

    // Getter 和 Setter 方法
    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public double[] getRates() {
        return rates;
    }

    public void setRates(double[] rates) {
        this.rates = rates;
    }

    public double[] getBrackets() {
        return brackets;
    }

    public void setBrackets(double[] brackets) {
        this.brackets = brackets;
    }

    /**
     * 生成并返回当前税率表的格式化字符串表示形式。
     * @return 当前的税率表字符串
     */
    public String getTaxRateTable() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-6s %-16s %-8s\n", "级数", "应纳税所得额", "税率（%）"));
        for (int i = 0; i < rates.length; i++) {
            String range;
            if (i == 0) {
                range = String.format("0-%.0f元", brackets[i]);
            } else if (i < rates.length - 1) {
                range = String.format("%.0f-%.0f元", brackets[i - 1] + 0.01, brackets[i]);
            } else {
                range = String.format("超过%.0f元部分", brackets[i - 1] + 0.01);
            }
            sb.append(String.format("%-4d %-30s %-10.1f\n", i + 1, range, rates[i] * 100));
        }
        return sb.toString();
    }
}
